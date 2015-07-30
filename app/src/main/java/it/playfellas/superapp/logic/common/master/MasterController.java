package it.playfellas.superapp.logic.common.master;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RWEvent;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 28/07/15.
 */
public abstract class MasterController {
    private static final String TAG = MasterController.class.getSimpleName();

    private static final int maxScore = 10; //TODO: config
    private static final boolean increaseSpeed = true; //TODO: config
    private static final int noStages = 4; //TODO: config
    //TODO: from config, based on the difficultyLevel
    private static final float defaultRtt = 5;
    private static final int rttPeriod = 10;
    private static final float minRtt = 3;
    private static final int noDifficultySteps = 5;

    private static final float decreaseFraction = Math.abs(defaultRtt - minRtt) / noDifficultySteps;
    private float currentRtt;
    private Timer rttDownCounter;
    private int score;
    private int stage;
    private boolean stageRunning;
    private GameHistory history;

    public MasterController() {
        super();
        score = 0;
        stage = 0;
        stageRunning = false;
        history = new GameHistory();
        TenBus.get().register(this);
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

    synchronized void setScore(int score) {
        this.score = score;
    }

    synchronized int getScore() {
        return score;
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
            TenBus.get().post(EventFactory.startGame());
        }

        resetRtt();

        if (increaseSpeed) {
            // scheduling `decreaseRtt` after `rttPeriod`
            // with period `rttPeriod`.
            rttDownCounter.schedule(new TimerTask() {
                @Override
                public void run() {
                    decreaseRtt();
                }
            }, rttPeriod, rttPeriod);
        }

        TenBus.get().post(EventFactory.beginStage());
        stageRunning = true;

        onBeginStage();
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
        if (stage >= noStages) {
            TenBus.get().post(EventFactory.endGame());
            //TODO: save history to FireBase
        }
    }

    private void resetRtt() {
        currentRtt = defaultRtt;
        notifyRtt(currentRtt);
    }

    private void decreaseRtt() {
        if (currentRtt <= minRtt) {
            rttDownCounter.cancel();
            return;
        }

        currentRtt -= decreaseFraction;
        notifyRtt(currentRtt);
    }

    private void notifyRtt(float rtt) {
        TenBus.get().post(EventFactory.rttUpdate(rtt));
    }

    @Subscribe
    public synchronized void onRw(RWEvent e) {
        String player = e.deviceAddress;
        boolean rw = e.isRight();

        onAnswer(rw);

        if (rw) {
            history.right(player);
        } else {
            history.wrong(player);
        }

        // not >=, we want to fire endStage only once!
        if (getScore() == maxScore) {
            // you win!
            endStage();
        }
    }
}
