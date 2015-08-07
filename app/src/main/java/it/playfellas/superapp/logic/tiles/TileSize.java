package it.playfellas.superapp.logic.tiles;

import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public enum TileSize {
    S(1),
    M((float) 1.5),
    L(2),
    XL((float) 2.5);

    @Getter
    private float multiplier;

    TileSize(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
