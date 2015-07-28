package it.playfellas.superapp.events;

import it.playfellas.superapp.logic.common.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 27/07/15.
 */
public class NewTileEvent extends TileEvent {
    public NewTileEvent(Tile t) {
        super(t);
    }
}
