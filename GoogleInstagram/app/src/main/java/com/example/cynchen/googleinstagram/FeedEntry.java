package com.example.cynchen.googleinstagram;

import android.provider.BaseColumns;

/**
 * Created by cynchen on 9/13/15.
 */
    /* Inner class that defines the table contents */
public abstract class FeedEntry implements BaseColumns {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String TABLE_NAME = "entry";
    public static final String ITEM_URL = "item";

    //Structure of the SQL entries that are created
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    ITEM_URL + TEXT_TYPE +" )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

}
