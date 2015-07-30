package it.playfellas.superapp.activities.slave.game1;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.common.Config;
import it.playfellas.superapp.logic.common.slave.SlaveController;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave1Presenter {

    private SlaveGame1Fragment slaveGame1Fragment;
    private SlaveController slaveControllerClass;
    private Config config;

    private TenBus bus = TenBus.get();

    public Slave1Presenter() {
    }

    public void onTakeView(SlaveGame1Fragment slaveGame1Fragment, Config config) {
        this.slaveGame1Fragment = slaveGame1Fragment;
//        this.slaveControllerClass = new SlaveController()

        this.config = config;
    }

    @Subscribe
    public void onNewTileEvent(NewTileEvent event) {
        slaveGame1Fragment.conveyorUp.addTile(event.getTile());
//        slaveGame1Fragment.conveyorDown.addTile(event.getTile());

    }
}
