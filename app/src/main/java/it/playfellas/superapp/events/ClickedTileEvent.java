package it.playfellas.superapp.events;

import it.playfellas.superapp.logic.common.tiles.Tile;

/**
 * Created by affo on 28/07/15.
 */
public class ClickedTileEvent extends TileEvent {
    public ClickedTileEvent(Tile t) {
        super(t);
    }
}
