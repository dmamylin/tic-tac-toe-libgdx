package org.madbunny.tictactoe.game;

import java.util.Optional;

public class StateSnapshot {
    public final char currentPlayer;
    public final char[][] field;
    public final Optional<Victory> victory;
    public final Optional<String> error;

    StateSnapshot(char currentPlayer, TicTacToeField field) {
        this.currentPlayer = currentPlayer;
        this.field = field.render();
        this.victory = field.getVictory();
        error = Optional.empty();
    }

    StateSnapshot(char currentPlayer, TicTacToeField field, String error) {
        this.currentPlayer = currentPlayer;
        this.field = field.render();
        this.victory = field.getVictory();
        this.error = Optional.of(error);
    }
}
