package org.madbunny.tictactoe.server.view;

import io.jooby.Context;
import org.madbunny.tictactoe.game.StateSnapshot;
import org.madbunny.tictactoe.game.TicTacToe;
import org.madbunny.tictactoe.game.util.MatrixIndex;

public abstract class ViewBase {
    private final TicTacToe game;

    public ViewBase(TicTacToe game) {
        this.game = game;
    }

    public String onStartNewGame(Context ctx) {
        setUpResponse(ctx);
        return formatState(game.startNewGame());
    }

    public String onGetGameState(Context ctx) {
        setUpResponse(ctx);
        return formatState(game.getState());
    }

    public String onSetCross(Context ctx) {
        setUpResponse(ctx);
        var index = extractFieldIndex(ctx);
        return formatState(game.setCross(index.row, index.col));
    }

    public String onSetZero(Context ctx) {
        setUpResponse(ctx);
        var index = extractFieldIndex(ctx);
        return formatState(game.setZero(index.row, index.col));
    }

    protected abstract void setUpResponse(Context ctx);

    protected abstract String formatState(StateSnapshot state);

    private static MatrixIndex extractFieldIndex(Context ctx) {
        return new MatrixIndex(
                ctx.path("row").intValue(-1),
                ctx.path("col").intValue(-1)
        );
    }
}
