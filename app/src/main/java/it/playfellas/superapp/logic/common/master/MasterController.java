package it.playfellas.superapp.logic.common.master;

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
    private GameHistory history;

    public MasterController() {
        super();
        score = 0;
        stage = 0;
        history = new GameHistory();
        TenBus.get().register(this);
    }

    /**
     * Call from presenter
     */
    public void beginStage() {
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
    }

    private void endStage() {
        rttDownCounter.cancel();
        rttDownCounter.purge();
        TenBus.get().post(EventFactory.endStage());

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
        if (e.isRight()) {
            score++;
            history.right(player);
        } else {
            score = 0;
            history.wrong(player);
        }

        if (score >= maxScore) {
            // you win!
            endStage();
        }
    }
}
