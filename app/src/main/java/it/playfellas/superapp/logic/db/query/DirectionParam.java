package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 03/08/15.
 */
public class DirectionParam extends QueryParam {
    private TileDirection direction;

    public DirectionParam(TileDirection direction) {
        super();
        this.direction = direction;
    }

    public DirectionParam(TileDirection direction, boolean not) {
        super(not);
        this.direction = direction;
    }

    @Override
    public String getQuery() {
        String operator = isNot() ? "!=" : "==";
        return StringUtils.join(new String[]{InternalConfig.KEY_DIRECTION, operator, this.direction.toString()}, " ");
    }
}
