package responses;

public class ErrorResponses {
    public static final String USER_ALREADY_EXISTS = "User already exists";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_USER_ID_FORMAT = "Error: Invalid user id format";
    public static final String FORBIDDEN_DELETE_ADMIN = "Forbidden: You don't have permission to delete Admin";
    public static final String MUST_NOT_BE_NULL = "Must not be null";

    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String INVALID_PASSWORD_FORMAT = "Invalid password format";
    public static final String INVALID_USERNAME_FORMAT = "Invalid username format";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";

    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
}