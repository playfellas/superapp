package it.playfellas.superapp.logic.slave.game23;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.tile.ClickedTileEvent;
import it.playfellas.superapp.events.tile.StackClickEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 02/09/15.
 */
public class Slave3Controller extends Slave23Controller {
    private TowerDispenser dispenser;
    private boolean wrongPop;

    public Slave3Controller(TileSelector ts) {
        super();
        this.wrongPop = false;
        this.dispenser = new TowerDispenser(ts);

        TenBus.get().register(this);
    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean right = super.isTileRight(t);
        synchronized (this) {
            this.wrongPop = right;
        }
        return right;
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }

    @Subscribe
    public void onConveyorClicked(ClickedTileEvent e) {
        TenBus.get().post(EventFactory.push(e.getTile()));
    }

    @Subscribe
    public synchronized void onStackClicked(StackClickEvent e) {
        if (wrongPop) {
            decrementPtr();
        }
        TenBus.get().post(EventFactory.pop(wrongPop));
    }
}
