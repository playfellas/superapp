package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.logic.slave.SlaveController;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 02/09/15.
 */
public abstract class Slave23Controller extends SlaveController {
    private static final String TAG = Slave2Controller.class.getSimpleName();
    private Tile[] baseTiles;
    private TileDispenserWBaseTiles dispenser;
    private int rightPtr;

    private Object busListener;

    public Slave23Controller() {
        super();
        this.busListener = new Object() {
            @Subscribe
            public void onBaseTiles(BaseTilesEvent e) {
                baseTiles = e.getTiles();
                dispenser.setBaseTiles(baseTiles);
            }
        };
        TenBus.get().register(busListener);
    }

    @Override
    public void init() {
        super.init();
        this.dispenser = getDispenser();
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
    protected abstract TileDispenserWBaseTiles getDispenser();
}
