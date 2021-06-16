package org.madbunny.tictactoe.server;

import io.jooby.ExecutionMode;
import io.jooby.Jooby;
import io.jooby.ServerOptions;

public class EmbeddedServer {
    private static final String[] SERVER_ARGS = {};

    private final Jooby server = Jooby.createApp(SERVER_ARGS, ExecutionMode.EVENT_LOOP, Server::new);

    public EmbeddedServer(int port) {
        var options = new ServerOptions()
                .setPort(port);
        server.setServerOptions(options);
    }

    public ServerOptions getServerOptions() {
        return server.getServerOptions();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }
}
