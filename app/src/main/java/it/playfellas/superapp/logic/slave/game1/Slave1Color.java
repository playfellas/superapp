package it.playfellas.superapp.logic.slave.game1;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class Slave1Color extends Slave1Controller {
    private ColorIntruderDispenser normalDispenser;
    private IntruderTileDispenser specialDispenser;
    private TileColor baseColor;
    private TileSelector ts;

    public Slave1Color(TileSelector ts) {
        super();
        this.ts = ts;
        TileColor[] colors = TileColor.values();
        // baseColor should not be NONE...
        int noneIndex = ArrayUtils.indexOf(colors, TileColor.NONE);
        colors = ArrayUtils.remove(colors, noneIndex);
        this.baseColor = colors[(new Random()).nextInt(colors.length)];
        this.normalDispenser = new ColorIntruderDispenser(ts, baseColor);
        this.specialDispenser = new IntruderDispenserInverter(ts, normalDispenser);
        normalDispenser.init();
        specialDispenser.init();
    }

    @Override
    protected boolean isTileRight(Tile t) {
        // right answer is when an intruder has been
        // selected. An intruder is such when it has
        // a color different from base one.
        boolean rw = ! t.getColor().equals(baseColor);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected TileDispenser getDispenser() {
        return normalDispenser;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        return specialDispenser;
    }
}
