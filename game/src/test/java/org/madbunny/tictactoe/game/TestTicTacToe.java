package org.madbunny.tictactoe.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.madbunny.tictactoe.core.FieldCell;

public class TestTicTacToe {
    private static final char[] EMPTY_ROW = new char[] {
            FieldCell.EMPTY, FieldCell.EMPTY, FieldCell.EMPTY
    };
    private static final char[][] EMPTY_FIELD = new char[][] {
            EMPTY_ROW, EMPTY_ROW, EMPTY_ROW
    };

    @Test
    void startNewGame() {
        var game = new TicTacToe();
        var state = game.getState();
        Assertions.assertArrayEquals(EMPTY_FIELD, state.field);
        Assertions.assertTrue(state.error.isEmpty());
        Assertions.assertTrue(state.victory.isEmpty());

        doTurn(game, state.currentPlayer, 0, 0);
        state = game.getState();
        Assertions.assertNotEquals(FieldCell.EMPTY, state.field[0][0]);
        Assertions.assertTrue(state.error.isEmpty());
        Assertions.assertTrue(state.victory.isEmpty());

        game.startNewGame();
        state = game.getState();
        Assertions.assertArrayEquals(EMPTY_FIELD, state.field);
        Assertions.assertTrue(state.error.isEmpty());
        Assertions.assertTrue(state.victory.isEmpty());
    }

    @Test
    void notYourTurnError() {
        var game = new TicTacToe();
        var state = game.getState();
        var oppositePlayer = getOppositePlayer(state.currentPlayer);

        state = doTurn(game, oppositePlayer, 0, 0);
        Assertions.assertTrue(state.error.isPresent());
    }

    @Test
    void invalidCellError() {
        var game = new TicTacToe();
        var state = game.getState();

        state = doTurn(game, state.currentPlayer, 3, 3);
        Assertions.assertTrue(state.error.isPresent());

        state = doTurn(game, state.currentPlayer, -1, 0);
        Assertions.assertTrue(state.error.isPresent());
    }

    @Test
    void alreadyOccupiedCellError() {
        var game = new TicTacToe();
        var state = game.getState();

        state = doTurn(game, state.currentPlayer, 0, 0);
        Assertions.assertTrue(state.error.isEmpty());

        state = doTurn(game, state.currentPlayer, 0, 0);
        Assertions.assertFalse(state.error.isEmpty());

        state = doTurn(game, state.currentPlayer, 1, 1);
        Assertions.assertTrue(state.error.isEmpty());

        state = doTurn(game, state.currentPlayer, 0, 0);
        Assertions.assertFalse(state.error.isEmpty());

        state = doTurn(game, state.currentPlayer, 1, 1);
        Assertions.assertFalse(state.error.isEmpty());
    }

    private static StateSnapshot doTurn(TicTacToe game, char player, int row, int col) {
        if (player == FieldCell.CROSS) {
            return game.setCross(row, col);
        } else if (player == FieldCell.ZERO) {
            return game.setZero(row, col);
        }
        throw new RuntimeException(String.format("Unknown player: %c", player));
    }

    private static char getOppositePlayer(char player) {
        if (player == FieldCell.CROSS) {
            return FieldCell.ZERO;
        } else if (player == FieldCell.ZERO) {
            return FieldCell.CROSS;
        }
        throw new RuntimeException(String.format("Unknown player: %c", player));
    }
}
