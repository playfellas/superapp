package it.playfellas.superapp.ui.slave.game2;

import com.squareup.otto.Subscribe;

import java.util.Random;

import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game2.Slave2Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;
import it.playfellas.superapp.ui.slave.TileDisposer;

/**
 * Created by Stefano Cappa on 12/08/15.
 */
public class Slave2Presenter extends SlavePresenter {

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;
    private TileSelector db;
    private TileDisposer tileDisposer;

    private TenBus bus = TenBus.get();

    public Slave2Presenter(TileSelector db, SlaveGame2Fragment slaveGame2Fragment, Config2 config) {
        bus.register(this);
        this.slaveGame2Fragment = slaveGame2Fragment;
        this.config = config;
        this.db = db;
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    public void initController() {
//        Slave2Controller slave2 = new Slave2Controller(this.db);
//        this.startTileDisposer(slave2);
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame2Fragment;
    }

    public void startTileDisposer(Slave2Controller slave2) {
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
        slaveGame2Fragment.getConveyorDown().changeSpeed(e.getRtt());
    }

    @Subscribe
    public void onToggleGameMode(ToggleGameModeEvent e) {
        slaveGame2Fragment.notifyMessage("Il gioco si è invertito");
    }

    private void addTileToConveyors(NewTileEvent event) {
        slaveGame2Fragment.getConveyorDown().addTile(event.getTile());
    }
}
