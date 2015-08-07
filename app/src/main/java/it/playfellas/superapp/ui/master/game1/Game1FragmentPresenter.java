package it.playfellas.superapp.ui.master.game1;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.PhotoEvent;
import it.playfellas.superapp.events.ui.ScoreUpdateEvent;
import it.playfellas.superapp.events.ui.UIEndStageEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.master.BitmapUtils;

public class Game1FragmentPresenter {
    private static final String TAG = Game1FragmentPresenter.class.getSimpleName();
    private Game1Fragment fragment;
    private Config1 config;
    private Master1Controller master1;
    private int currentStage;

    public void onTakeView(Game1Fragment fragment) {
        this.fragment = fragment;
        this.config = (Config1) this.fragment.getArguments().getSerializable(Game1Fragment.CONFIG1_ARG);
        this.currentStage = 0;
        TenBus.get().register(this);
        this.master1 = new Master1Controller(config);
        this.fragment.initCentralImage(config.getNoStages());
        this.master1.beginStage();
    }

    //TODO remove all this if-else in the final version. They are here only for testing
    @Subscribe
    public void onBTPhotoEvent(PhotoEvent event) {
        if(fragment!=null && fragment.photo1ImageView!=null ) {
            if(event.getPhotoByteArray()!=null) {
                Bitmap bitmap = BitmapUtils.toBitmap(event.getPhotoByteArray());
                if(bitmap!=null) {
                    fragment.photo1ImageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "onBTPhotoEvent, The bitmap from BitmapUtils.toBitmap was null");
                }
            } else {
                Log.e(TAG, "onBTPhotoEvent, you received a null photo!!!!");
            }
        } else {
            Log.e(TAG, "onBTPhotoEvent error, very bad! Probably you should call " +
                    "ButterKnife.bind in GameFragment superclass, but it's only a supposition");
        }
    }

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
        master1.beginStage();
    }

    /**
     * Method to update the scores in the Fragment.
     * @param event A {@link ScoreUpdateEvent}.
     */
    @Subscribe
    public void onUiScoreEvent(ScoreUpdateEvent event) {
        Log.d(TAG, "scoreUpdate - score from event: " + event.getScore() +
                " , config max score per stage:" + config.getNoStages() + " , currentStage: " + currentStage);
        fragment.setCurrentStageScore(event.getScore());
        fragment.setGlobalScore(event.getScore(), config.getMaxScore(), currentStage);
    }
}
