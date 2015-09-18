package it.playfellas.superapp.ui.slave.game2;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.events.ui.UIRWEvent;
import it.playfellas.superapp.logic.Config2;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave2Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.ui.slave.DisposingService;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave2Presenter extends SlavePresenter {
    private static final String TAG = Slave2Presenter.class.getSimpleName();

    private SlaveGame2Fragment slaveGame2Fragment;
    private Config2 config;
    private TileSelector db;
    private Slave2Controller slave2;

    public Slave2Presenter(TileSelector db, SlaveGame2Fragment slaveGame2Fragment, Config2 config) {

        TenBus.get().register(this);

        this.slaveGame2Fragment = slaveGame2Fragment;
        this.config = config;
        this.db = db;
        slave2 = new Slave2Controller(db);
    }

    private void stopConveyors() {
        this.slaveGame2Fragment.getConveyorDown().clear();
        this.slaveGame2Fragment.getConveyorDown().stop();
        this.slaveGame2Fragment.getConveyorDown().clear();
    }

    public void initController() {
        slave2.init();
    }

    private void addTileToConveyors(NewTileEvent event) {
        slaveGame2Fragment.getConveyorDown().addTile(event.getTile());
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    @Override
    protected void newTileEvent(NewTutorialTileEvent event) {
        slaveGame2Fragment.getConveyorDown().addTile(event.getTile());
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame2Fragment;
    }

    @Override
    public void pause() {
        DisposingService.stop();
        this.stopConveyors();
    }

    /**
     * Method to kill the presenter
     */
    @Override
    public void kill() {
        super.kill();

        //unregister tenbus here and also into the superclass
        TenBus.get().unregister(this);

        //unregister also the controller
        slave2.destroy();

        //stop the tiledisposer and conveyors
        DisposingService.stop();
        this.stopConveyors();
    }

    @Override
    public void restart() {
        DisposingService.start(slave2, config);
        this.stopConveyors();
        this.slaveGame2Fragment.getConveyorDown().start();
    }

    @Override
    protected void beginStageEvent(BeginStageEvent event) {
        //received a BeginStageEvent.
        //For this reason i must hide the dialog (if currently visible) and restart all presenter's logic
        Log.d(TAG, "------->BeginStageEvent received by the Slave Presenter");
        slaveGame2Fragment.hideWaitingDialog();
        this.restart();
    }

    @Override
    protected void endStageEvent(EndStageEvent event) {
        //received an EndStageEvent.
        //For this reason i must show a dialog and pause all presenter's logic
        Log.d(TAG, "------->EndStageEvent received by the Slave Presenter");
        slaveGame2Fragment.getConveyorUp().clear();
//        slaveGame2Fragment.showWaitingDialog();
        this.pause();
    }

    @Override
    protected void endGameEvent(EndGameEvent event) {
        Log.d(TAG, "------->EndGameEvent received by the Slave Presenter");
//        slaveGame2Fragment.hideWaitingDialog();
        this.kill();
        slaveGame2Fragment.endGame(event);
    }

    @Subscribe
    public void onRttEvent(RTTUpdateEvent e) {
        if (slaveGame2Fragment.getConveyorDown() != null) {
            (slaveGame2Fragment.getConveyorDown()).changeRTT(e.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] tiles = e.getTiles();
        slaveGame2Fragment.showBaseTiles(tiles);
    }

    @Subscribe
    public void onUiRWEvent(UIRWEvent e) {
        if (e.isRight()) {
            slaveGame2Fragment.getConveyorUp().correctTile();
        }
    }
}
