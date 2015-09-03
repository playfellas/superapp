package it.playfellas.superapp.logic.slave.game23;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.YourTurnEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.StackClickEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 02/09/15.
 */
public class Slave3Controller extends Slave23Controller {
    private static final String TAG = Slave3Controller.class.getSimpleName();
    private TowerDispenser dispenser;
    private Tile[] stack;
    private boolean wrongPop;

    public Slave3Controller(TileSelector ts) {
        super();
        this.dispenser = new TowerDispenser(ts);

        TenBus.get().register(this);
    }

    @Override
    protected void onBeginStage(BeginStageEvent e) {
        this.wrongPop = false;
    }

    @Override
    protected synchronized boolean isTileRight(Tile t) {
        // finding first empty slot on the stack
        int i = 0;
        for (; i < stack.length && stack[i] != null; i++) ;

        if (i >= stack.length) {
            Log.d(TAG, "Stack exceeded!");
            return false;
        }

        this.wrongPop = false;

        if (getBaseTiles()[i].equals(t)) {
            this.wrongPop = true;
            return true;
        }
        return false;
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }

    @Subscribe
    public void onConveyorClicked(ClickedTileEvent e) {
        TenBus.get().post(EventFactory.push(e.getTile()));
    }

    @Subscribe
    public synchronized void onStackClicked(StackClickEvent e) {
        TenBus.get().post(EventFactory.pop(wrongPop));
    }

    @Subscribe
    public synchronized void onStackUpdate(YourTurnEvent e) {
        this.stack = e.getStack();
    }
}
