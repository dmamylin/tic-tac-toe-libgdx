package org.madbunny.tictactoe.screen;

import com.badlogic.gdx.Gdx;
import org.lwjgl.util.Point;
import java.util.ArrayList;

public class GameField extends ScreenBase {
    private static final char EMPTY_CELL = ' ';
    private static final char CROSS_CELL = 'X';
    private static final char ZERO_CELL = 'O';
    private static final int FIELD_WIDTH = 3;
    private static final int FIELD_HEIGHT = 3;
    private static final int CELL_WIDTH = 200;
    private static final int CELL_HEIGHT = 200;
    private static final int GUI_HEIGHT = 50;

    private final ArrayList<Long> symbolSpriteIds = new ArrayList<>();
    private long backgroundSpriteId = INVALID_ENTITY_ID;

    private char[][] field = createField();
    private PlayerType currentTurn = PlayerType.CROSS;
    private GameState currentState = GameState.BEFORE_START;

    private enum PlayerType {
        CROSS,
        ZERO
    }

    private enum GameState {
        BEFORE_START,
        IN_PROGRESS,
        AFTER_VICTORY
    }

    private class VictoryState {
        public final boolean hasVictory;
        public final long victoryLineSpriteId;

        public VictoryState() {
            var verticalVictory = findVerticalVictory();
            if (verticalVictory != null) {
                hasVictory = true;
                victoryLineSpriteId = createVerticalLine(verticalVictory);
                return;
            }

            var horizontalVictory = findHorizontalVictory();
            if (horizontalVictory != null) {
                hasVictory = true;
                victoryLineSpriteId = createHorizontalLine(horizontalVictory);
                return;
            }

            var diagonalVictory = findDiagonalVictory();
            if (diagonalVictory != null) {
                hasVictory = true;
                victoryLineSpriteId = createDiagonalLine(diagonalVictory);
                return;
            }

            hasVictory = false;
            victoryLineSpriteId = INVALID_ENTITY_ID;
        }

        private Point findVerticalVictory() {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                char cellType = field[0][x];
                if (cellType == EMPTY_CELL) {
                    continue;
                }

                int y = 1;
                boolean success = true;
                for (; y < FIELD_HEIGHT; y++) {
                    if (cellType != field[y][x]) {
                        success = false;
                        break;
                    }
                }

                if (success) {
                    return new Point(x, 0);
                }
            }

            return null;
        }

        private Point findHorizontalVictory() {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                char cellType = field[y][0];
                if (cellType == EMPTY_CELL) {
                    continue;
                }

                int x = 1;
                boolean success = true;
                for (; x < FIELD_WIDTH; x++) {
                    if (cellType != field[y][x]) {
                        success = false;
                        break;
                    }
                }

                if (success) {
                    return new Point(0, y);
                }
            }

            return null;
        }

        private Point findDiagonalVictory() {
            char cellType = field[0][0];
            boolean hasVictory = true;
            for (int y = 0, x = 0; y < FIELD_HEIGHT && x < FIELD_WIDTH; y++, x++) {
                if (field[y][x] != cellType || field[y][x] == EMPTY_CELL) {
                    hasVictory = false;
                    break;
                }
            }

            if (hasVictory) {
                return new Point(0, 0);
            }

            cellType = field[0][FIELD_WIDTH - 1];
            hasVictory = true;
            for (int y = 0, x = FIELD_WIDTH - 1; y < FIELD_HEIGHT && x >= 0; y++, x--) {
                if (field[y][x] != cellType || field[y][x] == EMPTY_CELL) {
                    hasVictory = false;
                    break;
                }
            }

            if (hasVictory) {
                return new Point(FIELD_WIDTH - 1, 0);
            }

            return null;
        }

        private long createVerticalLine(Point origin) {
            var spriteId = createSprite("vertical_line.png");
            var sprite = getSprite(spriteId);
            sprite.setX(origin.getX() * CELL_WIDTH);
            sprite.setY(GUI_HEIGHT);
            return spriteId;
        }

        private long createHorizontalLine(Point origin) {
            var spriteId = createSprite("horizontal_line.png");
            var sprite = getSprite(spriteId);
            sprite.setX(origin.getX());
            sprite.setY(-origin.getY() * CELL_HEIGHT + (Gdx.graphics.getHeight() - CELL_HEIGHT));
            return spriteId;
        }

        private long createDiagonalLine(Point origin) {
            var spriteId = createSprite("diagonal_line.png");
            var sprite = getSprite(spriteId);
            sprite.setX(0);
            sprite.setY(GUI_HEIGHT);
            if (origin.getX() != 0) {
                sprite.flip(true, false);
            }
            return spriteId;
        }
    }

    @Override
    protected void onShow() {
        if (!isValidEntityId(backgroundSpriteId)) {
            backgroundSpriteId = createSprite("field_background.png");
        }
    }

    @Override
    protected void beforeRender() {
        scheduleForRender(backgroundSpriteId);
        for (var symbolSpriteId : symbolSpriteIds) {
            scheduleForRender(symbolSpriteId);
        }
    }

    public void onClick(int x, int y) {
        var cellId = determineCellId(x, y);
        if (cellId == null || currentState == GameState.AFTER_VICTORY) {
            return;
        }

        var currentValue = field[cellId.getY()][cellId.getX()];
        if (currentValue != EMPTY_CELL) {
            return;
        }

        if (currentTurn == PlayerType.CROSS) {
            setSymbol(cellId, "cross.png", CROSS_CELL);
            currentTurn = PlayerType.ZERO;
        } else if (currentTurn == PlayerType.ZERO) {
            setSymbol(cellId, "circle.png", ZERO_CELL);
            currentTurn = PlayerType.CROSS;
        }

        var victory = checkVictory();
        if (victory.hasVictory) {
            currentState = GameState.AFTER_VICTORY;
            symbolSpriteIds.add(victory.victoryLineSpriteId);
        } else if (currentState == GameState.BEFORE_START) {
            currentState = GameState.IN_PROGRESS;
        }
    }

    public void reset() {
        currentTurn = PlayerType.CROSS;
        currentState = GameState.BEFORE_START;
        for (var spriteId : symbolSpriteIds) {
            deleteSprite(spriteId);
        }
        symbolSpriteIds.clear();
        field = createField();
    }

    private void setSymbol(Point position, String spritePath, char symbol) {
        field[position.getY()][position.getX()] = symbol;
        var newCrossId = createSprite(spritePath);
        symbolSpriteIds.add(newCrossId);
        var newCross = getSprite(newCrossId);
        newCross.setX(position.getX() * CELL_WIDTH);
        newCross.setY(-position.getY() * CELL_HEIGHT + (Gdx.graphics.getHeight() - CELL_HEIGHT));
    }

    private VictoryState checkVictory() {
        return new VictoryState();
    }

    private static Point determineCellId(int x, int y) {
        x = x / CELL_WIDTH;
        y = y / CELL_HEIGHT;

        if (x < 0 || x >= FIELD_WIDTH) {
            return null;
        }

        if (y < 0 || y >= FIELD_HEIGHT) {
            return null;
        }

        return new Point(x, y);
    }

    private static char[][] createField() {
        var field = new char[FIELD_HEIGHT][FIELD_WIDTH];
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                field[i][j] = EMPTY_CELL;
            }
        }
        return field;
    }
}
