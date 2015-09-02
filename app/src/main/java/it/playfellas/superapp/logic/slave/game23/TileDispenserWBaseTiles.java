package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import java.util.Random;
import java.util.concurrent.Semaphore;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 07/08/15.
 */
public abstract class TileDispenserWBaseTiles extends TileDispenser {
    private static final String TAG = TileDispenserWBaseTiles.class.getSimpleName();
    private Tile[] baseTiles;

    private Semaphore sem;

    public TileDispenserWBaseTiles() {
        super();
        this.sem = new Semaphore(0);
    }

    public void setBaseTiles(Tile[] baseTiles) {
        this.baseTiles = baseTiles;
        sem.release();
    }

    @Override
    public Tile next() {
        try {
            Log.d(TAG, "Waiting for baseTiles to be ready");
            sem.acquire();
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for baseTiles", e);
            return getDistractor(baseTiles);
        }

        Log.d(TAG, "baseTiles ready");

        Tile nextTile;
        if ((new Random()).nextFloat() < InternalConfig.GAME2_TGT_PROB) {
            nextTile = RandomUtils.choice(baseTiles);
        } else {
            nextTile = getDistractor(baseTiles);
        }

        sem.release();
        return nextTile;
    }

    protected abstract Tile getDistractor(Tile[] baseTiles);
}
