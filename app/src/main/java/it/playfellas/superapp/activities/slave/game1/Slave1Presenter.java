package it.playfellas.superapp.activities.slave.game1;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave1Presenter {

    private SlaveGame1Fragment slaveGame1Fragment;
//    private Slave1 slave1;
    private Config1 config;

    private TenBus bus = TenBus.get();

    public Slave1Presenter() {
    }

    public void onTakeView(SlaveGame1Fragment slaveGame1Fragment, Config1 config) {
        this.slaveGame1Fragment = slaveGame1Fragment;
        this.config = config;

//        (new Timer(true)).schedule(new TimerTask() {
//            @Override
//            public void run() {
//                bus.post(EventFactory.newTile(slave1.getTile()));
//            }
//        },2000,2000);
    }


    @Subscribe
    public void onNewTileEvent(NewTileEvent event) {
        slaveGame1Fragment.getConveyorUp().addTile(event.getTile());
        slaveGame1Fragment.getConveyorDown().addTile(event.getTile());
    }
}
