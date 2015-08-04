package it.playfellas.superapp.activities.slave.game1;

import com.squareup.otto.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import it.playfellas.superapp.activities.slave.TileDisposer;
import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.DbAccess;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game1.Slave1Color;
import it.playfellas.superapp.logic.slave.game1.Slave1Controller;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave1Presenter {

    private SlaveGame1Fragment slaveGame1Fragment;
    private Slave1Controller slave1;
    private Config1 config;
    private TileSelector db;
    private TileDisposer tileDisposer;

    private TenBus bus = TenBus.get();

    public Slave1Presenter() {
    }

    public void onTakeView(TileSelector db, SlaveGame1Fragment slaveGame1Fragment, Config1 config) {
        this.slaveGame1Fragment = slaveGame1Fragment;
        this.config = config;
        this.db = db;
    }

    public void initController() {
        switch (this.config.getRule()) {
            default:
                slave1 = new Slave1Color(this.db);
                break;
            case 2:
                break;
        }

        this.tileDisposer = new TileDisposer(slave1, config) {
            @Override
            protected boolean shouldIStayOrShouldISpawn() {
                return true;
            }
        };

        this.tileDisposer.start();
    }


    @Subscribe
    public void onNewTileEvent(NewTileEvent event) {
        slaveGame1Fragment.getConveyorUp().addTile(event.getTile());
        slaveGame1Fragment.getConveyorDown().addTile(event.getTile());
    }
}
