package it.playfellas.superapp.logic.master;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RWEvent;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 28/07/15.
 */
public abstract class MasterController {
    private static final String TAG = MasterController.class.getSimpleName();
    private Config conf;

    private float currentRtt;
    private Timer rttDownCounter;
    private int score;
    private int stage;
    private boolean stageRunning;
    private GameHistory history;

    // Object to be registered on `TenBus`.
    // We need it to make extending classes inherit
    // `@Subscribe` methods.
    private Object busListener;

    public MasterController(final Config conf) {
        super();
        this.conf = conf;
        score = 0;
        stage = 0;
        stageRunning = false;
        history = new GameHistory();

        busListener = new Object() {
            @Subscribe
            public void onRw(RWEvent e) {
                synchronized (MasterController.this) {
                    String player = e.deviceAddress;
                    boolean rw = e.isRight();

                    onAnswer(rw);

                    if (rw) {
                        history.right(player);
                    } else {
                        history.wrong(player);
                    }

                    // not >=, we want to fire endStage only once!
                    if (score == conf.getMaxScore()) {
                        // you win!
                        endStage();
                        return;
                    }
                }
            }
        };
        TenBus.get().register(busListener);
    }

    /**
     * Called exactly after slaves are notified
     * with a `BeginStageEvent`.
     */
    abstract void onBeginStage();

    /**
     * Called exactly after slaves are notified
     * with a `EndStageEvent`.
     */
    abstract void onEndStage();

    /**
     * Called every time an answer is given
     * (i.e. when a tile is clicked by a slave).
     * Typical actions could be updating the `score` using provided methods.
     *
     * @param rw boolean, true if answer is right, false otherwise.
     */
    abstract void onAnswer(boolean rw);

    /**
     * @return The corresponding `StartGameEvent` (i.e. 1/2/3) to this `MasterController`
     */
    abstract StartGameEvent getNewGameEvent();

    synchronized void setScore(int score) {
        this.score = score;
    }

    synchronized void incrementScore() {
        this.score++;
    }

    synchronized void decrementScore() {
        this.score--;
    }

    synchronized void resetScore() {
        this.score = 0;
    }

    BluetoothDevice nextPlayer() {
        return TenBus.get().nextDevice();
    }

    /**
     * Call from presenter
     */
    public void beginStage() {
        if (stageRunning) {
            Log.w(TAG, "Cannot begin stage while stage is running. Returning silently...");
            return;
        }

        rttDownCounter = new Timer(true);
        if (stage == 0) {
            TenBus.get().post(getNewGameEvent());
        }

        resetRtt();

        if (conf.isSpeedUp()) {
            long updatePeriod = (long) (conf.getRttUpdatePeriod() * 1000); // from s to ms
            // scheduling `decreaseRtt` after `rttPeriod`
            // with period `rttPeriod`.
            rttDownCounter.schedule(new TimerTask() {
                @Override
                public void run() {
                    decreaseRtt();
                }
            }, updatePeriod, updatePeriod);
        }

        TenBus.get().post(EventFactory.beginStage());
        stageRunning = true;

        onBeginStage();
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized int getStage() {
        return stage;
    }

    private void endStage() {
        if (!stageRunning) {
            Log.w(TAG, "Cannot end stage while stage is not running. Returning silently...");
            return;
        }
        rttDownCounter.cancel();
        rttDownCounter.purge();

        TenBus.get().post(EventFactory.endStage());
        stageRunning = false;

        onEndStage();

        stage++;
        if (stage >= conf.getNoStages()) {
            TenBus.get().post(EventFactory.endGame());
            //TODO: save history to FireBase
        }
    }

    private void resetRtt() {
        currentRtt = conf.getDefaultRtt();
        notifyRtt(currentRtt);
    }

    private void decreaseRtt() {
        if (currentRtt <= conf.getMinRtt()) {
            rttDownCounter.cancel();
            return;
        }

        currentRtt -= conf.getRttDecreaseDelta();
        notifyRtt(currentRtt);
    }

    private void notifyRtt(float rtt) {
        TenBus.get().post(EventFactory.rttUpdate(rtt));
    }
}
