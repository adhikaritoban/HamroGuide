package com.example.sajak.hamroguide.Agencies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class AgenciesDBHolder extends SQLiteOpenHelper{

    public static final String DATABSE_NAME = "agencies_table.db";
    public static final String TABLE_NAME="agencies";
    public static final String CREATE_TABLE_QUERY="create table IF NOT EXISTS agencies(id TEXT, name TEXT, location TEXT, latitude TEXT, longitude TEXT, image TEXT, contact TEXT);";
    public static final String DROP_TABLE_QUERY = "drop table if exists agencies;";
    Context context;

    AgenciesDBHolder(Context context){
        super(context,DATABSE_NAME,null,1);
        this.context = context;
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

    public void insert_agencies(String id, String name, String location, String latitude, String longitude, String image, String contact, SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("location",location);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);
        contentValues.put("image",image);
        contentValues.put("contact", contact);
        sqLiteDatabase = new AgenciesDBHolder(context).getReadableDatabase();
        long l = sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Data insert","Inserted");

    }

    public Cursor get_agencies_data(SQLiteDatabase sqLiteDatabase){
        Cursor res=sqLiteDatabase.rawQuery("Select distinct * from agencies", null); //retrive all information and return to res
        return res;
    }
}
