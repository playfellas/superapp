package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 02/09/15.
 */
public class StackClickEvent extends TileEvent {
    public StackClickEvent(Tile t) {
        super(t);
    }
}
