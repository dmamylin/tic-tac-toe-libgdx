package org.madbunny.tictactoe.server.view;

import com.google.gson.Gson;
import io.jooby.Context;
import io.jooby.MediaType;
import org.madbunny.tictactoe.core.datamodel.FieldIndex;
import org.madbunny.tictactoe.core.datamodel.GameState;
import org.madbunny.tictactoe.core.datamodel.Victory;
import org.madbunny.tictactoe.game.StateSnapshot;
import org.madbunny.tictactoe.game.TicTacToe;

public final class JsonView extends ViewBase {
    private static final int FIRST_FIELD_IDX = 0;
    private static final int LAST_FIELD_IDX = 2;

    private final Gson json = new Gson();

    public JsonView(TicTacToe game) {
        super(game);
    }

    @Override
    protected void setUpResponse(Context ctx) {
        ctx.setResponseType(MediaType.json);
    }

    @Override
    protected String formatState(StateSnapshot state) {
        var model = new GameState();

        model.currentPlayer = state.currentPlayer;
        model.field = serializeField(state.field);
        state.victory.ifPresent(victory -> model.victory = serializeVictory(victory));
        state.error.ifPresent(error -> model.error = error);

        return json.toJson(model);
    }

    private static String[] serializeField(char[][] field) {
        var result = new String[field.length];
        for (int i = 0; i < field.length; i++) {
            result[i] = new String(field[i]);
        }
        return result;
    }

    private static Victory serializeVictory(org.madbunny.tictactoe.game.Victory victory) {
        var result = new Victory();
        result.winner = victory.who;
        result.line = new FieldIndex[2];
        var begin = result.line[0] = new FieldIndex();
        var end = result.line[1] = new FieldIndex();

        switch (victory.how) {
            case BY_ROW:
                begin.row = end.row = victory.id;
                begin.col = FIRST_FIELD_IDX;
                end.col = LAST_FIELD_IDX;
                break;

            case BY_COLUMN:
                begin.col = end.col = victory.id;
                begin.row = FIRST_FIELD_IDX;
                end.row = LAST_FIELD_IDX;
                break;

            case BY_PRIMARY_DIAGONAL:
                begin.row = begin.col = FIRST_FIELD_IDX;
                end.row = end.col = LAST_FIELD_IDX;
                break;

            case BY_SECONDARY_DIAGONAL:
                begin.row = end.col = FIRST_FIELD_IDX;
                begin.col = end.row = LAST_FIELD_IDX;
                break;
        }

        return result;
    }
}
