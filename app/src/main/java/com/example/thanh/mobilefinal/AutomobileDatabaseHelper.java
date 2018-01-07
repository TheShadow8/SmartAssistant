package com.example.thanh.mobilefinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thanh on 2017-12-23.
 */

public class AutomobileDatabaseHelper extends SQLiteOpenHelper {

    public static final String GAS_DETAILS_TABLE = "GAS_DETAILS_TABLE";
    public static final String KEY_ID = "_id";
    public static final String KEY_PRICE = "price";
    public static final String KEY_LITRES = "litres";
    public static final String KEY_KILOMETERS = "kilometers";
    public static final String KEY_DATE = "date";
    public static final String CREATE_TABLE_SQL
            = "CREATE TABLE " + GAS_DETAILS_TABLE + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_PRICE + " REAL, "
            + KEY_LITRES + " REAL, "
            + KEY_KILOMETERS + " REAL, "
            + KEY_DATE + " INTEGER"
            + " );";

    public static final String DROP_GAS_TABLE_SQL = "DROP TABLE IF EXISTS " + GAS_DETAILS_TABLE;
    public static final String SELECT_ALL_SQL
            = String.format("SELECT * FROM %s ORDER BY %s", GAS_DETAILS_TABLE, KEY_DATE);
    static final String DATABASE_NAME = "AutomobileDatabase.db";

    private static final int DATABASE_VERSION_NUM = 2;
    AutomobileDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION_NUM);
    }

    public AutomobileDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_GAS_TABLE_SQL);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_GAS_TABLE_SQL);
        onCreate(db);
    }

}
