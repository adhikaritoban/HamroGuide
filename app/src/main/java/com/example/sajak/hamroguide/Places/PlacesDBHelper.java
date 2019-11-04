package com.example.sajak.hamroguide.Places;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlacesDBHelper extends SQLiteOpenHelper{

    public static final String DATABSE_NAME = "place_table.db";
    public static final String TABLE_NAME="place";
    public static final String CREATE_TABLE_QUERY="create table IF NOT EXISTS place(id TEXT, location TEXT, latitude TEXT, longitude TEXT, place TEXT, image TEXT);";
    public static final String DROP_TABLE_QUERY = "drop table if exists place;";

    public PlacesDBHelper(Context context){
        super(context, DATABSE_NAME,null,1);
        Log.d("Database created","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
        Log.d("Table Created?","Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY);
        Log.d("Table drop","Table drop");
    }

    public void insert_place_data(String id, String location, Double latitude, Double longitude, String place, String image, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("location", location);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("place", place);
        contentValues.put("image", image);
        Log.d("Check", "DB " + place);
        long l = sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Data insert","Inserted");
    }

    public Cursor get_places_data(SQLiteDatabase sqLiteDatabase){
        Log.e("Check", "Get Data to Display");
        Cursor res=sqLiteDatabase.rawQuery("SELECT distinct * FROM place;", null); //retrive all information and return to res
        return res;
    }
}
