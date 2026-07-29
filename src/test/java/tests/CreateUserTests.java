package tests;

import apiClient.ApiClient;
import data.GenerateData;
import models.CreateUserRequest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static responses.ErrorResponses.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateUserTests extends TestBase {

    GenerateData generateData = new GenerateData();
    ApiClient apiClient = new ApiClient();

    @Test
    @DisplayName("Should create user when valid data is provided")
    public void shouldCreateUser_whenValidDataProvided() {

        String userName = generateData.generateUserName();
        String userPassword = generateData.generateUserPassword();
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserSuccess(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(201)
                .expectBody("userName", equalTo(userName))
                .expectBody("userId", notNullValue());
    }

    @Test
    @DisplayName("Should return 409 when registering an existing user")
    public void registerUserAlreadyExistsErrorTest() {
        String userName = generateData.generateUserName();
        String userPassword = generateData.generateUserPassword();
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserConflict(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(409)
                .expectErrorMessage("error", equalTo(USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Should return 400 when password is null")
    public void shouldReturn400_whenPasswordIsNull() {
        String userName = generateData.generateUserName();
        String userPassword = null;
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserBadRequest(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(MUST_NOT_BE_NULL));

    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", " ", ""})
    @DisplayName("Should return 400 when password is invalid")
    public void shouldReturn400_whenPasswordIsInvalid(String userPassword) {
        String userName = generateData.generateUserName();
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserInvalidPassword(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(INVALID_PASSWORD_FORMAT));

    }

    @Test
    @DisplayName("Should return 400 when username is null")
    public void shouldReturn400_whenUsernameIsNull() {
        String userName = null;
        String userPassword = generateData.generateUserPassword();
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserBadRequest(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(MUST_NOT_BE_NULL));

    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("Should return 400 when username is invalid")
    public void shouldReturn400_whenUsernameIsInvalid(String userName) {
        String userPassword = generateData.generateUserPassword();
        String userEmail = generateData.generateUserEmail();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserInvalidUsername(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(INVALID_USERNAME_FORMAT));

    }

    @Test
    @DisplayName("Should return 400 when email is null")
    public void shouldReturn400_whenEmailIsNull() {
        String userName = generateData.generateUserName();
        String userPassword = generateData.generateUserPassword();
        String userEmail = null;

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserBadRequest(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(MUST_NOT_BE_NULL));

    }

    @ParameterizedTest
    @ValueSource(strings = {"test.ru", "test@gmailcom", "testgmail.com"})
    @DisplayName("Should return 400 when email is invalid")
    public void shouldReturn400_whenEmailIsInvalid(String userEmail) {
        String userName = generateData.generateUserName();
        String userPassword = generateData.generateUserPassword();

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .userName(userName)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();

        wireMockManager.user().stubCreateUserInvalidUsername(userRequest);

        apiClient.createUser(userRequest)
                .expectStatus(400)
                .expectErrorMessage("error", equalTo(INVALID_USERNAME_FORMAT));

    }
}
