package org.madbunny.tictactoe.server.view;

import org.madbunny.tictactoe.game.StateSnapshot;
import org.madbunny.tictactoe.game.TicTacToe;
import io.jooby.Context;
import io.jooby.MediaType;

public final class TextView extends ViewBase {
    public TextView(TicTacToe game) {
        super(game);
    }

    @Override
    protected void setUpResponse(Context ctx) {
        ctx.setResponseType(MediaType.text);
    }

    @Override
    protected String formatState(StateSnapshot state) {
        var sb = new StringBuilder();
        sb.append(serializeField(state.field));
        state.error.ifPresent(error -> {
            sb.append(String.format("Error: %s\n", error));
        });
        state.victory.ifPresentOrElse(victory -> {
            sb.append(String.format("Victory of: %c\n", victory.who));
        }, () -> {
            sb.append(String.format("Turn of: %c\n", state.currentPlayer));
        });
        return sb.toString();
    }

    private static String serializeField(char[][] field) {
        var sb = new StringBuilder();
        for (char[] row : field) {
            for (char value : row) {
                sb.append(value);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
