package org.madbunny.tictactoe.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Application extends Server {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        LOG.debug("Hello, log!");
        runApp(args, Application::new);
    }
}
