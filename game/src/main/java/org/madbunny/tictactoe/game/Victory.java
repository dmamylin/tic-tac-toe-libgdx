package org.madbunny.tictactoe.game;

public class Victory {
    public enum Type {
        BY_ROW,
        BY_COLUMN,
        BY_PRIMARY_DIAGONAL,
        BY_SECONDARY_DIAGONAL
    }

    public final Type how;
    public final char who;
    public final int id;

    Victory(Type how, char who, int id) {
        this.how = how;
        this.who = who;
        this.id = id;
    }
}
