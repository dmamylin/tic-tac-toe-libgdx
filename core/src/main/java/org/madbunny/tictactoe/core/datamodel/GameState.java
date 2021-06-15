package org.madbunny.tictactoe.core.datamodel;

public class GameState {
    public char currentPlayer; // Whose turn is now
    public String[] field;
    public Victory victory;
    public String error;
}
