package it.playfellas.superapp.activities.slave.game1;

import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave1Presenter {

    private SlaveGame1Fragment slaveGame1Fragment;
    private Config1 config;

    private TenBus bus = TenBus.get();

    public Slave1Presenter() {
    }

    public void onTakeView(SlaveGame1Fragment slaveGame1Fragment, Config1 config) {
        this.slaveGame1Fragment = slaveGame1Fragment;

        this.config = config;
    }
}
