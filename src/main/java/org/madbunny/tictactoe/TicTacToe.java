package org.madbunny.tictactoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.madbunny.tictactoe.screen.GameField;

public class TicTacToe extends Game {
    @Override
    public void create() {
        setScreen(new GameField());
    }

    @Override
    public void render() {
        var screen = (GameField) getScreen();
        if (screen == null) {
            return;
        }

        onGameFieldUpdate(screen);
        screen.render(Gdx.graphics.getDeltaTime());
    }

    private void onGameFieldUpdate(GameField gameFieldScreen) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameFieldScreen.reset();
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gameFieldScreen.onClick(Gdx.input.getX(), Gdx.input.getY());
        }
    }
}
