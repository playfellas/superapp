package it.playfellas.superapp.activities.slave.game3;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave3Presenter {

    private SlaveGame3Fragment slaveGame3Fragment;
    private Config3 config;
    private TileSelector db;

    private TenBus bus = TenBus.get();

    public Slave3Presenter() {
    }

    public void onTakeView(TileSelector db, SlaveGame3Fragment slaveGame3Fragment, Config3 config) {
        this.slaveGame3Fragment = slaveGame3Fragment;
        this.config = config;
        this.db = db;
    }

    public void initController() {
        //TODO create a new controller passing this.db

        //TODO create a new disposer
        //start the disposer
    }


    @Subscribe
    public void onNewTileEvent(NewTileEvent event) {

    }
}