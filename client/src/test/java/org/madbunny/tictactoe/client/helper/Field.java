package org.madbunny.tictactoe.client.helper;

import org.madbunny.tictactoe.core.FieldCell;

import java.util.Arrays;
import java.util.stream.Stream;

public class Field {
    private static final int FIELD_SIZE = 3;

    private final String[] data = createEmptyField();

    private Field() {
    }

    public static Field createEmpty() {
        return new Field();
    }

    public Stream<String> streamRows() {
        return Arrays.stream(data);
    }

    public void setValue(char value, int row, int col) {
        var newRow = new char[FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            newRow[i] = data[row].charAt(i);
        }
        newRow[col] = value;
        data[row] = new String(newRow);
    }

    private static String[] createEmptyField() {
        var row = new char[FIELD_SIZE];
        var field = new String[FIELD_SIZE];
        Arrays.fill(row, FieldCell.EMPTY);
        Arrays.fill(field, new String(row));
        return field;
    }
}
