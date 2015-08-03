package it.playfellas.superapp.activities.master.game1;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.PhotoEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;

public class Game1FragmentPresenter {

    private Game1Fragment fragment;

    private Master1Controller master1 = new Master1Controller(new Config1());


    public void onTakeView(Game1Fragment fragment) {
        this.fragment = fragment;
//        master1.beginStage();
    }

    @Subscribe public void onBTPhotoEvent(PhotoEvent event) {
        fragment.photo1ImageView.setImageBitmap(event.getPhoto());
    }

    @Subscribe public void onEndStageEvent(EndStageEvent event) {
        //TODO SET CENTRAL IMAGE WHEN END/BEGIN A STAGE
        fragment.nextStage();
    }

}
