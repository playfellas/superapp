package it.playfellas.superapp.logic.db;

import it.playfellas.superapp.logic.tiles.TileShape;

/**
 * Created by affo on 03/08/15.
 */
public class ShapeParam extends QueryParam {
    private TileShape shape;

    public ShapeParam(TileShape shape) {
        super();
        this.shape = shape;
    }

    public ShapeParam(TileShape shape, boolean not) {
        super(not);
        this.shape = shape;
    }

    @Override
    String getQuery() {
        return null;
    }
}
