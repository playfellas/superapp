package it.playfellas.superapp.activities.master.game1;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.PhotoEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;

public class Game1FragmentPresenter {

    private Game1Fragment fragment;
    private Config1 config = new Config1();

    private Master1Controller master1 = new Master1Controller(config);


    public void onTakeView(Game1Fragment fragment) {
        this.fragment = fragment;
        this.master1.beginStage();
    }

    @Subscribe public void onBTPhotoEvent(PhotoEvent event) {
        fragment.photo1ImageView.setImageBitmap(event.getPhoto());
    }

    @Subscribe public void onEndStageEvent(EndStageEvent event) {
        //TODO SET CENTRAL IMAGE WHEN END/BEGIN A STAGE
        //TODO HOW CAN I GET THE CURRENT STAGE NUMBER FROM THIS EVENT?
        //TODO I WANT TO GET private int stage; in MASTERCONTROLLER.
        fragment.nextStage(0 /* get stage from mastercontroller */,config.getNoStages());
    }

}
