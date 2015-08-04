package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

import it.playfellas.superapp.InternalConfig;
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
    public String getQuery() {
        String operator = isNot() ? "!=" : "==";
        return StringUtils.join(new String[]{InternalConfig.KEY_SHAPE, operator, this.shape.toString()}, " ");
    }
}
