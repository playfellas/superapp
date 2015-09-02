package it.playfellas.superapp.logic.slave.game23;

import it.playfellas.superapp.logic.db.TileSelector;

/**
 * Created by affo on 02/09/15.
 */
public class Slave3Controller extends Slave23Controller {
    private TowerDispenser dispenser;

    public Slave3Controller(TileSelector ts) {
        super();
        this.dispenser = new TowerDispenser(ts);
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }
}
