package org.madbunny.tictactoe.app.desktop.screen.game;

import org.madbunny.tictactoe.client.HttpClient;
import org.madbunny.tictactoe.core.datamodel.GameState;
import org.slf4j.Logger;

import java.util.Calendar;

class StateProvider {
    private static final long FETCH_FRESH_STATE_PERIOD_MS = 250;

    private final Logger logger;
    private final HttpClient client;
    private GameState currentState;
    private long lastFetchTimeMs;

    StateProvider(Logger logger, HttpClient client) {
        this.logger = logger;
        this.client = client;
        currentState = fetchGameState();
    }

    public GameState getGameState() {
        if (Calendar.getInstance().getTimeInMillis() - lastFetchTimeMs > FETCH_FRESH_STATE_PERIOD_MS) {
            var newState = fetchGameState();
            if (newState != null) {
                currentState = newState;
            }
        }
        return currentState;
    }

    public GameState startNewGame() {
        try {
            currentState = client.startNewGame().orElse(currentState);
        } catch (Exception e) {
            onFetchError("startNewGame", e);
        }
        return currentState;
    }

    public GameState setCross(int row, int col) {
        try {
            currentState = client.setCross(row, col).orElse(currentState);
        } catch (Exception e) {
            onFetchError("setCross", e);
        }
        return currentState;
    }

    public GameState setZero(int row, int col) {
        try {
            currentState = client.setZero(row, col).orElse(currentState);
        } catch (Exception e) {
            onFetchError("setZero", e);
        }
        return currentState;
    }

    private GameState fetchGameState() {
        GameState result = null;
        try {
            result = client.getGameState().orElse(currentState);
        } catch (Exception e) {
            onFetchError("fetchGameState", e);
        }
        lastFetchTimeMs = Calendar.getInstance().getTimeInMillis();
        return result;
    }

    private void onFetchError(String method, Exception e) {
        var msg = String.format("Cannot fetch game state in method %s from server due to an error: %s", method, e.getMessage());
        logger.error(msg);
    }
}
