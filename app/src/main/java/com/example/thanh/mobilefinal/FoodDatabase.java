package com.example.thanh.mobilefinal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FoodDatabase  extends SQLiteOpenHelper {
    public static String DB_food_Name = "f_db";
    public static int DB_version = 1;
    public static final String table = "f_Table";
    public static final String RowID = "_id";
    public final static String food_TYPE = "type";
    public final static String DATE = "date";
    public final static String TIME = "time";
    public final static String Calories = "Calories";
    public final static String Fat = "Total_Fat";
    public final static String Carbohydrate = "Carbohydrate";

    public SQLiteDatabase f_database;

    private Context context;

    public static final String ACTIVITY_NAME = "f_DatabaseHelper";

    public FoodDatabase(Context ctx) {
        super(ctx, DB_food_Name, null, DB_version);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MSG = "CREATE TABLE " + table + "("
                + RowID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + food_TYPE + " TEXT, "
                + DATE + " NUMERIC, "
                + TIME + " TEXT, "
                + Calories + " TEXT, "
                + Fat + " TEXT, "
                + Carbohydrate + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_MSG);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        //  super(Context ctx ,String "Messages.db" , SQLiteDatabase.CursorFactory factory,int version);
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db);
    }

    public void setWritable() {
        f_database = getWritableDatabase();
    }

    public void closeDatabase() {
        if (f_database != null && f_database.isOpen()) {
            f_database.close();
        }
    }

    public void insert(String type, String date, String time, String calories, String total_Fat, String carbohydrate) {
        f_database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(food_TYPE, type);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(Calories, calories);
        values.put(Fat, total_Fat);
        values.put(Carbohydrate, carbohydrate);

        f_database.insert(table, null, values);
    }

    public void delete(Long id) {
        f_database = getWritableDatabase();
        f_database.execSQL("DELETE FROM " + table + " WHERE " + RowID + " = " + id);
    }


    public void update(Long id, String type, String date, String time, String calories, String total_Fat, String carbohydrate) {
        f_database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(food_TYPE, type);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(Calories, calories);
        values.put(Fat, total_Fat);
        values.put(Carbohydrate, carbohydrate);

        f_database.update(table, values, RowID + " = " + id, null);
    }

    Cursor c;
    String totalday;
    String sum;
    String day;
    String yesterday;
    double y;
    int d;
    double s;
    double t;
    double AVG;

    public double getSUM() {

        f_database = getWritableDatabase();

        c = f_database.rawQuery(" select SUM ( Calories ) from  f_Table;", null);
        c.moveToFirst();

        sum = c.getString(0);

        f_database = getWritableDatabase();
        c = f_database.rawQuery("SELECT COUNT (DISTINCT " + DATE + ") FROM " + table, null);
        c.moveToFirst();

        totalday = c.getString(0);


        s = Double.parseDouble(sum);
        t = Double.parseDouble(totalday);

        AVG = s / t;

        return AVG;

    }

    public int getday() {
        f_database = getWritableDatabase();
        c = f_database.rawQuery("SELECT COUNT (DISTINCT " + DATE + ") FROM " + table, null);
        c.moveToFirst();
        day = c.getString(0);

        d = Integer.parseInt(day);


        return d;

    }


    public String getYesterday() {
        f_database = getWritableDatabase();



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date()); //date of today


        c = f_database.rawQuery("SELECT  SUM(" + Calories + ") FROM " + table + " WHERE " + DATE + " = DATE('today' , '-1 day')", null);
        c.moveToFirst();

        yesterday = c.getString(0);

        return yesterday;
    }



    public Cursor getCursor() {
        return f_database.query(table, null, null, null, null, null, null);
    }

}


