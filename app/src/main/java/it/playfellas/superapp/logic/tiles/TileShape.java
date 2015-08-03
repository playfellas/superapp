package it.playfellas.superapp.logic.tiles;

/**
 * Created by affo on 31/07/15.
 */
public enum TileShape {
    //TODO: dummy shapes, still waiting for real ones.
    ANY,
    SQUARE,
    CIRCLE;

    @Override
    public String toString() {
        return this.name();
    }
}
