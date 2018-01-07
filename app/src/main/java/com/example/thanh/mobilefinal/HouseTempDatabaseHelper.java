package com.example.thanh.mobilefinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by thanh on 2017-12-29.
 */

public class HouseTempDatabaseHelper extends SQLiteOpenHelper {
    public static String ACTIVITY_NAME = "HouseTempDatabaseHelper";
    public static final String DATABASE_NAME = "temps.db";
    private static final int VERSION_NUM = 8;
    private static final String TAG = "HouseTempDatabaseHelper";
    public static final String KEY_ID = "_id";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_DAY = "day";
    public static final String KEY_TIME = "time";
    public static final String TABLE_NAME = "temperature_time";
    public static final String KEY_DATA = "key_data";

    private HouseTempDatabaseHelper houseTempDatabaseHelper;
    private SQLiteDatabase db;

    public HouseTempDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");

        db.execSQL("Create table " + TABLE_NAME +
                "( "
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_DAY + " text,  "
                + KEY_TIME + " text, "
                + KEY_TEMPERATURE + " text, "
                + KEY_DATA + " text not null );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVision, int newVersion) {

        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVision + " newVersion=" + newVersion);
        Log.i(TAG, "Updating database from version " + oldVision + " to " + newVersion
                + ", while old data will be destroyed. ");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void close() {
        if(houseTempDatabaseHelper != null){
            houseTempDatabaseHelper.close();
        }

    }

    public Cursor getData () {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public boolean insertData (String day, String time, String temp, String sch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DAY,day);
        contentValues.put(KEY_TIME,time);
        contentValues.put(KEY_TEMPERATURE,temp);
        contentValues.put(KEY_DATA,sch);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }


    public Cursor getItemID(String sch){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_NAME +
                " WHERE " + KEY_DATA +  " = '" + sch + "'";

        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getItemDay(String sch){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_DAY + " FROM " + TABLE_NAME +
                " WHERE " + KEY_DATA +  " = '" + sch + "'";

        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getItemTime(String sch){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_TIME + " FROM " + TABLE_NAME +
                " WHERE " + KEY_DATA +  " = '" + sch + "'";

        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getItemTemp(String sch){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KEY_TEMPERATURE + " FROM " + TABLE_NAME +
                " WHERE " + KEY_DATA +  " = '" + sch + "'";

        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public void updateData( String newData, int id, String oldData) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + KEY_DATA +
                " = '" + newData + "' WHERE "
                + KEY_ID + " = '" + id + "'" + " AND "
                + KEY_DATA + " = '" + oldData + "'";
        db.execSQL(query);
    }


}
