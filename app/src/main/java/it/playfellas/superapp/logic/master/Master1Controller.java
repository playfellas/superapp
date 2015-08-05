package it.playfellas.superapp.logic.master;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 31/07/15.
 */
public class Master1Controller extends MasterController {
    private Config1 conf;

    public Master1Controller(Config1 conf){
        super(conf);
        this.conf = conf;
    }

    @Override
    void onBeginStage() {

    }

    @Override
    void onEndStage() {

    }

    @Override
    void onAnswer(boolean rw) {
        int score = getScore();
        if (score != 0 && score % conf.getRuleChange() == 0) {
            TenBus.get().post(EventFactory.gameChange());
        }
    }

    @Override
    StartGameEvent getNewGameEvent() {
        return EventFactory.startGame1(conf);
    }
}
