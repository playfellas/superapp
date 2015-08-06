package it.playfellas.superapp.logic.slave.game1;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 06/08/15.
 */
public class Slave1Direction extends Slave1Controller {
    private TileSelector ts;
    private DirectionIntruderDispenser normal;
    private InvertedDirectionDispenser special;
    public static final TileDirection baseDir = TileDirection.RIGHT;

    public Slave1Direction(TileSelector ts) {
        super();
        this.ts = ts;
        this.normal = new DirectionIntruderDispenser(ts);
        this.special = new InvertedDirectionDispenser(ts, normal);
        this.normal.init();
        this.special.init();
    }

    @Override
    protected boolean isTileRight(Tile t) {
        boolean rw = !t.getDirection().equals(baseDir);
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
