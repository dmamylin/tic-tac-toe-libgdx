package org.madbunny.tictactoe.client.helper;

import org.madbunny.tictactoe.client.HttpClient;
import org.madbunny.tictactoe.core.datamodel.GameState;
import org.madbunny.tictactoe.server.EmbeddedServer;

import java.time.Duration;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Supplier;

public class Service {
    private static final Duration WAIT_START_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration WAIT_START_SLEEP_PERIOD = Duration.ofMillis(50);

    private final EmbeddedServer server = new EmbeddedServer(0);
    private final HttpClient client = new HttpClient(server.getServerOptions().getHost(), server.getServerOptions().getPort());

    public void start() {
        server.start();
        var deadlineMs = Calendar.getInstance().getTimeInMillis() + WAIT_START_TIMEOUT.toMillis();
        while (Calendar.getInstance().getTimeInMillis() < deadlineMs) {
            try {
                if (client.ping()) {
                    return;
                }
            } catch (Exception e) {
                // Ping request didn't succeed
            }

            try {
                Thread.sleep(WAIT_START_SLEEP_PERIOD.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
                throw new RuntimeException("The waiting thread was interrupted");
            }
        }

        throw new RuntimeException("Could not initialize the service");
    }

    public void stop() {
        server.stop();
    }

    public HttpClient getClient() {
        return client;
    }
}
