package it.playfellas.superapp.logic.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;
import it.playfellas.superapp.logic.tiles.TileDirection;
import it.playfellas.superapp.logic.tiles.TileShape;
import it.playfellas.superapp.logic.tiles.TileType;

/**
 * Created by Stefano Cappa on 04/08/15.
 */
public class DbFiller extends DbAccess {

    public static final String DIR_NAME = "tiles";
    public static final String EXTENSION = ".png";

    private List<Tile> tiles;

    /**
     * Class to fill the db.
     *
     * @param context The Activity context.
     */
    public DbFiller(Context context) {
        super(context);
        tiles = new ArrayList<>();

        this.createTiles();
    }

    /**
     * Method to fill the db with the tiles
     * @throws DbException An exception during add operations.
     */
    public void fill() throws DbException {
        for (Tile tile : tiles) {
            super.add(InternalConfig.TABLE_NAME, toTileEntity(tile));
        }
    }

    private void createTiles() {
        //String url, TileColor color, TileDirection direction, TileShape shape, TileType type, int size)<
        tiles.add(new Tile(getPath("anguria"), TileColor.GREEN, TileDirection.NONE, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("auto"), TileColor.RED,TileDirection.LEFT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("auto_intruso"), TileColor.YELLOW,TileDirection.LEFT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("bottone"), TileColor.BLUE , TileDirection.NONE, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("dado"),TileColor.WHITE, TileDirection.NONE, TileShape.CUBE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("fish"), TileColor.RED, TileDirection.RIGHT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("fish_intruso"), TileColor.YELLOW, TileDirection.RIGHT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("leone"), TileColor.NONE, TileDirection.NONE, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("pallino_intruso"), TileColor.LIGHT_BLUE, TileDirection.NONE, TileShape.CIRCLE ,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("quadrato"),TileColor.GREEN, TileDirection.NONE, TileShape.SQUARE ,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("quadrifoglio"),TileColor.GREEN, TileDirection.NONE, TileShape.NONE ,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("stella"),TileColor.YELLOW, TileDirection.NONE, TileShape.NONE ,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tartaruga"),TileColor.GREEN, TileDirection.UP, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tasto_avanti"),TileColor.ORANGE, TileDirection.RIGHT, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tasto_esci"),TileColor.ORANGE, TileDirection.RIGHT, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tasto_intruso_dinamico"),TileColor.ORANGE, TileDirection.NONE, TileShape.SQUARE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tasto_intruso_statico"),TileColor.ORANGE, TileDirection.NONE, TileShape.SQUARE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tasto_rigioca"),TileColor.ORANGE, TileDirection.NONE, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("tigre"),TileColor.NONE, TileDirection.NONE, TileShape.NONE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("triangolo_1"),TileColor.ORANGE, TileDirection.UP, TileShape.TRIANGLE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("triangolo_2"),TileColor.RED, TileDirection.UP, TileShape.TRIANGLE,TileType.ABSTRACT,1));
        tiles.add(new Tile(getPath("trifoglio"),TileColor.GREEN, TileDirection.NONE, TileShape.NONE,TileType.ABSTRACT,1));
    }

    private String getPath(String fileName) {
        return DIR_NAME + "/" + fileName + EXTENSION;
    }

    private TileEntity toTileEntity (Tile t) {
        TileEntity te = new TileEntity();
        te.setUrl(t.getUrl());
        te.setColor(t.getColor().toString());
        te.setShape(t.getShape().toString());
        te.setDirection(t.getDirection().toString());
        te.setSize(t.getSize());
        te.setType(t.getType().toString());
        return te;
    }

}
