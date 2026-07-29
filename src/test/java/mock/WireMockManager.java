package mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockManager {
    private final WireMockServer wireMockServer;
    private final UserMocks userMocks;

    public WireMockManager() {
        this.wireMockServer = new WireMockServer(options().port(8080).notifier(new ConsoleNotifier(false)));
        this.userMocks = new UserMocks(wireMockServer);
    }

    public void start() {
        wireMockServer.start();
    }

    public void stop() {
        if (wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    public void resetAll() {
        wireMockServer.resetAll();
    }

    public UserMocks user() {
        return userMocks;
    }
}
