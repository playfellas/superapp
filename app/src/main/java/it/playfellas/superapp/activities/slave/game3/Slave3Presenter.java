package it.playfellas.superapp.activities.slave.game3;

import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave3Presenter {

    private SlaveGame3Fragment slaveGame3Fragment;
    private Config3 config;

    private TenBus bus = TenBus.get();

    public Slave3Presenter() {
    }

    public void onTakeView(SlaveGame3Fragment slaveGame3Fragment, Config3 config) {
        this.slaveGame3Fragment = slaveGame3Fragment;

        this.config = config;
    }
}