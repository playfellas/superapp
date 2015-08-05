package it.playfellas.superapp.logic.db;

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
public class DbFiller {

    private DbAccess dbAccess;

    /**
     * Class to fill the db.
     *
     * @param dbAccess The DbAccess
     */
    public DbFiller(DbAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    /**
     * Method to fill the db with the drawable.tiles
     * @throws DbException An exception during add operations.
     */
    public void fill() throws DbException {

        List<Tile> tiles = this.createTiles();

        for (Tile tile : tiles) {
            dbAccess.add(InternalConfig.TABLE_NAME, tile);
        }
    }

    private List<Tile> createTiles() {
        List<Tile> tiles = new ArrayList<>();
        //String url, TileColor color, TileDirection direction, TileShape shape, TileType type, int size)
        tiles.add(new Tile("anguria", TileColor.GREEN, TileDirection.NONE, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile("auto", TileColor.RED,TileDirection.LEFT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile("auto_intruso", TileColor.YELLOW,TileDirection.LEFT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile("bottone", TileColor.BLUE , TileDirection.NONE, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile("dado",TileColor.WHITE, TileDirection.NONE, TileShape.CUBE, TileType.ABSTRACT,1));
        tiles.add(new Tile("fish", TileColor.RED, TileDirection.RIGHT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile("fish_intruso", TileColor.YELLOW, TileDirection.RIGHT, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile("leone", TileColor.NONE, TileDirection.NONE, TileShape.NONE, TileType.ABSTRACT,1));
        tiles.add(new Tile("pallino_intruso", TileColor.LIGHT_BLUE, TileDirection.NONE, TileShape.CIRCLE ,TileType.ABSTRACT,1));
        tiles.add(new Tile("quadrato",TileColor.GREEN, TileDirection.NONE, TileShape.SQUARE ,TileType.ABSTRACT,1));
        tiles.add(new Tile("quadrifoglio",TileColor.GREEN, TileDirection.NONE, TileShape.NONE ,TileType.ABSTRACT,1));
        tiles.add(new Tile("stella",TileColor.YELLOW, TileDirection.NONE, TileShape.NONE ,TileType.ABSTRACT,1));
        tiles.add(new Tile("tartaruga",TileColor.GREEN, TileDirection.UP, TileShape.CIRCLE, TileType.ABSTRACT,1));
        tiles.add(new Tile("tasto_avanti",TileColor.ORANGE, TileDirection.RIGHT, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile("tasto_esci",TileColor.ORANGE, TileDirection.RIGHT, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile("tasto_intruso_dinamico",TileColor.ORANGE, TileDirection.NONE, TileShape.SQUARE,TileType.ABSTRACT,1));
        tiles.add(new Tile("tasto_intruso_statico",TileColor.ORANGE, TileDirection.NONE, TileShape.SQUARE,TileType.ABSTRACT,1));
        tiles.add(new Tile("tasto_rigioca",TileColor.ORANGE, TileDirection.NONE, TileShape.CIRCLE,TileType.ABSTRACT,1));
        tiles.add(new Tile("tigre",TileColor.NONE, TileDirection.NONE, TileShape.NONE,TileType.ABSTRACT,1));
        tiles.add(new Tile("triangolo_1",TileColor.ORANGE, TileDirection.UP, TileShape.TRIANGLE,TileType.ABSTRACT,1));
        tiles.add(new Tile("triangolo_2",TileColor.RED, TileDirection.UP, TileShape.TRIANGLE,TileType.ABSTRACT,1));
        tiles.add(new Tile("trifoglio",TileColor.GREEN, TileDirection.NONE, TileShape.NONE,TileType.ABSTRACT,1));
        return tiles;
    }
}
