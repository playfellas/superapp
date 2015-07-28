package it.playfellas.superapp.events;

import it.playfellas.superapp.logic.common.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public abstract class TileEvent extends InternalEvent {
    @Getter
    private Tile tile;

    public TileEvent(Tile t) {
        this.tile = t;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + tile.toString();
    }
}
