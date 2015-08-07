package it.playfellas.superapp.ui.master;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.ui.ScoreUpdateEvent;
import it.playfellas.superapp.events.ui.UIEndStageEvent;
import it.playfellas.superapp.logic.Config;
import it.playfellas.superapp.logic.master.MasterController;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 07/08/15.
 */
public abstract class GamePresenter {
    private static final String TAG = GamePresenter.class.getSimpleName();
    private MasterController master;
    private int currentStage;
    private GameFragment fragment;
    private Config config;

    /**
     * Object to be registered on {@link it.playfellas.superapp.network.TenBus}.
     * We need it to make extending classes inherit @Subscribe methods.
     */
    private Object busListener;

    protected GamePresenter(final GameFragment fragment, final Config config) {
        this.fragment = fragment;
        this.config = config;
        this.currentStage = 0;
        this.busListener = new Object() {
            @Subscribe
            public void onUiBeginStageEvent(UIEndStageEvent event) {
                Log.d(TAG, "beginStage: " + event.getStageNumber() + " over " + config.getNoStages());
                currentStage = event.getStageNumber();
            }

            @Subscribe
            public void onUiEndStageEvent(UIEndStageEvent event) {
                //pass the current stage number and the total number of stages
                Log.d(TAG, "nextStage: " + event.getStageNumber() + " over " + config.getNoStages());
                fragment.updateStageImage(event.getStageNumber(), config.getNoStages());
                master.beginStage();
            }

            /**
             * Method to update the scores in the Fragment.
             *
             * @param event A {@link ScoreUpdateEvent}.
             */
            @Subscribe
            public void onUiScoreEvent(ScoreUpdateEvent event) {
                Log.d(TAG, "scoreUpdate - score from event: " + event.getScore() +
                        " , config max score per stage:" + config.getNoStages() + " , currentStage: " + currentStage);
                fragment.setCurrentStageScore(event.getScore());
                fragment.setGlobalScore(event.getScore(), config.getMaxScore(), currentStage);
            }
        };
        TenBus.get().register(busListener);
    }

    protected void init() {
        this.master = newMasterController();
    }
    
    protected abstract MasterController newMasterController();

    protected MasterController getMaster() {
        return master;
    }
}
