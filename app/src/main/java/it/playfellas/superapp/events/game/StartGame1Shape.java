package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1Shape;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Shape extends StartGame1Event {
    @Getter
    private Config1Shape conf;

    public StartGame1Shape(Config1Shape conf) {
        super(conf);
        this.conf = conf;
    }
}
