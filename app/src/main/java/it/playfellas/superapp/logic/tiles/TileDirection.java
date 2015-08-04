package it.playfellas.superapp.logic.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileDirection {
    NONE,
    LEFT,
    RIGHT,
    UP,
    DOWN;

    @Override
    public String toString() {
        return this.name();
    }
}
