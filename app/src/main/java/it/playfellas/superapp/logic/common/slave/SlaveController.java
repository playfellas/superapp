package it.playfellas.superapp.logic.common.slave;

import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.ClickedTileEvent;
import it.playfellas.superapp.events.EventFactory;
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
    //TODO: from config, based on the difficultyLevel
    private static final float defaultRtt = 5;
    private static final int rttPeriod = 10;
    private static final float minRtt = 3;
    private static final int noDifficultySteps = 5;

    private static final float decreaseFraction = Math.abs(defaultRtt - minRtt) / noDifficultySteps;
    private float currentRtt;
    private Timer rttDownCounter;

    public SlaveController() {
        super();
        TenBus.get().register(this);
    }

    // override to implement the logic of the game
    public abstract boolean isTileRight(Tile t);

    public void start() {
        rttDownCounter = new Timer(true);
        resetRtt();
        // scheduling `decreaseRtt` after `rttPeriod`
        // with period `rttPeriod`.
        rttDownCounter.schedule(new TimerTask() {
            @Override
            public void run() {
                decreaseRtt();
            }
        }, rttPeriod, rttPeriod);
    }

    public void stop() {
        rttDownCounter.cancel();
        rttDownCounter.purge();
    }

    private void resetRtt() {
        currentRtt = defaultRtt;
    }

    private void decreaseRtt() {
        if (currentRtt <= minRtt) {
            rttDownCounter.cancel();
            return;
        }
        
        currentRtt -= decreaseFraction;
        TenBus.get().post(EventFactory.rttUpdate(currentRtt));
        dispenser.setRtt(currentRtt);
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
}
