package it.playfellas.superapp.logic.tiles;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by affo on 27/07/15.
 */
public class Tile {
    @Getter
    private String name;
    @Getter
    private TileColor color;
    @Getter
    private TileDirection direction;
    @Getter
    private TileShape shape;
    @Getter
    private TileType type;
    @Getter
    @Setter
    private TileSize size;

    public Tile(String name, TileColor color, TileDirection direction, TileShape shape, TileType type) {
        super();
        this.name = name;
        this.color = color;
        this.direction = direction;
        this.shape = shape;
        this.type = type;
        this.size = TileSize.S;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", direction=" + direction +
                ", shape=" + shape +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
