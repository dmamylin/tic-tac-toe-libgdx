package org.madbunny.tictactoe.app.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.madbunny.tictactoe.app.desktop.screen.game.GameFieldScreen;
import org.madbunny.tictactoe.client.HttpClient;

class GameController extends Game {
    private final HttpClient client = new HttpClient("http://localhost", 8080);

    @Override
    public void create() {
        setScreen(new GameFieldScreen(client));
    }

    @Override
    public void render() {
        var screen = (GameFieldScreen) getScreen();
        if (screen == null) {
            return;
        }

        onGameFieldUpdate(screen);
        screen.render(Gdx.graphics.getDeltaTime());
    }

    private void onGameFieldUpdate(GameFieldScreen gameFieldScreen) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameFieldScreen.reset();
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gameFieldScreen.onClick(Gdx.input.getX(), Gdx.input.getY());
        }
    }
}
