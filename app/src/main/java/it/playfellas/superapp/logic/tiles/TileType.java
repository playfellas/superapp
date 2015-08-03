package it.playfellas.superapp.logic.tiles;

/**
 * Created by affo on 03/08/15.
 */
public enum TileType {
    ABSTRACT,
    CONCRETE,
    PHOTO;

    @Override
    public String toString() {
        return this.name();
    }
}
