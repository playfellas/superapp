package it.playfellas.superapp.activities.slave.game2;

import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave2Presenter {

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;

    private TenBus bus = TenBus.get();

    public Slave2Presenter() {
    }

    public void onTakeView(SlaveGame2Fragment slaveGame2Fragment, Config2 config) {
        this.slaveGame2Fragment = slaveGame2Fragment;

        this.config = config;
    }
}
