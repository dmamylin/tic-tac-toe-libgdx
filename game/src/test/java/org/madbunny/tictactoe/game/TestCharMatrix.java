package org.madbunny.tictactoe.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.madbunny.tictactoe.game.util.CharMatrix;

class TestCharMatrix {
    private static final char ZERO = '0';
    private static final char ONE = '1';

    @Test
    void creation() {
        var matrix = new CharMatrix(1, 2);
        Assertions.assertEquals(1, matrix.getRowCount());
        Assertions.assertEquals(2, matrix.getColumnCount());
    }

    @Test
    void creationWithDefaultValue() {
        final char expected = 'A';
        var matrix = new CharMatrix(7, 4, expected);
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                Assertions.assertEquals(expected, matrix.getValue(i, j));
            }
        }
    }

    @Test
    void setAndGetValues() {
        var matrix = new CharMatrix(3, 4, ZERO);

        matrix.setValue(0, 0, ONE);
        Assertions.assertEquals(ONE, matrix.getValue(0, 0));

        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Assertions.assertEquals(ZERO, matrix.getValue(i, j));
            }
        }
    }

    @Test
    void getRowAndColumn() {
        // 1 0 1 0
        // 0 0 0 0
        // 0 1 0 0
        var matrix = new CharMatrix(3, 4, ZERO);
        matrix.setValue(0, 0, ONE);
        matrix.setValue(0, 2, ONE);
        matrix.setValue(2, 1, ONE);

        Assertions.assertArrayEquals(new char[]{ONE, ZERO, ONE, ZERO}, matrix.getRow(0));
        Assertions.assertArrayEquals(new char[]{ZERO, ZERO, ZERO, ZERO}, matrix.getRow(1));
        Assertions.assertArrayEquals(new char[]{ZERO, ONE, ZERO, ZERO}, matrix.getRow(2));

        Assertions.assertArrayEquals(new char[]{ONE, ZERO, ZERO}, matrix.getColumn(0));
        Assertions.assertArrayEquals(new char[]{ZERO, ZERO, ONE}, matrix.getColumn(1));
        Assertions.assertArrayEquals(new char[]{ONE, ZERO, ZERO}, matrix.getColumn(2));
        Assertions.assertArrayEquals(new char[]{ZERO, ZERO, ZERO}, matrix.getColumn(3));
    }

    @Test
    void getValues() {
        // 0 1 0
        // 1 1 0
        var matrix = new CharMatrix(2, 3, ZERO);
        matrix.setValue(0, 1, ONE);
        matrix.setValue(1, 0, ONE);
        matrix.setValue(1, 1, ONE);

        Assertions.assertArrayEquals(new char[]{ZERO, ONE}, matrix.getValues(0, 0, 1, 1));
        Assertions.assertArrayEquals(new char[]{ONE, ZERO}, matrix.getValues(0, 1, 1, 1));
        Assertions.assertArrayEquals(new char[]{ZERO, ONE}, matrix.getValues(0, 2, 1, -1));
        Assertions.assertArrayEquals(new char[]{ZERO, ONE}, matrix.getValues(0, 0, 1, 0));
        Assertions.assertArrayEquals(new char[]{ONE, ZERO}, matrix.getValues(1, 0, -1, 0));
        Assertions.assertArrayEquals(new char[]{ONE, ONE, ZERO}, matrix.getValues(1, 0, 0, 1));
        Assertions.assertArrayEquals(new char[]{ZERO, ONE, ONE}, matrix.getValues(1, 2, 0, -1));
    }
}