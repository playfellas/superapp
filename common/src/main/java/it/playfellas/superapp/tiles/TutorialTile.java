package it.playfellas.superapp.tiles;

import lombok.Getter;

/**
 * Created by affo on 14/09/15.
 */
public class TutorialTile extends Tile {
    private Tile tile;
    @Getter
    private boolean rw;

    public TutorialTile(Tile t, boolean rw) {
        super(t.getName(), t.getColor(), t.isDirectable(), t.getShape(), t.getType());
        this.tile = t;
        this.rw = rw;
    }
}
