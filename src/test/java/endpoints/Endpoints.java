package endpoints;

import config.TestConfig;

public class Endpoints {
    public static final String POST_USER = TestConfig.getProperty("post.user");
    public static final String DELETE_USER = TestConfig.getProperty("delete.user.id");
}
