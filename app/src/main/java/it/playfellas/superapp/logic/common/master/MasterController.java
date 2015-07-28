package it.playfellas.superapp.logic.common.master;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 28/07/15.
 */
public abstract class MasterController {
    //TODO: from config, based on the difficultyLevel
    private static final float defaultRtt = 5;
    private static final int rttPeriod = 10;
    private static final float minRtt = 3;
    private static final int noDifficultySteps = 5;

    private static final float decreaseFraction = Math.abs(defaultRtt - minRtt) / noDifficultySteps;
    private float currentRtt;
    private Timer rttDownCounter;

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
}
