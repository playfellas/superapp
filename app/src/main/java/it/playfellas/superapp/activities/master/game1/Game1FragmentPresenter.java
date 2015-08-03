package it.playfellas.superapp.activities.master.game1;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.master.Master1Controller;

public class Game1FragmentPresenter {

    private Game1Fragment fragment;

    private Master1Controller master1 = new Master1Controller(new Config1());


    public void onTakeView(Game1Fragment fragment) {
        this.fragment = fragment;

//        master1.beginStage();
    }

}
