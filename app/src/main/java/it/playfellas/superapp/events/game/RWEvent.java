package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.logic.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public class RWEvent extends NetEvent {
    @Getter
    private Tile tile;
    @Getter
    private boolean right;

    public RWEvent(Tile t, boolean right) {
        this.tile = t;
        this.right = right;
    }
}
