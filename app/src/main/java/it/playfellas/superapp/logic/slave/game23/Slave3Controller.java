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

    public Slave3Controller(TileSelector ts) {
        super();
        this.dispenser = new TowerDispenser(ts);

        TenBus.get().register(this);
    }

    @Override
    protected synchronized void onBeginStage(BeginStageEvent e) {
    }

    private int emptySlot() {
        int i = 0;
        for (; i < stack.length && stack[i] != null; i++) ;
        return i;
    }

    @Override
    protected synchronized boolean isTileRight(Tile t) {
        if (emptySlot() >= stack.length) {
            Log.d(TAG, "Stack exceeded!");
            return false;
        }

        int i = 0;
        do {
            Tile st = stack[i];

            if (st == null) {
                break;
            }

            if (!st.equals(getBaseTiles()[i])) {
                return false;
            }

            i++;
        } while (i < stack.length);

        return getBaseTiles()[i].equals(t);
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
        int i = emptySlot() - 1;
        boolean wrongPop = false;
        if (i >= 0) {
            wrongPop = getBaseTiles()[i].equals(stack[i]);
        }
        TenBus.get().post(EventFactory.pop(wrongPop));
    }

    @Subscribe
    public synchronized void onStackUpdate(YourTurnEvent e) {
        this.stack = e.getStack();
    }
}
