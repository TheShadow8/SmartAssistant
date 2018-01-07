package com.example.thanh.mobilefinal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;

import static com.example.thanh.mobilefinal.AutomobileDatabaseHelper.DATABASE_NAME;

/**
 * Created by thanh on 2017-12-27.
 */

public class ExerciseTrackingDatabaseHelper extends SQLiteOpenHelper {

    public static  final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy MMM dd hh:mm");
    public final static String LOGGER_NAME = "ChatDatabaseHelper";
    public final static String DATABASE_NAME = "EXERCISE_DB";
    public final static String TABLE_NAME = "EXERCISE_TABLE";
    public final static int VERSION_NUM = 1;
    public final static String ID = "_id";
    public final static String TYPE = "type";
    public final static String TIME = "time";
    public final static String DURATION = "duration";
    public final static String COMMENT = "comment";

    public ExerciseTrackingDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_MSG = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TYPE + " TEXT, "
                + TIME + " TEXT, "
                + DURATION + " INTEGER, "
                + COMMENT + " TEXT " + ")";
        db.execSQL(CREATE_TABLE_MSG);
        Log.i(LOGGER_NAME, "Table " + TABLE_NAME + " is created.");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(LOGGER_NAME, "Table " + TABLE_NAME + " is upgraded, oldVersion= " + oldVersion
                + " newVersion=" + newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //delete what was there previously
        onCreate(db);
        //  Log.i("ChatDatabaseHelper", "Calling onCreate");
        Log.i(LOGGER_NAME, "Table " + TABLE_NAME + " is downgraded, newVersion=" + newVersion + "oldVersion=" + oldVersion);

    }
}
