package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.logic.common.Config;
import lombok.Getter;

/**
 * Created by affo on 29/07/15.
 */
public abstract class StartGameEvent extends NetEvent {
    @Override
    public String toString() {
        return "Starting game: " + getClass().getSimpleName();
    }
}
