package org.madbunny.tictactoe.game;

import org.madbunny.tictactoe.game.exception.CellIsAlreadyOccupiedException;
import org.madbunny.tictactoe.game.exception.InvalidCellIndexException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class TestTicTacToeField {
    @Test
    void setValue() {
        var field = new TicTacToeField();

        Assertions.assertDoesNotThrow(() -> field.setCross(0, 0));
        Assertions.assertDoesNotThrow(() -> field.setZero(0, 1));
        Assertions.assertDoesNotThrow(() -> field.setCross(1, 0));

        Assertions.assertThrows(CellIsAlreadyOccupiedException.class, () -> field.setCross(0, 0));
        Assertions.assertThrows(CellIsAlreadyOccupiedException.class, () -> field.setZero(0, 0));
        Assertions.assertThrows(CellIsAlreadyOccupiedException.class, () -> field.setCross(0, 1));
        Assertions.assertThrows(CellIsAlreadyOccupiedException.class, () -> field.setCross(1, 0));
        Assertions.assertThrows(CellIsAlreadyOccupiedException.class, () -> field.setZero(1, 0));

        Assertions.assertThrows(InvalidCellIndexException.class, () -> field.setCross(-1, 0));
        Assertions.assertThrows(InvalidCellIndexException.class, () -> field.setCross(3, 0));

        Assertions.assertThrows(InvalidCellIndexException.class, () -> field.setZero(-1, 0));
        Assertions.assertThrows(InvalidCellIndexException.class, () -> field.setZero(3, 0));
    }

    @Test
    void render() {
        var field = new TicTacToeField();

        Assertions.assertDoesNotThrow(() -> field.setCross(0, 0));
        Assertions.assertDoesNotThrow(() -> field.setZero(0, 1));
        Assertions.assertDoesNotThrow(() -> field.setCross(1, 0));
        Assertions.assertEquals("XO.\nX..\n...", fieldToString(field.render()));

        Assertions.assertDoesNotThrow(() -> field.setZero(0, 2));
        Assertions.assertDoesNotThrow(() -> field.setZero(1, 1));
        Assertions.assertDoesNotThrow(() -> field.setCross(1, 2));
        Assertions.assertDoesNotThrow(() -> field.setZero(2, 0));
        Assertions.assertDoesNotThrow(() -> field.setCross(2, 1));
        Assertions.assertDoesNotThrow(() -> field.setZero(2, 2));
        Assertions.assertEquals("XOO\nXOX\nOXO", fieldToString(field.render()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ".........",
            "XX.XX.OO.",
            "XXOOXXXOO",
            "X.X.X.OXO",
    })
    void whenNoVictory(String strField) {
        var field = fieldFromString(strField);
        var actual = field.getVictory();
        Assertions.assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(OnVictoryArgumentsProvider.class)
    void whenVictory(String strField, Victory expected) {
        var field = fieldFromString(strField);
        var actualOpt = field.getVictory();
        Assertions.assertTrue(actualOpt.isPresent());

        var actual = actualOpt.get();
        Assertions.assertEquals(expected.id, actual.id);
        Assertions.assertEquals(expected.who, actual.who);
        Assertions.assertEquals(expected.how, actual.how);
    }

    private static class OnVictoryArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("XXX......", new Victory(Victory.Type.BY_ROW, 'X', 0)),
                    Arguments.of("OOO......", new Victory(Victory.Type.BY_ROW, 'O', 0)),
                    Arguments.of("XXXOO..XX", new Victory(Victory.Type.BY_ROW, 'X', 0)),
                    Arguments.of("XX.OOO.XX", new Victory(Victory.Type.BY_ROW, 'O', 1)),
                    Arguments.of("XX.X.XOOO", new Victory(Victory.Type.BY_ROW, 'O', 2)),
                    Arguments.of("X..X..X..", new Victory(Victory.Type.BY_COLUMN, 'X', 0)),
                    Arguments.of(".O..O..O.", new Victory(Victory.Type.BY_COLUMN, 'O', 1)),
                    Arguments.of("..X..X..X", new Victory(Victory.Type.BY_COLUMN, 'X', 2)),
                    Arguments.of("X...X...X", new Victory(Victory.Type.BY_PRIMARY_DIAGONAL, 'X', 0)),
                    Arguments.of("O...O...O", new Victory(Victory.Type.BY_PRIMARY_DIAGONAL, 'O', 0)),
                    Arguments.of("..X.X.X..", new Victory(Victory.Type.BY_SECONDARY_DIAGONAL, 'X', 0)),
                    Arguments.of("..O.O.O..", new Victory(Victory.Type.BY_SECONDARY_DIAGONAL, 'O', 0))
            );
        }
    }

    private static TicTacToeField fieldFromString(String field) {
        assert(field.length() == 9);

        var result = new TicTacToeField();
        var strId = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                var value = field.charAt(strId);
                if (value == 'X') {
                    setCross(result, i, j);
                } else if (value == 'O') {
                    setZero(result, i, j);
                }
                strId++;
            }
        }
        return result;
    }

    private static String fieldToString(char[][] field) {
        var sb = new StringBuilder();
        for (int i = 0; i < field.length; i++) {
            for (char value : field[i]) {
                sb.append(value);
            }

            if (i + 1 < field.length) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static void setCross(TicTacToeField field, int row, int col) {
        try {
            field.setCross(row, col);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void setZero(TicTacToeField field, int row, int col) {
        try {
            field.setZero(row, col);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
