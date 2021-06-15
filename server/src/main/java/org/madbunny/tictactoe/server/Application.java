package org.madbunny.tictactoe.server;

import org.madbunny.tictactoe.game.TicTacToe;
import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.madbunny.tictactoe.server.view.JsonView;
import org.madbunny.tictactoe.server.view.TextView;
import org.madbunny.tictactoe.server.view.ViewBase;

import javax.annotation.Nonnull;
import java.util.Map;

public class Application extends Jooby {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final TicTacToe GAME = new TicTacToe();
    private static final Map<String, ViewBase> VIEWS = Map.of(
            "text", new TextView(GAME),
            "json", new JsonView(GAME)
    );

    public Application() {
        get("/{view}/startNewGame", Application::onStartNewGame);
        get("/{view}/getGameState", Application::onGetGameState);
        get("/{view}/setCross/{row}/{col}", Application::onSetCross);
        get("/{view}/setZero/{row}/{col}", Application::onSetZero);
    }

    public static void main(String[] args) {
        LOG.debug("Hello, log!");
        runApp(args, Application::new);
    }

    @Nonnull
    private static String onStartNewGame(Context ctx) {
        return getView(ctx).onStartNewGame(ctx);
    }

    @Nonnull
    private static String onGetGameState(Context ctx) {
        return getView(ctx).onGetGameState(ctx);
    }

    @Nonnull
    private static String onSetCross(Context ctx) {
        return getView(ctx).onSetCross(ctx);
    }

    @Nonnull
    private static String onSetZero(Context ctx) {
        return getView(ctx).onSetZero(ctx);
    }

    private static ViewBase getView(Context ctx) {
        var viewName = ctx.path("view").value();
        var view = VIEWS.getOrDefault(viewName, null);
        if (view == null) {
            throw new StatusCodeException(StatusCode.NOT_FOUND);
        }
        return view;
    }
}
