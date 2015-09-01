package it.playfellas.superapp.logic.slave.game2;

import java.util.Random;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.RandomUtils;
import it.playfellas.superapp.logic.db.TileSelector;
import it.playfellas.superapp.logic.db.query.BinaryOperator;
import it.playfellas.superapp.logic.db.query.Conjunction;
import it.playfellas.superapp.logic.db.query.Shape;
import it.playfellas.superapp.logic.db.query.Type;
import it.playfellas.superapp.logic.slave.TileDispenser;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileShape;
import it.playfellas.superapp.logic.tiles.TileType;

/**
 * Created by affo on 07/08/15.
 */
public class SizeDispenser extends TileDispenser {
    private TileSelector ts;
    private Tile[] baseTiles;
    private TileShape baseShape;

    public SizeDispenser(TileSelector ts, Tile[] baseTiles) {
        super();
        this.ts = ts;
        this.baseTiles = baseTiles;
        this.baseShape = baseTiles[0].getShape();
    }

    @Override
    public Tile next() {
        if ((new Random()).nextFloat() < InternalConfig.GAME2_TGT_PROB) {
            return RandomUtils.choice(baseTiles);
        }
        return ts.random(1, new Conjunction(new Shape(BinaryOperator.DIFFERENT, baseShape), new Type(BinaryOperator.EQUALS, TileType.ABSTRACT))).get(0);
    }
}
