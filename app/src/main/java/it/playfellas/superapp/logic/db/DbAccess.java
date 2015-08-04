package it.playfellas.superapp.logic.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import it.playfellas.superapp.InternalConfig;
import it.playfellas.superapp.logic.db.query.QueryParam;
import it.playfellas.superapp.logic.tiles.Tile;
import it.playfellas.superapp.logic.tiles.TileColor;
import it.playfellas.superapp.logic.tiles.TileDirection;
import it.playfellas.superapp.logic.tiles.TileShape;
import it.playfellas.superapp.logic.tiles.TileType;


/**
 * TODO documentation
 */
public class DbAccess implements TileSelector {

    private DbAdapter dbHelper;

    public DbAccess(Context context) {
        dbHelper = new DbAdapter(context);
    }

    @Override
    public List<Tile> random(int n, QueryParam query) {
        List<Tile> tiles = new ArrayList<>();
        List<Integer> randomIndexes = new ArrayList<>();

        String whereClause = query.getQuery();
        dbHelper.open();
        Cursor cursor = dbHelper.fetchByQuery(InternalConfig.TABLE_NAME, whereClause);
        for (int i = 0; i < cursor.getCount(); i++) {
            randomIndexes.add(i);
        }
        Collections.shuffle(randomIndexes);
        int maxLength = n > randomIndexes.size() ? randomIndexes.size() : n;
        randomIndexes = randomIndexes.subList(0, maxLength);

        for (Integer pos : randomIndexes) {
            cursor.moveToPosition(pos);
            tiles.add(getTile(cursor));
        }
        dbHelper.close();
        return tiles;
    }

    public List<Tile> getAll(String tableName) {
        List<Tile> tiles = new ArrayList<>();

        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllData(tableName);

        while (cursor.moveToNext()) {
            tiles.add(getTile(cursor));
        }

        dbHelper.close();
        return tiles;
    }

    public void logDb(String tableName) {
        TileEntity tileEntity = new TileEntity();
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllData(tableName);
        while (cursor.moveToNext()) {
            tileEntity.setUrl(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_URL)));
            tileEntity.setColor(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_COLOR)));
            tileEntity.setShape(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_SHAPE)));
            tileEntity.setDirection(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_DIRECTION)));
            tileEntity.setType(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_TYPE)));
            tileEntity.setSize(cursor.getInt(cursor.getColumnIndex(InternalConfig.KEY_SIZE)));
            Log.d("DB:" + InternalConfig.DATABASE_NAME + "_TABLE:" + tableName, tileEntity.toString());
        }
        dbHelper.close();
    }

    private Tile getTile(TileEntity entity) {
        String url = entity.getUrl();
        TileColor c = TileColor.valueOf(entity.getColor());
        TileShape s = TileShape.valueOf(entity.getShape());
        TileDirection d = TileDirection.valueOf(entity.getDirection());
        TileType t = TileType.valueOf(entity.getType());
        int size = entity.getSize();
        return new Tile(url, c, d, s, t, size);
    }

    private Tile getTile(Cursor c) {
        return getTile(getEntity(c));
    }

    private TileEntity getEntity(Cursor cursor) {
        TileEntity tileEntity = new TileEntity();
        tileEntity.setUrl(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_URL)));
        tileEntity.setColor(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_COLOR)));
        tileEntity.setShape(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_SHAPE)));
        tileEntity.setDirection(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_DIRECTION)));
        tileEntity.setType(cursor.getString(cursor.getColumnIndex(InternalConfig.KEY_TYPE)));
        tileEntity.setSize(cursor.getInt(cursor.getColumnIndex(InternalConfig.KEY_SIZE)));
        return tileEntity;
    }
}
