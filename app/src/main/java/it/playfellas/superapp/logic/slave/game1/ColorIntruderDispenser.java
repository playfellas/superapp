package it.playfellas.superapp.logic.slave.game1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.playfellas.superapp.logic.db.query.ColorParam;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Disjunction;
import it.playfellas.superapp.logic.db.query.QueryParam;
import it.playfellas.superapp.logic.db.query.ShapeParam;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;
import it.playfellas.superapp.logic.tiles.TileShape;

/**
 * Created by affo on 31/07/15.
 */
public class ColorIntruderDispenser extends IntruderTileDispenser {
    private TileColor baseColor;
    private TileSelector ts;

    public ColorIntruderDispenser(TileSelector ts, TileColor baseColor) {
        super(ts);
        this.ts = ts;
        this.baseColor = baseColor;
    }

    @Override
    List<Tile> newTargets(int n) {
        return ts.random(n, new ColorParam(baseColor));
    }

    @Override
    List<Tile> newCritical(int n, List<Tile> targets) {
        // COLOR | DIRECTION | SHAPE
        // diff  |   any     |  same
        Set<TileShape> shapes = new HashSet<>();
        for (Tile t : targets) {
            shapes.add(t.getShape());
        }

        QueryParam[] params = new QueryParam[shapes.size()];
        int i = 0;
        for (TileShape s : shapes) {
            params[i] = new ShapeParam(s);
            i++;
        }

        QueryParam simpleOne = new ColorParam(baseColor, true);
        List<Tile> res = ts.random(n, new Conjunction(simpleOne, new Disjunction(params)));
        return validate(n, res, simpleOne);
    }

    @Override
    List<Tile> newEasy(int n, List<Tile> targets) {
        // COLOR | DIRECTION | SHAPE
        // diff  |   any     |  diff
        Set<TileShape> shapes = new HashSet<>();
        for (Tile t : targets) {
            shapes.add(t.getShape());
        }

        QueryParam[] params = new QueryParam[shapes.size()];
        int i = 0;
        for (TileShape s : shapes) {
            params[i] = new ShapeParam(s, true);
            i++;
        }

        QueryParam simpleOne = new ColorParam(baseColor, true);
        List<Tile> res = ts.random(n, new Conjunction(simpleOne, new Conjunction(params)));
        return validate(n, res, simpleOne);
    }
}
