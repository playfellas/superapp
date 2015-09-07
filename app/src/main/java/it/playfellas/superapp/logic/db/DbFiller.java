package it.playfellas.superapp.logic.db;

import java.util.ArrayList;
import java.util.List;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.tiles.Tile;
import it.playfellas.superapp.tiles.TileColor;
import it.playfellas.superapp.tiles.TileShape;
import it.playfellas.superapp.tiles.TileType;

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
     *
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
        //String drawable_id, TileColor color, boolean directionable, TileShape shape, TileType type
        tiles.add(new Tile("anguria", TileColor.GREEN, false, TileShape.CIRCLE, TileType.CONCRETE));
        tiles.add(new Tile("auto", TileColor.RED, true, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("auto_intruso", TileColor.YELLOW, true, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("bottone", TileColor.BLUE, false, TileShape.CIRCLE, TileType.CONCRETE));
        tiles.add(new Tile("dado", TileColor.WHITE, false, TileShape.CUBE, TileType.CONCRETE));
        tiles.add(new Tile("dado", TileColor.WHITE, false, TileShape.CUBE, TileType.ABSTRACT));
        tiles.add(new Tile("fish", TileColor.RED, true, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("fish", TileColor.RED, true, TileShape.NONE, TileType.ABSTRACT));
        tiles.add(new Tile("fish_intruso", TileColor.YELLOW, true, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("leone", TileColor.NONE, false, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("pallino_intruso", TileColor.LIGHT_BLUE, false, TileShape.CIRCLE, TileType.ABSTRACT));
        tiles.add(new Tile("quadrato", TileColor.GREEN, false, TileShape.SQUARE, TileType.ABSTRACT));
        tiles.add(new Tile("quadrifoglio", TileColor.GREEN, false, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("stella", TileColor.YELLOW, false, TileShape.NONE, TileType.ABSTRACT));
        tiles.add(new Tile("tartaruga", TileColor.GREEN, true, TileShape.CIRCLE, TileType.CONCRETE));
        tiles.add(new Tile("tasto_avanti", TileColor.ORANGE, true, TileShape.CIRCLE, TileType.ABSTRACT));
        tiles.add(new Tile("tasto_esci", TileColor.ORANGE, true, TileShape.CIRCLE, TileType.ABSTRACT));
        tiles.add(new Tile("tasto_intruso_dinamico", TileColor.ORANGE, false, TileShape.SQUARE, TileType.ABSTRACT));
        tiles.add(new Tile("tasto_intruso_statico", TileColor.ORANGE, false, TileShape.SQUARE, TileType.ABSTRACT));
        tiles.add(new Tile("tasto_rigioca", TileColor.ORANGE, false, TileShape.CIRCLE, TileType.ABSTRACT));
        tiles.add(new Tile("tigre", TileColor.NONE, false, TileShape.NONE, TileType.CONCRETE));
        tiles.add(new Tile("triangolo_1", TileColor.ORANGE, true, TileShape.TRIANGLE, TileType.ABSTRACT));
        tiles.add(new Tile("triangolo_2", TileColor.RED, true, TileShape.TRIANGLE, TileType.ABSTRACT));
        tiles.add(new Tile("trifoglio", TileColor.GREEN, false, TileShape.NONE, TileType.CONCRETE));
        return tiles;
    }
}
