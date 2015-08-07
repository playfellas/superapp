package it.playfellas.superapp.logic.slave.game1;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Direction;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileDirection;
import it.playfellas.superapp.logic.tiles.TileType;

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

    private Tile swap(Tile t) {
        TileDirection direction = t.getDirection();
        TileType type = t.getType();

        if (direction == TileDirection.NONE) {
            Log.d(TAG, "Cannot swap tile with no direction");
            return t;
        }

        TileDirection swapped;

        switch (type) {
            case CONCRETE:
                // concrete tiles can only be swapped LEFT/RIGHT
                switch (direction) {
                    case LEFT:
                        swapped = TileDirection.RIGHT;
                        break;
                    case RIGHT:
                        swapped = TileDirection.LEFT;
                        break;
                    default:
                        swapped = direction;
                        Log.e(TAG, "Concrete tile with wrong direction set: " + direction);
                }
                break;
            default:
                TileDirection[] dirs = TileDirection.values();
                dirs = ArrayUtils.remove(dirs, ArrayUtils.indexOf(dirs, TileDirection.NONE));
                dirs = ArrayUtils.remove(dirs, ArrayUtils.indexOf(dirs, direction));
                swapped = RandomUtils.choice(Arrays.asList(dirs));
        }

        return new Tile(t.getName(), t.getColor(), swapped, t.getShape(), t.getType());
    }

    @Override
    List<Tile> newTargets(int n) {
        return ts.random(n, new Direction(BinaryOperator.EQUALS, base));
    }

    @Override
    List<Tile> newCritical(int n, final List<Tile> targets) {
        // pick n random targets and return same targets with
        // randomly swapped direction.
        List<Tile> criticals = new ArrayList<>();

        List<Tile> rndTgts = RandomUtils.choice(targets, n);


        for (Tile t : rndTgts) {
            criticals.add(swap(t));
        }

        return criticals;
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        return ts.random(n, new Direction(BinaryOperator.EQUALS, TileDirection.NONE));
    }
}
