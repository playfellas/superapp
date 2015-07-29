package it.playfellas.superapp.events;

import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public class RWEvent extends NetEvent {
    @Getter
    private boolean right;

    public RWEvent(boolean right) {
        this.right = right;
    }
}
