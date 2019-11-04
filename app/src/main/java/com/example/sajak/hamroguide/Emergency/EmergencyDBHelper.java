package com.example.sajak.hamroguide.Emergency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EmergencyDBHelper extends SQLiteOpenHelper{

    public static final String DATABSE_NAME = "emergency_table.db";
    public static final String TABLE_NAME="emergency";
    public static final String CREATE_TABLE_QUERY="create table IF NOT EXISTS emergency(number PRIMARY_KEY TEXT, name TEXT);";
    public static final String DROP_TABLE_QUERY = "drop table if exists emergency;";

    public EmergencyDBHelper(Context context){
        super(context,DATABSE_NAME,null,1);
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

    public void insert_emergency(int number, String name, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put("number",number);
        contentValues.put("name",name);
        long l = sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Data insert","Inserted");
    }

    public Cursor get_emergency_Data(SQLiteDatabase sqLiteDatabase){
        Cursor res=sqLiteDatabase.rawQuery("Select distinct * from emergency", null); //retrive all information and return to res
        return res;
    }
}
