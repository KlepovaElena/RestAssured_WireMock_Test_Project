package apiClient;

import io.restassured.response.Response;
import org.hamcrest.Matcher;

public class ApiResponse {
    private final Response response;

    public ApiResponse(Response response) {
        this.response = response;
    }

    public ApiResponse expectStatus(int statusCode) {
        response.then().statusCode(statusCode);
        return this;
    }

    public ApiResponse expectBody(String path, Matcher<Object> matcher) {
        response.then().body(path, matcher);
        return this;
    }

    public ApiResponse expectErrorMessage(String path, Matcher<Object> matcher) {
        response.then().body(path, matcher);
        return this;
    }
}
