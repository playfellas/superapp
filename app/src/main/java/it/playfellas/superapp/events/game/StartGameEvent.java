package it.playfellas.superapp.events.game;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.logic.common.Config;
import it.playfellas.superapp.logic.common.slave.SlaveController;
import lombok.Getter;

/**
 * Created by affo on 29/07/15.
 */
public class StartGameEvent extends NetEvent {
    @Getter
    private Class<SlaveController> controllerClass;
    @Getter
    private Config conf;

    public StartGameEvent(Class<SlaveController> controllerClass, Config conf) {
        this.controllerClass = controllerClass;
        this.conf = conf;
    }

    @Override
    public String toString() {
        return "Starting game: " + controllerClass.getSimpleName();
    }
}
