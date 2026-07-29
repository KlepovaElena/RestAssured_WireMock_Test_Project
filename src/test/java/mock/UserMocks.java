package mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import data.GenerateData;
import endpoints.Endpoints;
import jsonHelper.JsonHelper;
import models.CreateUserRequest;

import static responses.ErrorResponses.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class UserMocks {

    GenerateData generateData = new GenerateData();

    private final WireMockServer server;

    private static final String USER_ID_PATH_PATTERN = "/api/user/\\d+";
    private static final String INVALID_USER_ID_PATH_PATTERN = "/api/user/(?!\\d+$).*";

    public UserMocks(WireMockServer server) {
        this.server = server;
    }

    public void stubDeleteSuccess() {
        server.stubFor(deleteUserRequest(USER_ID_PATH_PATTERN).willReturn(noContentResponse()));
    }

    public void stubDeleteNotFound() {
        server.stubFor(deleteUserRequest(USER_ID_PATH_PATTERN)
                .willReturn(jsonResponse(404, errorJson(USER_NOT_FOUND))));
    }

    public void stubDeleteForbiddenAdmin() {
        server.stubFor(deleteUserRequest(USER_ID_PATH_PATTERN)
                .willReturn(jsonResponse(403, errorJson(FORBIDDEN_DELETE_ADMIN))));
    }

    public void stubDeleteInvalidId() {
        server.stubFor(deleteUserRequest(INVALID_USER_ID_PATH_PATTERN)
                .willReturn(jsonResponse(400, errorJson(INVALID_USER_ID_FORMAT))));
    }

    public void stubDeleteEmptyResponse() {
        server.stubFor(deleteUserRequest(USER_ID_PATH_PATTERN)
                .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
    }

    public void stubDeleteUnauthorized() {
        server.stubFor(deleteRequestWithoutAuth(USER_ID_PATH_PATTERN)
                .willReturn(jsonResponse(401, errorJson(UNAUTHORIZED))));
    }

    public void stubCreateUserSuccess(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(createdResponse(String.format("""
                        {
                          "userId": "%s",
                          "userName": "%s"
                        }
                        """, generateData.generateUserId(), request.getUserName()))));
    }

    public void stubCreateUserConflict(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(jsonResponse(409, errorJson(USER_ALREADY_EXISTS))));
    }

    public void stubCreateUserBadRequest(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(jsonResponse(400, errorJson(MUST_NOT_BE_NULL))));
    }

    public void stubCreateUserInvalidPassword(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(jsonResponse(400, errorJson(INVALID_PASSWORD_FORMAT))));
    }

    public void stubCreateUserInvalidUsername(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(jsonResponse(400, errorJson(INVALID_USERNAME_FORMAT))));
    }

    public void stubCreateUserInvalidEmail(CreateUserRequest request) {
        server.stubFor(postRequest(Endpoints.POST_USER)
                .withRequestBody(equalToJson(JsonHelper.toJson(request)))
                .willReturn(jsonResponse(400, errorJson(INVALID_EMAIL_FORMAT))));
    }

    public void stubDeleteInternalError() {
        server.stubFor(deleteUserRequest(USER_ID_PATH_PATTERN)
                .willReturn(jsonResponse(500, errorJson(INTERNAL_SERVER_ERROR))));
    }

    private MappingBuilder deleteUserRequest(String pathPattern) {
        return delete(urlPathMatching(pathPattern))
                .withHeader("Authorization", equalTo("Test token"))
                .withHeader("Content-Type", equalTo("application/json"));
    }

    private MappingBuilder deleteRequestWithoutAuth(String pathPattern) {
        return delete(urlPathMatching(pathPattern))
                .withHeader("Content-Type", equalTo("application/json"));
    }

    private MappingBuilder postRequest(String endpoint) {
        return post(urlEqualTo(endpoint))
                .withHeader("Content-Type", equalTo("application/json"));
    }

    private ResponseDefinitionBuilder jsonResponse(int statusCode, String body) {
        return aResponse().withStatus(statusCode).withHeader("Content-Type", "application/json").withBody(body);
    }

    private ResponseDefinitionBuilder createdResponse(String body) {
        return jsonResponse(201, body);
    }

    private ResponseDefinitionBuilder noContentResponse() {
        return aResponse().withStatus(204).withHeader("Content-Type", "application/json");
    }

    private String errorJson(String message) {
        return String.format("""
                {
                  "error": "%s"
                }
                """, message);
    }
}