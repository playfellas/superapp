package it.playfellas.superapp.activities.slave.game2;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave2Presenter {

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;
    private TileSelector db;

    private TenBus bus = TenBus.get();

    public Slave2Presenter() {
    }

    public void onTakeView(TileSelector db, SlaveGame2Fragment slaveGame2Fragment, Config2 config) {
        this.slaveGame2Fragment = slaveGame2Fragment;
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
