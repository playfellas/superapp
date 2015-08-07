package it.playfellas.superapp.logic.master.game2;

import org.apache.commons.lang3.ArrayUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileSize;
import it.playfellas.superapp.logic.tiles.TileType;

/**
 * Created by affo on 07/08/15.
 */
public abstract class Master2Controller extends MasterController {
    private Config2 conf;
    private TileSelector ts;

    public Master2Controller(TileSelector ts, Config2 conf) {
        super(conf);
        this.conf = conf;
        this.ts = ts;

        conf.setMaxScore(InternalConfig.NO_FIXED_TILES);
    }

    @Override
    protected void onBeginStage() {
        Tile[] tiles = newBaseTiles();
        TileSize[] sizes = getSizes();
        setSizes(tiles, sizes);
        // broadcast baseTiles for this stage
        EventFactory.baseTiles(tiles);
    }

    private Tile[] newBaseTiles() {
        return (Tile[]) ts.random(InternalConfig.NO_FIXED_TILES, new Type(BinaryOperator.EQUALS, TileType.ABSTRACT)).toArray();
    }

    private void setSizes(Tile[] tiles, TileSize[] sizes) {
        for (int i = 0; i < tiles.length; i++) {
            TileSize s = i < sizes.length ? sizes[i] : sizes[sizes.length - 1];
            tiles[i].setSize(s);
        }
    }

    /**
     * Use in extending classes, plz
     */

    protected TileSize[] getGrowing() {
        return TileSize.values(); // returned in the order they are declared
    }

    protected TileSize[] getDecreasing() {
        TileSize[] sizes = TileSize.values();
        ArrayUtils.reverse(sizes);
        return sizes;
    }

    @Override
    protected void onAnswer(boolean rw) {
        if (rw) {
            incrementScore();
        }
    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame2(conf);
    }

    protected abstract TileSize[] getSizes();
}
