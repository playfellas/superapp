package it.playfellas.superapp.logic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import it.playfellas.superapp.InternalConfig;

/**
 * Class with queries.
 */
class DbAdapter {
    private Context context;
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public DbAdapter(Context context) {
        this.context = context;
    }

    /**
     * Method to open db.
     *
     * @return A {@link DbAdapter}, i.e. the instance of this class.
     * @throws SQLException that indicates there was an error with SQL parsing or execution.
     */
    public DbAdapter open() throws SQLException {
        dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Method to close db. Remember to close db after every query!!!!!!
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Method to add a new row in the db, specifying every field.
     *
     * @param tableName The table name.
     * @param url       Url's tile.
     * @param color     Color's tile.
     * @param shape     Shape's tile.
     * @param direction Direction's tile.
     * @param type      Type's tile.
     * @param size      Size's tile.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertTupleValues(String tableName, String url, String color, String shape, String direction, String type, int size) {
        ContentValues initialValues = createContentValues(url, color, shape, direction, type, size);
        return database.insertOrThrow(tableName, null, initialValues);
    }

    /**
     * Method to add a new row in the db, specifying the object that represents the entire db's tuple.
     * This cam be useful if you have the {@link TileEntity} already created and you don't want
     * to get all the fields and pass to the
     * {@link #insertTupleValues}.
     *
     * @param tableName The table name.
     * @param dbRow     The {@link TileEntity}.
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertTupleObject(String tableName, TileEntity dbRow) {
        ContentValues initialValues = createContentValuesByObject(dbRow);
        return database.insertOrThrow(tableName, null, initialValues);
    }

    /**
     * Method to get the entire db.
     *
     * @param tableName The table name.
     * @return A Cursor.
     */
    public Cursor fetchAllData(String tableName) {
        return database.query(tableName, InternalConfig.ALL_COLUMNS,
                null, null, null, null, null);
    }

    /**
     * Method to fetch tuple's by a specific whereClause.
     *
     * @param tableName   The table name.
     * @param whereClause Where statement of the query.
     * @return A Cursor.
     */
    public Cursor fetchByQuery(String tableName, String whereClause) {
        Log.d("DbAdapter", whereClause);
        Cursor mCursor = database.query(true, tableName, InternalConfig.ALL_COLUMNS,
                whereClause, null, null, null, null, null);
        return mCursor;
    }


    private ContentValues createContentValues(String url, String color, String shape, String direction, String type, int size) {
        ContentValues values = new ContentValues();
        values.put(InternalConfig.KEY_URL, url);
        values.put(InternalConfig.KEY_COLOR, color);
        values.put(InternalConfig.KEY_SHAPE, shape);
        values.put(InternalConfig.KEY_DIRECTION, direction);
        values.put(InternalConfig.KEY_TYPE, type);
        values.put(InternalConfig.KEY_SIZE, size);
        return values;
    }

    private ContentValues createContentValuesByObject(TileEntity dbRow) {
        return this.createContentValues(dbRow.getUrl(), dbRow.getColor(), dbRow.getShape(),
                dbRow.getDirection(), dbRow.getType(), dbRow.getSize());
    }

}
