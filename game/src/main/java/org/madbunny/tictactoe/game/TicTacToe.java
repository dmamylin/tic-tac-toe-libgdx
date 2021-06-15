package org.madbunny.tictactoe.game;

import org.madbunny.tictactoe.core.FieldCell;
import org.madbunny.tictactoe.game.exception.CellIsAlreadyOccupiedException;
import org.madbunny.tictactoe.game.exception.InvalidCellIndexException;

import java.util.Random;

public class TicTacToe {
    private static class State {
        public TicTacToeField gameField = new TicTacToeField();
        public char currentPlayer = generateFirstPlayer();

        public StateSnapshot makeSnapshot() {
            return new StateSnapshot(currentPlayer, gameField);
        }

        public StateSnapshot makeSnapshot(String error) {
            return new StateSnapshot(currentPlayer, gameField, error);
        }

        private static char generateFirstPlayer() {
            var generator = new Random();
            var players = new char[]{FieldCell.CROSS, FieldCell.ZERO};
            return players[generator.nextInt(players.length)];
        }
    }

    private final Object lock = new Object();
    private State state = new State();

    public StateSnapshot startNewGame() {
        synchronized (lock) {
            state = new State();
            return state.makeSnapshot();
        }
    }

    public StateSnapshot setCross(int row, int col) {
        synchronized (lock) {
            if (state.gameField.getVictory().isPresent()) {
                return state.makeSnapshot();
            }

            if (state.currentPlayer != FieldCell.CROSS) {
                return state.makeSnapshot("Not your turn");
            }

            try {
                state.gameField.setCross(row, col);
            } catch (InvalidCellIndexException e) {
                return state.makeSnapshot(formatInvalidCellError(row, col));
            } catch (CellIsAlreadyOccupiedException e) {
                return state.makeSnapshot(formatAlreadyOccupiedCellError(row, col));
            }

            state.currentPlayer = FieldCell.ZERO;
            return state.makeSnapshot();
        }
    }

    public StateSnapshot setZero(int row, int col) {
        synchronized (lock) {
            if (state.gameField.getVictory().isPresent()) {
                return state.makeSnapshot();
            }

            if (state.currentPlayer != FieldCell.ZERO) {
                return state.makeSnapshot("Not your turn");
            }

            try {
                state.gameField.setZero(row, col);
            } catch (InvalidCellIndexException e) {
                return state.makeSnapshot(formatInvalidCellError(row, col));
            } catch (CellIsAlreadyOccupiedException e) {
                return state.makeSnapshot(formatAlreadyOccupiedCellError(row, col));
            }

            state.currentPlayer = FieldCell.CROSS;
            return state.makeSnapshot();
        }
    }

    public StateSnapshot getState() {
        synchronized (lock) {
            return state.makeSnapshot();
        }
    }

    private static String formatInvalidCellError(int row, int col) {
        return String.format("Invalid cell id: (%d, %d)", row, col);
    }

    private static String formatAlreadyOccupiedCellError(int row, int col) {
        return String.format("Cell with id: (%d, %d) is already occupied", row, col);
    }
}
