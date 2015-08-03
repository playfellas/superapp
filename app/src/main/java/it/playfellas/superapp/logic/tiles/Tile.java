package it.playfellas.superapp.logic.tiles;

import lombok.Getter;

/**
 * Created by affo on 27/07/15.
 */
public class Tile {
    @Getter
    private String url;
    @Getter
    private TileColor color;
    @Getter
    private TileDirection direction;
    @Getter
    private TileShape shape;
    @Getter
    private TileType type;

    public Tile(String url, TileColor color, TileDirection direction, TileShape shape, TileType type) {
        super();
        this.url = url;
        this.color = color;
        this.direction = direction;
        this.shape = shape;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "url='" + url + '\'' +
                ", color=" + color +
                ", direction=" + direction +
                ", shape=" + shape +
                ", type=" + type +
                '}';
    }
}
