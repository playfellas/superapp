package it.playfellas.superapp.events.tile;

import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TutorialTile;

/**
 * Created by affo on 14/09/15.
 */
public class NewTutorialTileEvent extends NewTileEvent {
    private TutorialTile tile;

    public NewTutorialTileEvent(Tile t, boolean rw) {
        super(t);
        this.tile = new TutorialTile(t, rw);
    }

    // not using lombok to show overriding
    @Override
    public TutorialTile getTile() {
        return this.tile;
    }
}
