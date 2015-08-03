package it.playfellas.superapp.logic.slave;

import java.util.Random;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.game1.ColorIntruderDispenser;
import it.playfellas.superapp.logic.slave.game1.IntruderDispenserInverter;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class Slave1Color extends Slave1Controller {
    private ColorIntruderDispenser dispenser;
    private TileColor baseColor;

    public Slave1Color(TileSelector ts) {
        super();
        TileColor[] colors = TileColor.values();
        this.baseColor = colors[(new Random()).nextInt(colors.length)];
        this.dispenser = new ColorIntruderDispenser(ts, baseColor);
    }

    @Override
    boolean isTileRight(Tile t) {
        boolean rw = t.getColor().equals(baseColor);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    TileDispenser getDispenser() {
        return dispenser;
    }

    @Override
    TileDispenser getSpecialDispenser() {
        return new IntruderDispenserInverter(dispenser);
    }
}
