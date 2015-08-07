package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1Direction;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Direction extends StartGame1Event {
    @Getter
    private Config1Direction conf;

    public StartGame1Direction(Config1Direction conf) {
        super(conf);
        this.conf = conf;
    }
}
