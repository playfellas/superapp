package it.playfellas.superapp.logic.common.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.ClickedTileEvent;
import it.playfellas.superapp.events.GameChangeEvent;
import it.playfellas.superapp.logic.common.tiles.Tile;
import it.playfellas.superapp.network.TenBus;
import lombok.Setter;

/**
 * Created by affo on 28/07/15.
 */
public abstract class SlaveController {
    private static final String TAG = SlaveController.class.getSimpleName();
    @Setter
    protected TileDispenser dispenser; // apply strategy here
    private float currentRtt; // TODO: set timer that increments periodically rtt

    public SlaveController() {
        super();
        TenBus.get().register(this);
    }

    // override to implement the logic of the game
    public abstract boolean isTileRight(Tile t);

    @Subscribe
    public void onGameChange(GameChangeEvent e) {
        Class<TileDispenser> dispenserClass = e.getDispenserClass();
        TileDispenser newDispenser = null;
        try {
            // TileDispenser constructor should be empty!
            newDispenser = dispenserClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Log.e(TAG, "Error in instantiating new " + dispenserClass.getSimpleName(), ex);
            Log.i(TAG, "Going on with " + dispenser.getClass().getSimpleName());
            return;
        }

        dispenser.kill();
        setDispenser(newDispenser);
        dispenser.setRtt(currentRtt);
        dispenser.dispense();
    }

    @Subscribe
    public void onTileClicked(ClickedTileEvent e) {
        Tile t = e.getTile();
        if (isTileRight(t)) {
            Log.d(TAG, "Clicked on right tile");
            //TODO: post NetEvent for right tile
        } else {
            Log.d(TAG, "Clicked on wrong tile");
            //TODO: post Netevent for wrong tile
        }
    }
}
