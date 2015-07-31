package it.playfellas.superapp.logic.tiles;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 27/07/15.
 */
public class Tile {
    @Getter
    private TileColor color;
    @Getter
    private TileDirection direction;
    @Getter
    private TileShape shape;
    @Getter
    @Setter
    private String description;

    public Tile(TileColor color, TileDirection direction, TileShape shape) {
        super();
        this.color = color;
        this.direction = direction;
        this.shape = shape;
        this.description = "";
    }
}
