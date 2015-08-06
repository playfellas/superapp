package it.playfellas.superapp.logic.db.query;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 03/08/15.
 */
public class Direction extends Atom {
    private TileDirection direction;

    public Direction(BinaryOperator op, TileDirection direction) {
        super(op);
        this.direction = direction;
    }

    @Override
    protected String getColumnName() {
        return InternalConfig.KEY_DIRECTION;
    }

    @Override
    protected String getValue() {
        return quoteValue(this.direction.toString());
    }
}
