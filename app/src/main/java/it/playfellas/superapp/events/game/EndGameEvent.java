package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import lombok.Getter;

/**
 * Created by affo on 29/07/15.
 */
public class EndGameEvent extends NetEvent {
    @Getter
    private boolean won;

    public EndGameEvent(boolean won) {
        this.won = won;
    }
}
