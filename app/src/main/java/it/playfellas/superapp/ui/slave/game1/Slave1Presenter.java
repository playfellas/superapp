package it.playfellas.superapp.ui.slave.game1;

import com.squareup.otto.Subscribe;

import java.util.Random;

import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.ToggleGameModeEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config1;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game1.Slave1Color;
import it.playfellas.superapp.logic.slave.game1.Slave1ColorAgain;
import it.playfellas.superapp.logic.slave.game1.Slave1Controller;
import it.playfellas.superapp.logic.slave.game1.Slave1Direction;
import it.playfellas.superapp.logic.slave.game1.Slave1Shape;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.slave.TileDisposer;

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
        bus.register(this);
    }

    public void onTakeView(TileSelector db, SlaveGame1Fragment slaveGame1Fragment, Config1 config) {
        this.slaveGame1Fragment = slaveGame1Fragment;
        this.config = config;
        this.db = db;
    }

    public void initController() {
        switch (this.config.getRule()) {
            default:
            case 0:
                slave1 = new Slave1Color(this.db);
                slave1.init();
                break;
            case 1:
                slave1 = new Slave1ColorAgain(this.db);
                slave1.init();
                break;
            case 2:
                slave1 = new Slave1Direction(this.db);
                slave1.init();
                break;
            case 3:
                slave1 = new Slave1Shape(this.db);
                slave1.init();
                break;
        }

        this.tileDisposer = new TileDisposer(slave1, config) {
            @Override
            protected boolean shouldIStayOrShouldISpawn() {
                //TODO implement real tiledisposer
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
    public void onNewTileEvent(NewTileEvent event) {
        if (config.getRule() == 0) {
            this.addTileToConveyors(event);
        } else {
            this.addTileToDownConveyor(event);
        }
    }

    @Subscribe
    public void onRttEvent(RTTUpdateEvent e) {
        slaveGame1Fragment.getConveyorUp().changeSpeed(e.getRtt());
        slaveGame1Fragment.getConveyorDown().changeSpeed(e.getRtt());
    }

    @Subscribe
    public void onToggleGameMode(ToggleGameModeEvent e) {
        slaveGame1Fragment.notifyMessage("Il gioco si è invertito");
    }

    private void addTileToConveyors(NewTileEvent event) {
        Random r = new Random();
        if (r.nextBoolean()) {
            slaveGame1Fragment.getConveyorUp().addTile(event.getTile());
        } else {
            slaveGame1Fragment.getConveyorDown().addTile(event.getTile());
        }
    }

    private void addTileToDownConveyor(NewTileEvent event) {
        slaveGame1Fragment.getConveyorDown().addTile(event.getTile());
    }

    @Subscribe
    public void onUIRWEvent(UIRWEvent e) {
        slaveGame1Fragment.onRightOrWrong(e);
    }
}
