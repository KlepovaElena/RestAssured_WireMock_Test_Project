package tests;

import apiClient.ApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static data.TestData.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static responses.ErrorResponses.*;

public class DeleteUserTests extends TestBase {

    ApiClient apiClient = new ApiClient();

    private static final int USER_ID = 1;

    @Test
    @DisplayName("Should return 204 when deleting user with valid ID")
    public void shouldReturn204_whenValidUserIdProvided() {

        wireMockManager.user().stubDeleteSuccess();

        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(204);

    }

    @Test
    @DisplayName("Should return 404 when user not found")
    public void shouldReturn404_whenUserNotFound() {

        wireMockManager.user().stubDeleteNotFound();

        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(404)
                .expectErrorMessage("error", equalTo(USER_NOT_FOUND));
    }

    @Test
    @DisplayName("Should be idempotent: second delete request should return 404")
    public void shouldBeIdempotent_whenDeletingSameUserTwice() {

        wireMockManager.user().stubDeleteSuccess();
        apiClient.deleteUser(USER_ID, AUTH_HEADER).expectStatus(204);

        wireMockManager.user().stubDeleteNotFound();
        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(404)
                .expectErrorMessage("error", equalTo(USER_NOT_FOUND));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", " ", ""})
    @DisplayName("Should return 400 when invalid user ID format is provided")
    public void shouldReturn400_whenInvalidUserIdFormat(String invalidId) {

        wireMockManager.user().stubDeleteInvalidId();

        apiClient.deleteUser(invalidId, AUTH_HEADER)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(INVALID_USER_ID_FORMAT));
    }

    @Test
    @DisplayName("Should return 403 when trying to delete admin user")
    public void shouldReturn403_whenDeletingAdmin() {

        wireMockManager.user().stubDeleteForbiddenAdmin();

        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(403)
                .expectErrorMessage("error", equalTo(FORBIDDEN_DELETE_ADMIN));

    }

    @Test
    @DisplayName("Should throw exception when server drops connection (empty response)")
    public void shouldThrowException_whenServerDropsConnection() {

        wireMockManager.user().stubDeleteEmptyResponse();

        Exception exception = assertThrows(Exception.class, () -> {
            apiClient.deleteUser(USER_ID, AUTH_HEADER);
        });

        System.out.println("Exception message: " + exception.getMessage());
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should return 401 when Authorization header is missing")
    public void shouldReturn401_whenAuthHeaderMissing() {
        wireMockManager.user().stubDeleteUnauthorized();

        apiClient.deleteUserWithoutAuth(USER_ID)
                .expectStatus(401)
                .expectErrorMessage("error", equalTo(UNAUTHORIZED));
    }

    @Test
    @DisplayName("Should retry request when server returns 500 error")
    public void shouldRetry_whenServerReturns500() {
        wireMockManager.user().stubDeleteInternalError();

        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(500)
                .expectErrorMessage("error", equalTo(INTERNAL_SERVER_ERROR));

        wireMockManager.user().stubDeleteSuccess();

        apiClient.deleteUser(USER_ID, AUTH_HEADER)
                .expectStatus(204);
    }
}
