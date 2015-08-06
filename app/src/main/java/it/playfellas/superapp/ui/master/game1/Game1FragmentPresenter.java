package it.playfellas.superapp.ui.master.game1;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.PhotoEvent;
import it.playfellas.superapp.events.ui.ScoreUpdateEvent;
import it.playfellas.superapp.events.ui.UIEndStageEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;
import it.playfellas.superapp.network.TenBus;

public class Game1FragmentPresenter {

    private static final String TAG = Game1FragmentPresenter.class.getSimpleName();
    private Game1Fragment fragment;
    private Config1 config;

    private Master1Controller master1;

    public void onTakeView(Game1Fragment fragment) {
        this.fragment = fragment;
        this.config = (Config1) this.fragment.getArguments().getSerializable(Game1Fragment.CONFIG1_ARG);
        TenBus.get().register(this);
        this.master1 = new Master1Controller(config);
        this.fragment.initCentralImage(config.getNoStages());
        this.master1.beginStage();
    }

    //TODO remove all this if-else in the final version. They are here only for testing
    @Subscribe
    public void onBTPhotoEvent(PhotoEvent event) {
        if(fragment!=null && fragment.photo1ImageView!=null ) {
            if(event.getPhoto()!=null) {
                fragment.photo1ImageView.setImageBitmap(event.getPhoto());
            } else {
                Log.e(TAG, "onBTPhotoEvent, you received a null photo!!!!");
            }
        } else {
            Log.e(TAG, "onBTPhotoEvent error, very bad! Probably you should call " +
                    "ButterKnife.bind in GameFragment superclass, but it's only a supposition");
        }
    }

    @Subscribe
    public void onUiEndStageEvent(UIEndStageEvent event) {
        //pass the current stage number and the total number of stages
        Log.d(TAG, "nextStage: " + event.getStageNumber() + " over " + config.getNoStages());
        fragment.updateStageImage(event.getStageNumber(), config.getNoStages());
        master1.beginStage();
    }

    @Subscribe
    public void onUiScoreEvent(ScoreUpdateEvent event) {
        //TODO this is the stage score. To create the global score sum the partial score.
        Log.d(TAG, "scoreUpdate: " + event.getScore());
        fragment.setScore(event.getScore());
    }


}
