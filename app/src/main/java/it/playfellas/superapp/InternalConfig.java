package it.playfellas.superapp;

/**
 * Created by Stefano Cappa on 04/08/15.
 */
public class InternalConfig {

    //TODO fill with all configs

    //************DB************
    public static final String DATABASE_NAME = "superapp.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tiles";

    public static final String KEY_ID = "_id";
    public static final String KEY_URL = "url";
    public static final String KEY_COLOR = "color";
    public static final String KEY_SHAPE = "shape";
    public static final String KEY_DIRECTION = "direction";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SIZE = "size";

    public static final String[] ALL_COLUMNS = new String[]{
            InternalConfig.KEY_URL, InternalConfig.KEY_COLOR, InternalConfig.KEY_SHAPE,
            InternalConfig.KEY_DIRECTION, InternalConfig.KEY_TYPE, InternalConfig.KEY_SIZE};

    //************BT************


    //************??************
}
