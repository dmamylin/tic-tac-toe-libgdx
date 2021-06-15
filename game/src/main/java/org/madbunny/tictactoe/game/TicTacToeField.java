package org.madbunny.tictactoe.game;

import org.madbunny.tictactoe.core.FieldCell;
import org.madbunny.tictactoe.game.exception.CellIsAlreadyOccupiedException;
import org.madbunny.tictactoe.game.exception.InvalidCellIndexException;
import org.madbunny.tictactoe.game.util.CharMatrix;

import java.nio.CharBuffer;
import java.util.Optional;

class TicTacToeField {
    private static final int N_ROWS = 3;
    private static final int N_COLS = 3;

    private final CharMatrix cells = new CharMatrix(N_ROWS, N_COLS, FieldCell.EMPTY);

    TicTacToeField() {
    }

    public void setCross(int rowId, int colId)
            throws InvalidCellIndexException,
                   CellIsAlreadyOccupiedException {
        writeToCell(rowId, colId, FieldCell.CROSS);
    }

    public void setZero(int rowId, int colId)
            throws InvalidCellIndexException,
                   CellIsAlreadyOccupiedException {
        writeToCell(rowId, colId, FieldCell.ZERO);
    }

    public char[][] render() {
        var result = new char[N_ROWS][N_COLS];
        for (int i = 0; i < N_ROWS; i++) {
            result[i] = cells.getRow(i);
        }
        return result;
    }

    public Optional<Victory> getVictory() {
        for (int i = 0; i < N_ROWS; i++) {
            var unique = tryGetUniqueValue(cells.getRow(i));
            if (unique.isPresent()) {
                return Optional.of(new Victory(Victory.Type.BY_ROW, unique.get(), i));
            }
        }

        for (int i = 0; i < N_COLS; i++) {
            var unique = tryGetUniqueValue(cells.getColumn(i));
            if (unique.isPresent()) {
                return Optional.of(new Victory(Victory.Type.BY_COLUMN, unique.get(), i));
            }
        }

        // Primary diagonal
        var unique = tryGetUniqueValue(cells.getValues(0, 0, 1, 1));
        if (unique.isPresent()) {
            return Optional.of(new Victory(Victory.Type.BY_PRIMARY_DIAGONAL, unique.get(), 0));
        }

        // Secondary diagonal
        unique = tryGetUniqueValue(cells.getValues(0, N_COLS - 1, 1, -1));
        return unique.map(who -> new Victory(Victory.Type.BY_SECONDARY_DIAGONAL, who, 0));
    }

    private static Optional<Character> tryGetUniqueValue(char[] values) {
        var uniques = CharBuffer.wrap(values).chars().distinct().toArray();
        if (uniques.length != 1) {
            return Optional.empty();
        }
        var unique = Character.valueOf((char)uniques[0]);
        if (unique != FieldCell.EMPTY) {
            return Optional.of(unique);
        }
        return Optional.empty();
    }

    private void writeToCell(int rowId, int colId, char value)
            throws InvalidCellIndexException,
                   CellIsAlreadyOccupiedException {
        checkCellIndices(rowId, colId);
        checkCellIsAlreadyOccupied(cells, rowId, colId);
        cells.setValue(rowId, colId, value);
    }

    private static void checkCellIsAlreadyOccupied(CharMatrix data, int rowId, int colId)
            throws CellIsAlreadyOccupiedException {
        var by = data.getValue(rowId, colId);
        if (by == FieldCell.EMPTY) {
            return;
        }
        throw new CellIsAlreadyOccupiedException(rowId, colId, by);
    }

    private static void checkCellIndices(int rowId, int colId) throws InvalidCellIndexException {
        if (0 <= rowId && rowId < N_ROWS && 0 <= colId && colId < N_COLS) {
            return;
        }
        throw new InvalidCellIndexException(N_ROWS, N_COLS, rowId, colId);
    }
}
