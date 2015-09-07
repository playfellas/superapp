package it.playfellas.superapp.logic.tiles;


/**
 * Created by affo on 31/07/15.
 */
public enum TileColor {
    NONE(42),
    RED(0xFFFF0000),
    BLUE(0xFF0000FF),
    YELLOW(0xFFFFFF00),
    WHITE(0xFFFFFF),
    BLACK(0x000000),
    GREEN(0xFF00FF00),
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
