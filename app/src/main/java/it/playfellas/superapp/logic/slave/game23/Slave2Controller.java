package it.playfellas.superapp.logic.slave.game23;

import it.playfellas.superapp.logic.db.TileSelector;

/**
 * Created by affo on 07/08/15.
 */
public class Slave2Controller extends Slave23Controller {
    private SizeDispenser dispenser;

    public Slave2Controller(TileSelector ts) {
        super();
        this.dispenser = new SizeDispenser(ts);
    }

    @Override
    protected TileDispenserWBaseTiles getDispenser() {
        return dispenser;
    }
}
