package org.madbunny.tictactoe.game.exception;

public class InvalidCellIndexException extends Exception {
    public InvalidCellIndexException(int rowCount, int colCount, int rowId, int colId) {
        super();
    }

    private static String createErrorMessage(int rowCount, int colCount, int rowId, int colId) {
        return String.format(
                "A pair of indices must be in the range: (0, 0) <= (i, j) < (%d, %d), but it is: (%d, %d)",
                rowCount, colCount, rowId, colId
        );
    }
}
