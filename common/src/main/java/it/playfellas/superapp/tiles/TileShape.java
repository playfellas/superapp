package it.playfellas.superapp.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileShape {
    //TODO: dummy shapes, still waiting for real ones.
    NONE,
    SQUARE,
    CIRCLE,
    CUBE,
    TRIANGLE;

    @Override
    public String toString() {
        return this.name();
    }
}
