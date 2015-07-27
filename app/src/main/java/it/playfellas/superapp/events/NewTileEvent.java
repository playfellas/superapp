package it.playfellas.superapp.events;

import it.playfellas.superapp.logic.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 27/07/15.
 */
public class NewTileEvent extends InternalEvent {
    @Getter
    private Tile tile;

    public NewTileEvent(Tile t) {
        this.tile = t;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + tile.toString();
    }
}
