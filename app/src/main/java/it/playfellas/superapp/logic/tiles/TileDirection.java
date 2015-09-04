package it.playfellas.superapp.logic.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileDirection {
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
    NONE;

    @Override
    public String toString() {
        return this.name();
    }
}
