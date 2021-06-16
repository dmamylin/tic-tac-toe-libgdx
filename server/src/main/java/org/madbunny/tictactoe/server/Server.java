package org.madbunny.tictactoe.server;

import io.jooby.Context;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import org.madbunny.tictactoe.game.TicTacToe;
import org.madbunny.tictactoe.server.view.JsonView;
import org.madbunny.tictactoe.server.view.TextView;
import org.madbunny.tictactoe.server.view.ViewBase;

import javax.annotation.Nonnull;
import java.util.Map;

class Server extends Jooby {
    private static final TicTacToe GAME = new TicTacToe();
    private static final Map<String, ViewBase> VIEWS = Map.of(
            "text", new TextView(GAME),
            "json", new JsonView(GAME)
    );

    public Server() {
        get("/ping", ctx -> {
            ctx.setResponseType(MediaType.text);
            ctx.setResponseCode(StatusCode.OK);
            return "pong";
        });

        get("/{view}/startNewGame", Server::onStartNewGame);
        get("/{view}/getGameState", Server::onGetGameState);
        get("/{view}/setCross/{row}/{col}", Server::onSetCross);
        get("/{view}/setZero/{row}/{col}", Server::onSetZero);
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
