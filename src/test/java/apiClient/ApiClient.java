package apiClient;

import endpoints.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CreateUserRequest;

import static io.restassured.RestAssured.given;

public class ApiClient {
    public ApiResponse createUser(CreateUserRequest user) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .post(Endpoints.POST_USER);
        return new ApiResponse(response);
    }

    public ApiResponse deleteUser(int userId, String header) {
        Response response = given()
                .headers("Authorization", header)
                .pathParams("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Endpoints.DELETE_USER);
        return new ApiResponse(response);
    }

    public ApiResponse deleteUser(String userId, String header) {
        Response response = given()
                .headers("Authorization", header)
                .pathParams("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Endpoints.DELETE_USER);
        return new ApiResponse(response);
    }

    public ApiResponse deleteUserWithoutAuth(int userId) {
        Response response = given()
                .pathParams("id", userId)
                .contentType(ContentType.JSON)
                .when()
                .delete(Endpoints.DELETE_USER);
        return new ApiResponse(response);
    }
}
