package org.madbunny.tictactoe.game.util;

import java.util.ArrayList;
import java.util.Arrays;

public class CharMatrix {
    private final char[][] data;

    public CharMatrix(int rows, int columns) {
        data = new char[rows][];
        for (int i = 0; i < rows; i++) {
            data[i] = new char[columns];
        }
    }

    public CharMatrix(int rows, int columns, char defaultValue) {
        this(rows, columns);
        for (var row : data) {
            Arrays.fill(row, defaultValue);
        }
    }

    public char getValue(int row, int col) {
        return data[row][col];
    }

    public char[] getRow(int row) {
        return data[row];
    }

    public char[] getColumn(int col) {
        var cols = getRowCount();
        var column = new char[cols];
        for (int i = 0; i < getRowCount(); i++) {
            column[i] = getValue(i, col);
        }
        return column;
    }

    public char[] getValues(int row, int col, int rowStep, int colStep) {
        var values = new ArrayList<Character>();
        while (isCorrectIndex(row, col)) {
            values.add(data[row][col]);
            row += rowStep;
            col += colStep;
        }

        var result = new char[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    public void setValue(int row, int col, char value) {
        data[row][col] = value;
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        if (data.length > 0) {
            return data[0].length;
        }
        return 0;
    }

    private boolean isCorrectIndex(int row, int col) {
        if (data.length == 0) {
            return false;
        }
        var rowCount = data.length;
        var colCount = data[0].length;
        return 0 <= row && row < rowCount && 0 <= col && col < colCount;
    }
}
