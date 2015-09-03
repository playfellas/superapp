package it.playfellas.superapp.ui.slave.game3;

import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.YourTurnEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave3Controller;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileSize;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;
import it.playfellas.superapp.ui.slave.TileDisposer;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave3Presenter extends SlavePresenter {

    private SlaveGame3Fragment slaveGame3Fragment;
    private Config3 config;
    private TileSelector db;
    private TileDisposer tileDisposer;
    private Slave3Controller slave3;
    private Tile[] currentStack;

    @Getter
    private int filledTowerSlots = 0;

    public Slave3Presenter(TileSelector db, SlaveGame3Fragment slaveGame3Fragment, Config3 config) {
        TenBus.get().register(this);
        this.slaveGame3Fragment = slaveGame3Fragment;
        this.config = config;
        this.db = db;
        slave3 = new Slave3Controller(db);
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame3Fragment;
    }

    @Override
    public void pause() {
        this.tileDisposer.pause();
        this.slaveGame3Fragment.getConveyorDown().clear();
        this.slaveGame3Fragment.getConveyorDown().stop();
        this.slaveGame3Fragment.getConveyorDown().clear();
    }

    @Override
    public void restart() {
        this.tileDisposer.restart();
        this.slaveGame3Fragment.getConveyorDown().start();
    }

    public void startTileDisposer() {
        slave3.init();
        this.tileDisposer = new TileDisposer(slave3, config) {
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
        if (slaveGame3Fragment.getConveyorDown() != null) {
            slaveGame3Fragment.getConveyorDown().changeSpeed(e.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] tiles = e.getTiles();
        slaveGame3Fragment.updateCompleteTower(tiles);
    }


    @Subscribe
    public void onYourTurnEvent(YourTurnEvent e) {
        slaveGame3Fragment.showEndTurnDialog();
        currentStack = e.getStack();
        if (e.getPlayerAddress().equals(TenBus.get().myBTAddress())) {
            slaveGame3Fragment.hideEndTurnDialog();
            this.restart();
            slaveGame3Fragment.updateSlotsTower(e.getStack());
        } else {
            slaveGame3Fragment.updateDialogSlotsTower(e.getStack());
        }
    }

    @Subscribe
    public void onbClickTileEvent(ClickedTileEvent e) {
        //pausePresenter
        this.pause();
//        slaveGame3Fragment.showEndTurnDialog();
//        slaveGame3Fragment.updateDialogSlotsTower(this.currentStack);
    }

    private void addTileToConveyors(NewTileEvent event) {
        slaveGame3Fragment.getConveyorDown().addTile(event.getTile());
    }

    public TileSize[] getTileSizes() {
        TileSize[] sizes = TileSize.values();
        ArrayUtils.reverse(sizes);
        return sizes;
    }

    public void stackClicked() {
        TenBus.get().post(EventFactory.stackClick());
    }
}
