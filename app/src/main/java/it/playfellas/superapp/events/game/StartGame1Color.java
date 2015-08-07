package it.playfellas.superapp.events.game;

import it.playfellas.superapp.logic.Config1Color;
import lombok.Getter;

/**
 * Created by affo on 07/08/15.
 */
public class StartGame1Color extends StartGame1Event {
    @Getter
    private Config1Color conf;

    public StartGame1Color(Config1Color conf) {
        super(conf);
        this.conf = conf;
    }
}
