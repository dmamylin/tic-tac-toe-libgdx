package org.madbunny.tictactoe.app.desktop.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.madbunny.tictactoe.app.desktop.screen.ScreenBase;
import org.madbunny.tictactoe.client.HttpClient;
import org.madbunny.tictactoe.core.FieldCell;
import org.madbunny.tictactoe.core.datamodel.FieldIndex;
import org.madbunny.tictactoe.core.datamodel.Victory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameFieldScreen extends ScreenBase {
    private static class FieldId {
        public final int row;
        public final int col;

        FieldId(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class Cell {
        public final Sprite sprite;
        public final char symbol;

        Cell(Sprite sprite, char symbol) {
            this.sprite = sprite;
            this.symbol = symbol;
        }

        Cell() {
            sprite = null;
            symbol = FieldCell.EMPTY;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(GameFieldScreen.class);
    private static final int FIELD_WIDTH = 3;
    private static final int FIELD_HEIGHT = 3;
    private static final int CELL_WIDTH = 200;
    private static final int CELL_HEIGHT = 200;
    private static final int GUI_HEIGHT = 50;

    private final Texture crossTexture = createTexture("cross.png");
    private final Texture zeroTexture = createTexture("circle.png");
    private final Texture horizontalLineTexture = createTexture("horizontal_line.png");
    private final Texture verticalLineTexture = createTexture("vertical_line.png");
    private final Texture diagonalLineTexture = createTexture("diagonal_line.png");
    private final Sprite backgroundSprite = createSprite(createTexture("field_background.png"));
    private final Cell[][] cachedField = createEmptyField();
    private final StateProvider currentState;

    public GameFieldScreen(HttpClient client) {
        currentState = new StateProvider(LOG, client);
    }

    @Override
    protected void beforeRender() {
        actualizeCachedField();
        var state = currentState.getGameState();
        scheduleForRender(backgroundSprite);
        for (var row : cachedField) {
            for (var element : row) {
                if (element.sprite != null) {
                    scheduleForRender(element.sprite);
                }
            }
        }

        if (state.victory != null) {
            var victoryLine = makeVictory(state.victory);
            scheduleForRender(victoryLine);
        }
    }

    private void actualizeCachedField() {
        var grid = fieldAsGrid(currentState.getGameState().field);
        if (grid == null) {
            return;
        }

        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (cachedField[i][j].symbol == grid[i][j]) {
                    continue;
                }

                if (grid[i][j] == FieldCell.EMPTY) {
                    cachedField[i][j] = new Cell();
                } else {
                    var sprite = makeSprite(grid[i][j] == FieldCell.CROSS ? crossTexture : zeroTexture, i, j);
                    cachedField[i][j] = new Cell(sprite, grid[i][j]);
                }
            }
        }
    }

    public void onClick(int x, int y) {
        var fieldId = determineFieldId(x, y);
        if (fieldId == null || cachedField[fieldId.row][fieldId.col].symbol != FieldCell.EMPTY) {
            return;
        }

        var state = currentState.getGameState();
        if (state.victory != null) {
            return;
        }

        if (state.currentPlayer == FieldCell.CROSS) {
            currentState.setCross(fieldId.row, fieldId.col);
        } else if (state.currentPlayer == FieldCell.ZERO) {
            currentState.setZero(fieldId.row, fieldId.col);
        }
    }

    public void reset() {
        currentState.startNewGame();
        for (var row : cachedField) {
            for (int i = 0; i < FIELD_WIDTH; i++) {
                row[i] = new Cell();
            }
        }
    }

    private Sprite makeSprite(Texture texture, int row, int col) {
        var sprite = createSprite(texture);
        sprite.setX(col * CELL_WIDTH);
        sprite.setY(-row * CELL_HEIGHT + (Gdx.graphics.getHeight() - CELL_HEIGHT));
        return sprite;
    }

    private Sprite makeVictory(Victory victory) {
        var begin = victory.line[0];
        var end = victory.line[1];
        int dRow = Math.abs(end.row - begin.row);
        int dCol = Math.abs(end.col - begin.col);

        if (dRow == 0) {
            return makeSprite(horizontalLineTexture, begin.row, 0);
        } else if (dCol == 0) {
            return makeSprite(verticalLineTexture, FIELD_HEIGHT - 1, begin.col);
        }

        var diagonal = makeSprite(diagonalLineTexture, FIELD_HEIGHT - 1, 0);
        if (!isMainDiagonal(begin, end)) {
            diagonal.flip(true, false);
        }

        return diagonal;
    }

    private static Cell[][] createEmptyField() {
        var field = new Cell[FIELD_HEIGHT][FIELD_WIDTH];
        for (var row : field) {
            for (int i = 0; i < FIELD_WIDTH; i++) {
                row[i] = new Cell();
            }
        }
        return field;
    }

    private static FieldId determineFieldId(int x, int y) {
        int col = x / CELL_WIDTH;
        int row = y / CELL_HEIGHT;

        if (col < 0 || col >= FIELD_WIDTH) {
            return null;
        }

        if (row < 0 || row >= FIELD_HEIGHT) {
            return null;
        }

        return new FieldId(row, col);
    }

    private static char[][] fieldAsGrid(String[] rawField) {
        if (rawField.length < 1) {
            return null;
        }

        int rowCount = rawField.length;
        int colCount = rawField[0].length();
        char[][] grid = new char[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                grid[i][j] = rawField[i].charAt(j);
            }
        }

        return grid;
    }

    private static boolean isMainDiagonal(FieldIndex begin, FieldIndex end) {
        return (begin.row == 0 && begin.col == 0) || (end.row == 0 && end.col == 0);
    }
}
