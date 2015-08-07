package it.playfellas.superapp.logic.slave.game1;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 06/08/15.
 */
public class InvertedDirectionDispenser extends IntruderTileDispenser {
    private DirectionIntruderDispenser normal;

    public InvertedDirectionDispenser(TileSelector ts, DirectionIntruderDispenser normal) {
        super(ts);
        this.normal = normal;
    }

    private Tile swapDir(Tile t) {
        TileDirection d;
        switch (t.getDirection()) {
            case UP:
                d = TileDirection.DOWN;
                break;
            case DOWN:
                d = TileDirection.UP;
                break;
            case LEFT:
                d = TileDirection.RIGHT;
                break;
            case RIGHT:
                d = TileDirection.LEFT;
                break;
            default:
                d = TileDirection.LEFT;
        }
        return new Tile(t.getName(), t.getColor(), d, t.getShape(), t.getType());
    }

    @Override
    List<Tile> newTargets(int n) {
        List<Tile> newTgts = new ArrayList<>();
        for (Tile t : normal.getTargets(n)) {
            newTgts.add(swapDir(t));
        }
        return newTgts;
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        return normal.getTargets(n);
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        return normal.getEasy(n, targets);
    }
}
