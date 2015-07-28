package it.playfellas.superapp.events;

import lombok.Getter;

/**
 * Created by affo on 28/07/15.
 */
public class RTTUpdateEvent extends InternalEvent {
    @Getter
    private float rtt;

    public RTTUpdateEvent(float rtt) {
        super();
        this.rtt = rtt;
    }
}
