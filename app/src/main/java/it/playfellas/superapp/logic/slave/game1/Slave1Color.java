package it.playfellas.superapp.logic.slave.game1;

import java.util.Random;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class Slave1Color extends Slave1Controller {
    private ColorIntruderDispenser dispenser;
    private TileColor baseColor;
    private TileSelector ts;

    public Slave1Color(TileSelector ts) {
        super();
        this.ts = ts;
        TileColor[] colors = TileColor.values();
        this.baseColor = colors[(new Random()).nextInt(colors.length)];
        this.dispenser = new ColorIntruderDispenser(ts, baseColor);
    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = t.getColor().equals(baseColor);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected TileDispenser getDispenser() {
        return dispenser;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        return new IntruderDispenserInverter(ts, dispenser);
    }
}
