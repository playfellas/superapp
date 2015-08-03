package it.playfellas.superapp.logic.db;

import it.playfellas.superapp.logic.tiles.TileDirection;

/**
 * Created by affo on 03/08/15.
 */
public class DirectionParam extends QueryParam {
    private TileDirection direction;

    public DirectionParam(TileDirection direction){
        super();
        this.direction = direction;
    }

    public DirectionParam(TileDirection direction, boolean not) {
        super(not);
        this.direction = direction;
    }

    @Override
    String getQuery() {
        return null;
    }
}
