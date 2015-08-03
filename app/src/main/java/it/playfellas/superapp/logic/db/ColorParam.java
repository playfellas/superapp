package it.playfellas.superapp.logic.db;

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
    String getQuery() {
        return null;
    }
}
