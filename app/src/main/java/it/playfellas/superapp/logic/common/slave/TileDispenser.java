package it.playfellas.superapp.logic.common.slave;

import android.util.Log;

import java.util.Random;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.logic.common.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 27/07/15.
 * This class is an automatic Tile dispenser.
 * The tile dispenser needs a reference time (in seconds) to be set
 * using `setRtt` method __before__ being started --- otherwise, a default RTT of 5s will be used.
 * `rtt` will be used by the dispenser to determine when to provide new tiles.
 * Once istantiated and provided with an `rtt`,
 * the dispenser can be started using its `dispense` method.
 * Dispensed tiles will be posted on Otto bus as `NewTileEvent`s.
 * The dispenser can be stopped invoking its `kill` method.
 */
public abstract class TileDispenser implements Runnable {
    private static final String TAG = TileDispenser.class.getSimpleName();
    private static final float errorPercentage = (float) 0.4;
    private static final int tileDensity = 4;
    private static final float defaultRtt = (float) 5;
    private Thread internalThread;
    // rtt is the desired Round Trip Time
    // of a Tile on the screen
    private float rtt;
    private Random rng;

    public TileDispenser() {
        // trying to keep the constructor without parameters
        // because we want to instantiate it from its class
        // object (see OnGameChange in SlaveController)
        super();
        rtt = defaultRtt;
        rng = new Random();
        internalThread = null;
    }

    protected abstract Tile getTile();

    private void rndSleep() {
        try {
            // select a random sleep time adding or
            // subtracting the error to the given rtt
            // divided in equal intervals by the density of tiles
            float deltaT = getRtt() / tileDensity;
            float rndError = rng.nextFloat() * (deltaT * errorPercentage);
            float sleepTime = rng.nextBoolean() ? deltaT + rndError : deltaT - rndError;
            Thread.sleep((long) (sleepTime * 1000)); // from s to ms
        } catch (InterruptedException e) {
            kill();
        }
    }

    @Override
    public void run() {
        while (true) {
            Tile t = getTile();
            TenBus.get().post(EventFactory.newTile(t));
            rndSleep();
        }
    }

    public void dispense() {
        if (internalThread != null) {
            Log.w(TAG, "Trying to start dispenser while already started");
            return;
        }

        internalThread = new Thread(this);
        internalThread.start();
    }

    public void kill() {
        if (internalThread == null) {
            Log.w(TAG, "Trying to stop dispenser while already stopped");
            return;
        }

        internalThread.interrupt();
        internalThread = null;
    }

    public boolean isDispensing() {
        return internalThread != null;
    }

    public synchronized void setRtt(float rtt) {
        if (rtt < 0) {
            Log.w(TAG, "Cannot set RTT to negative value");
            return;
        }
        this.rtt = rtt;
    }

    private synchronized float getRtt() {
        return rtt;
    }
}
