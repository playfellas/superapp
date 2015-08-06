package it.playfellas.superapp.logic.slave.game1;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileShape;
import lombok.Getter;

/**
 * Created by affo on 06/08/15.
 */
public class Slave1Shape extends Slave1Controller {
    private ShapeIntruderDispenser normal;
    private IntruderTileDispenser special;
    @Getter
    private TileShape baseShape;

    public Slave1Shape(TileSelector ts) {
        super();
        TileShape[] shapes = TileShape.values();
        // base should not be NONE...
        int noneIndex = ArrayUtils.indexOf(shapes, TileShape.NONE);
        shapes = ArrayUtils.remove(shapes, noneIndex);
        this.baseShape = RandomUtils.choice(Arrays.asList(shapes));
        this.normal = new ShapeIntruderDispenser(ts, baseShape);
        this.special = new IntruderDispenserInverter(ts, normal);
        normal.init();
        special.init();
    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = !t.getShape().equals(baseShape);
        return isNormalMode() ? rw : !rw;
    }

    @Override
    protected TileDispenser getSpecialDispenser() {
        return special;
    }

    @Override
    protected TileDispenser getDispenser() {
        return normal;
    }
}
