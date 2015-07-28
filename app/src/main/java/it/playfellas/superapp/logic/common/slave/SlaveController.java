package it.playfellas.superapp.logic.common.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.ClickedTileEvent;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.GameChangeEvent;
import it.playfellas.superapp.events.RTTUpdateEvent;
import it.playfellas.superapp.logic.common.tiles.Tile;
import it.playfellas.superapp.network.TenBus;
import lombok.Setter;

/**
 * Created by affo on 28/07/15.
 */
public abstract class SlaveController {
    private static final String TAG = SlaveController.class.getSimpleName();
    @Setter
    private TileDispenser dispenser; // apply strategy here
    private float currentRtt;

    public SlaveController() {
        super();
        TenBus.get().register(this);
        dispenser = getDispenser();
    }

    // override to implement the logic of the game
    abstract boolean isTileRight(Tile t);

    abstract TileDispenser getDispenser();

    public void start() {
        dispenser.dispense();
    }

    public void stop() {
        dispenser.kill();
    }

    @Subscribe
    public void onGameChange(GameChangeEvent e) {
        Class<TileDispenser> dispenserClass = e.getDispenserClass();
        TileDispenser newDispenser;
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
        boolean rw = isTileRight(t);
        String rwWord = rw ? "Correct" : "Incorrect";
        Log.d(TAG, rwWord + " answer given");
        TenBus.get().post(EventFactory.rw(rw));
    }

    @Subscribe
    public void onRttUpdate(RTTUpdateEvent e) {
        float rtt = e.getRtt();
        currentRtt = rtt;
        dispenser.setRtt(rtt);
    }
}
