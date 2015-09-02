package it.playfellas.superapp.logic.slave.game23;

import com.squareup.otto.Subscribe;

import it.playfellas.superapp.events.EventFactory;
import it.playfellas.superapp.events.tile.StackClickEvent;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.network.TenBus;

/**
 * Created by affo on 02/09/15.
 */
public class Slave3Controller extends Slave23Controller {
    private TowerDispenser dispenser;

    public Slave3Controller(TileSelector ts) {
        super();
        this.dispenser = new TowerDispenser(ts);

        TenBus.get().register(this);
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }

    // invoked on stack click
    @Subscribe
    public void onStackClicked(StackClickEvent e) {
        decrementPtr();
        TenBus.get().post(EventFactory.pop());
    }
}
