package tests;

import config.TestConfig;
import io.restassured.RestAssured;
import mock.WireMockManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    static WireMockManager wireMockManager = new WireMockManager();


    @BeforeAll
    static void setup() {
        wireMockManager.start();
        RestAssured.baseURI = TestConfig.getBaseUrl();
    }

    @AfterAll
    static void teardown() {
        wireMockManager.stop();
    }

    @AfterEach
    void resetStubs() {
        TestBase.wireMockManager.resetAll();
    }
}
