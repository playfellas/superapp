package it.playfellas.superapp.events;

import it.playfellas.superapp.logic.common.slave.TileDispenser;
import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 * Event for changes during an already-started game.
 */
public class GameChangeEvent extends NetEvent {
    @Getter
    private Class<TileDispenser> dispenserClass;

    public GameChangeEvent(Class<TileDispenser> dispenserClass) {
        this.dispenserClass = dispenserClass;
    }
}
