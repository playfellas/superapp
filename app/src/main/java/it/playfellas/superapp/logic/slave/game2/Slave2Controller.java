package it.playfellas.superapp.logic.slave.game2;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 07/08/15.
 */
public class Slave2Controller extends SlaveController {
    private static final String TAG = Slave2Controller.class.getSimpleName();
    private Tile[] baseTiles;
    private SizeDispenser dispenser;
    private int rightPtr;

    public Slave2Controller(TileSelector ts) {
        super();
        this.dispenser = new SizeDispenser(ts);

        TenBus.get().register(this);
    }

    @Override
    protected synchronized void onBeginStage(BeginStageEvent e) {
        this.rightPtr = 0;
    }

    @Override
    protected void onEndStage(EndStageEvent e) {
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
            synchronized (this) {
                rightPtr++;
            }
            return true;
        }
        return false;
    }

    @Override
    protected TileDispenser getDispenser() {
        return dispenser;
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        this.baseTiles = e.getTiles();
        dispenser.setBaseTiles(baseTiles);
    }
}
