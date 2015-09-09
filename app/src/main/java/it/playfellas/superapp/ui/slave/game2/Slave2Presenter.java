package it.playfellas.superapp.ui.slave.game2;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.conveyors.MovingConveyor;
import java.util.Random;

import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave2Controller;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;
import it.playfellas.superapp.ui.slave.TileDisposer;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave2Presenter extends SlavePresenter {

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;
    private TileSelector db;
    private TileDisposer tileDisposer;
    private Slave2Controller slave2;

    public Slave2Presenter(TileSelector db, SlaveGame2Fragment slaveGame2Fragment, Config2 config) {
        TenBus.get().register(this);
        this.slaveGame2Fragment = slaveGame2Fragment;
        this.config = config;
        this.db = db;
        slave2 = new Slave2Controller(db);
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame2Fragment;
    }

    @Override
    public void pause() {
        this.tileDisposer.pause();
        this.slaveGame2Fragment.getConveyorDown().clear();
        this.slaveGame2Fragment.getConveyorDown().stop();
        this.slaveGame2Fragment.getConveyorDown().clear();
    }

    @Override
    public void restart() {
        this.tileDisposer.restart();
        this.slaveGame2Fragment.getConveyorDown().start();
    }

    public void startTileDisposer() {
        slave2.init();
        this.tileDisposer = new TileDisposer(slave2, config) {
            @Override
            protected boolean shouldIStayOrShouldISpawn() {
                Random r = new Random();
                if ((r.nextInt(4)) == 3) {
                    return false;       //p=1/4
                } else { //numbers 0,1,2
                    return true;        //p=3/4
                }
            }
        };
        this.tileDisposer.start();
    }

    @Subscribe
    public void onRttEvent(RTTUpdateEvent e) {
        if (slaveGame2Fragment.getConveyorDown() != null) {
            slaveGame2Fragment.getConveyorDown().changeRTT(e.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] tiles = e.getTiles();
        slaveGame2Fragment.showBaseTiles(tiles);
    }

    @Subscribe
    public void onUiRWEvent(UIRWEvent e) {
        //TODO
    }

    private void addTileToConveyors(NewTileEvent event) {
        slaveGame2Fragment.getConveyorDown().addTile(event.getTile());
    }
}
