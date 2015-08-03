package it.playfellas.superapp.logic.slave.game1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.playfellas.superapp.logic.db.ColorParam;
import it.playfellas.superapp.logic.db.Conjunction;
import it.playfellas.superapp.logic.db.Disjunction;
import it.playfellas.superapp.logic.db.QueryParam;
import it.playfellas.superapp.logic.db.ShapeParam;
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
        super();
        this.ts = ts;
        this.baseColor = baseColor;
        TileColor[] cs = TileColor.values();
    }

    @Override
    List<Tile> getTargets(int n) {
        return ts.random(n, new Conjunction(new ColorParam(baseColor)));
    }

    @Override
    List<Tile> getCritical(int n, List<Tile> targets) {
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

        return ts.random(n, new Conjunction(new ColorParam(baseColor, true), new Disjunction(params)));
    }

    @Override
    List<Tile> getEasy(int n, List<Tile> targets) {
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

        return ts.random(n, new Conjunction(new ColorParam(baseColor, true), new Conjunction(params)));
    }
}
