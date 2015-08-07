package it.playfellas.superapp.logic.master.game1;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.StartGameEvent;
import it.playfellas.superapp.logic.Config1Direction;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 07/08/15.
 */
public class Master1Direction extends Master1Controller {
    public static final TileDirection baseDir = TileDirection.RIGHT;
    private Config1Direction conf;

    public Master1Direction(Config1Direction conf) {
        super(conf);
        this.conf = conf;
        conf.setBaseDirection(baseDir);
    }

    @Override
    protected void onBeginStage() {

    }

    @Override
    protected void onEndStage() {

    }

    @Override
    protected StartGameEvent getNewGameEvent() {
        return EventFactory.startGame1Direction(conf);
    }
}
