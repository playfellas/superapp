package it.playfellas.superapp.logic.slave.game1;

import java.util.Random;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class Slave1Color extends Slave1Controller {
    private TileColor baseColor;
    private TileSelector ts;

    public Slave1Color(TileSelector ts) {
        super(ts);
        this.ts = ts;
        TileColor[] colors = TileColor.values();
        this.baseColor = colors[(new Random()).nextInt(colors.length)];
    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = t.getColor().equals(baseColor);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected IntruderTileDispenser getDispenser(TileSelector ts) {
        return new ColorIntruderDispenser(ts, baseColor);
    }

    @Override
    protected IntruderTileDispenser getSpecialDispenser(TileSelector ts, IntruderTileDispenser normalDispenser) {
        return new IntruderDispenserInverter(ts, normalDispenser);
    }
}
