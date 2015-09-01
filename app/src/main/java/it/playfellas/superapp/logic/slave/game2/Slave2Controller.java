package it.playfellas.superapp.logic.slave.game2;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.concurrent.Semaphore;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;

/**
 * Created by affo on 07/08/15.
 */
public class Slave2Controller extends SlaveController {
    private static final String TAG = Slave2Controller.class.getSimpleName();
    private Tile[] baseTiles;
    private TileSelector ts;
    private int rightPtr;

    private Semaphore l;

    public Slave2Controller(TileSelector ts) {
        super();
        this.ts = ts;
        rightPtr = 0;

        l = new Semaphore(0);
    }

    @Override
    public void init() {
        // We need init not to instantiate the dispenser,
        // This has to be done `onBaseTiles`.
    }

    private void waitForTiles() {
        try {
            l.acquire();
        } catch (InterruptedException e) {
            Log.e(TAG, "Interrupted while waiting for baseTiles", e);
            return;
        }
        l.release();
    }

    public Tile[] getBaseTiles() {
        waitForTiles();
        return baseTiles;
    }

    @Override
    protected void onBeginStage(BeginStageEvent e) {
        super.setDispenser(getDispenser());
    }

    @Override
    protected void onEndStage(EndStageEvent e) {
        int perms = l.drainPermits();
        if (perms > 1) {
            Log.d(TAG, "Wrong number of permits for internal semaphore: " + perms);
        }
    }

    @Override
    protected void onEndGame(EndGameEvent e) {
    }

    @Override
    protected boolean isTileRight(Tile t) {
        if (rightPtr >= baseTiles.length) {
            Log.d(TAG, "The stage should have been already finished: " + rightPtr + " right answers > " + baseTiles.length);
            return false;
        }

        if (t.equals(baseTiles[rightPtr])) {
            rightPtr++;
            return true;
        }
        return false;
    }

    @Override
    protected TileDispenser getDispenser() {
        return new SizeDispenser(ts, getBaseTiles());
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        baseTiles = e.getTiles();
        l.release();
    }
}
