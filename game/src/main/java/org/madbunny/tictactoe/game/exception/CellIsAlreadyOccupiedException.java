package org.madbunny.tictactoe.game.exception;

public class CellIsAlreadyOccupiedException extends Exception {
    public CellIsAlreadyOccupiedException(int rowId, int colId, char by) {
        super(createErrorMessage(rowId, colId, by));
    }

    private static String createErrorMessage(int rowId, int colId, char by) {
        return String.format(
                "Cell (%d, %d) is already occupied by %c",
                rowId, colId, by
        );
    }
}
