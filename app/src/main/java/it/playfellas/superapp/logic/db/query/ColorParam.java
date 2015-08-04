package it.playfellas.superapp.logic.db.query;

import org.apache.commons.lang3.StringUtils;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.tiles.TileColor;

/**
 * Created by affo on 03/08/15.
 */
public class ColorParam extends QueryParam {
    private TileColor color;

    public ColorParam(TileColor color) {
        super();
        this.color = color;
    }

    public ColorParam(TileColor color, boolean not) {
        super(not);
        this.color = color;
    }

    @Override
    public String getQuery() {
        String operator = isNot() ? "!=" : "==";
        return StringUtils.join(new String[]{InternalConfig.KEY_COLOR, operator, this.color.toString()}, " ");
    }
}
