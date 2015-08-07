package it.playfellas.superapp.logic.slave.game1;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Direction;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 06/08/15.
 */
public class DirectionIntruderDispenser extends IntruderTileDispenser {
    private static final String TAG = DirectionIntruderDispenser.class.getSimpleName();
    private TileSelector ts;
    private final TileDirection base = Slave1Direction.baseDir;

    public DirectionIntruderDispenser(TileSelector ts) {
        super(ts);
        this.ts = ts;
    }

    @Override
    List<Tile> newTargets(int n) {
        List<Tile> tgts = new ArrayList<>();
        for (Tile t : ts.random(n, new Direction(BinaryOperator.EQUALS, true))){
            tgts.add(t.setDirection(base));
        }
        return tgts;
    }

    @Override
    List<Tile> newCritical(int n, final List<Tile> targets) {
        // pick n random targets and return same targets with
        // randomly swapped direction.
        List<Tile> criticals = new ArrayList<>();

        List<Tile> rndTgts = RandomUtils.choice(targets, n);


        for (Tile t : rndTgts) {
            criticals.add(t.changeDirection());
        }

        return criticals;
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        return ts.random(n, new Direction(BinaryOperator.EQUALS, false));
    }
}
