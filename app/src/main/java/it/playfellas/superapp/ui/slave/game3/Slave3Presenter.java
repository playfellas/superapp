package it.playfellas.superapp.ui.slave.game3;

import android.util.Log;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.game.BeginStageEvent;
import it.playfellas.superapp.events.game.EndGameEvent;
import it.playfellas.superapp.events.game.EndStageEvent;
import it.playfellas.superapp.events.game.RTTUpdateEvent;
import it.playfellas.superapp.events.game.YourTurnEvent;
import it.playfellas.superapp.events.tile.BaseTilesEvent;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.NewTileEvent;
import it.playfellas.superapp.events.tile.NewTutorialTileEvent;
import it.playfellas.superapp.logic.Config3;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game23.Slave3Controller;
import it.playfellas.superapp.network.TenBus;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.ui.slave.DisposingService;
import it.playfellas.superapp.ui.slave.SlaveGameFragment;
import it.playfellas.superapp.ui.slave.SlavePresenter;

/**
 * Created by Stefano Cappa on 30/07/15.
 */
public class Slave3Presenter extends SlavePresenter {
    private static final String TAG = Slave3Presenter.class.getSimpleName();

    private SlaveGame3Fragment slaveGame3Fragment;
    private Config3 config;
    private TileSelector db;
    private Slave3Controller slave3;

    public Slave3Presenter(TileSelector db, SlaveGame3Fragment slaveGame3Fragment, Config3 config) {

        TenBus.get().register(this);

        this.slaveGame3Fragment = slaveGame3Fragment;
        this.config = config;
        this.db = db;
        slave3 = new Slave3Controller(db);
    }

    private void addTileToConveyors(NewTileEvent event) {
        slaveGame3Fragment.getConveyorDown().addTile(event.getTile());
    }

    private void stopConveyors() {
        this.slaveGame3Fragment.getConveyorDown().clear();
        this.slaveGame3Fragment.getConveyorDown().stop();
        this.slaveGame3Fragment.getConveyorDown().clear();
    }

    @Override
    protected void newTileEvent(NewTileEvent event) {
        this.addTileToConveyors(event);
    }

    @Override
    protected void newTileEvent(NewTutorialTileEvent event) {
        slaveGame3Fragment.getConveyorDown().addTile(event.getTile());
    }

    @Override
    protected SlaveGameFragment getSlaveGameFragment() {
        return this.slaveGame3Fragment;
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
        slave3.destroy();

        //stop the tiledisposer and conveyors
        DisposingService.stop();
        this.stopConveyors();
    }

    @Override
    public void restart() {
        DisposingService.start(slave3, config);
        this.stopConveyors();
        this.slaveGame3Fragment.getConveyorDown().start();
    }

    public void initController() {
        slave3.init();
    }

    @Override
    protected void beginStageEvent(BeginStageEvent event) {
        //received a BeginStageEvent.
        //For this reason i must hide the dialog (if currently visible) and restart all presenter's logic
        Log.d(TAG, "------->BeginStageEvent received by the Slave Presenter");
        slaveGame3Fragment.hideWaitingDialog();
        this.restart();
    }

    @Override
    protected void endStageEvent(EndStageEvent event) {
        //received an EndStageEvent.
        //For this reason i must show a dialog and pause all presenter's logic
        Log.d(TAG, "------->EndStageEvent received by the Slave Presenter");
        slaveGame3Fragment.getConveyorUp().clear();
        slaveGame3Fragment.showWaitingDialog();
        this.pause();
    }

    @Override
    protected void endGameEvent(EndGameEvent event) {
        Log.d(TAG, "------->EndGameEvent received by the Slave Presenter");
        slaveGame3Fragment.hideWaitingDialog();
        this.kill();
        slaveGame3Fragment.endGame();
    }

    @Subscribe
    public void onRttEvent(RTTUpdateEvent e) {
        if (slaveGame3Fragment.getConveyorDown() != null) {
            slaveGame3Fragment.getConveyorDown().changeRTT(e.getRtt());
        }
    }

    @Subscribe
    public void onBaseTiles(BaseTilesEvent e) {
        Tile[] tiles = e.getTiles();
        slaveGame3Fragment.updateCompleteStack(tiles);
    }

    @Subscribe
    public void onYourTurnEvent(YourTurnEvent e) {
        slaveGame3Fragment.hideEndTurnDialog();
        if (e.getPlayerAddress().equals(TenBus.get().myBTAddress())) {
            this.restart();
            slaveGame3Fragment.updateSlotsStack(e.getStack());
        } else {
            slaveGame3Fragment.showEndTurnDialog();
            slaveGame3Fragment.updateDialogSlotsStack(e.getStack());
        }
    }

    @Subscribe
    public void onClickTileEvent(ClickedTileEvent e) {
        //pausePresenter
        this.pause();
    }
}
