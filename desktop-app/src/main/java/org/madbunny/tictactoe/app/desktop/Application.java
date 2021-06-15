package org.madbunny.tictactoe.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Application {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 650;
    private static final String WINDOW_TITLE = "Tic-Tac-Toe";

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WINDOW_WIDTH;
        config.height = WINDOW_HEIGHT;
        config.title = WINDOW_TITLE;
        config.resizable = false;
        new LwjglApplication(new GameController(), config);
    }
}
