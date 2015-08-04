package it.playfellas.superapp.logic.db;

import lombok.Getter;
import lombok.Setter;

public class TileEntity {

    @Getter
    @Setter
    private String url;
    @Getter
    @Setter
    private String color;
    @Getter
    @Setter
    private String shape;
    @Getter
    @Setter
    private String direction;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private int size;

    @Override
    public String toString() {
        return this.url + ", " + this.color + ", " + this.shape + ", " + this.direction + ", " + this.type + ", " + this.size;
    }

}
