package it.playfellas.superapp.logic.tiles;

import android.graphics.Color;

/**
 * Created by affo on 31/07/15.
 */
public enum TileColor {
    NONE(42),
    RED(Color.RED),
    BLUE(Color.BLUE),
    YELLOW(Color.YELLOW),
    WHITE(0xFFFFFF),
    BLACK(0x000000),
    GREEN(Color.GREEN),
    LIGHT_BLUE(0x88BAD4),
    ORANGE(0xFFA112);

    private String hex;

    TileColor(int androidColor) {
        this.hex = String.format("#%06X", (0xFFFFFF & androidColor));
    }

    public String hex() {
        return hex;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
