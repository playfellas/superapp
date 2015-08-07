package it.playfellas.superapp.events.ui;

import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class UIRWEvent extends UIEvent {
    @Getter
    private boolean right;

    public UIRWEvent(boolean right) {
        this.right = right;
    }
}
