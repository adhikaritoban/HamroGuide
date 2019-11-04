package com.example.sajak.hamroguide.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsDBHelper extends SQLiteOpenHelper {

    public static final String DATABSE_NAME = "news_table.db";
    public static final String TABLE_NAME="news";
    public static final String CREATE_TABLE_QUERY="create table IF NOT EXISTS news(news_id PRIMARY_KEY TEXT, head TEXT, " +
            "news_content TEXT, news_date TEXT, writer TEXT, news_image TEXT, website TEXT, images TEXT, imaget TEXT);";
    public static final String DROP_TABLE_QUERY = "drop table if exists hospital;";

    public NewsDBHelper(Context context) {
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

    public void insert_news_data(String news_id, String head, String news_content, String news_date, String writer, String news_image, String website, String images, String imaget, SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put("news_id",news_id);
        contentValues.put("head",head);
        contentValues.put("news_content",news_content);
        contentValues.put("news_date",news_date);
        contentValues.put("writer",writer);
        contentValues.put("news_image",news_image);
        contentValues.put("website",website);
        contentValues.put("images",images);
        contentValues.put("imaget",imaget);
        long l = sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Data insert","Inserted");
    }

    public Cursor get_news_data(SQLiteDatabase sqLiteDatabase){
        Cursor res=sqLiteDatabase.rawQuery("SELECT distinct  * FROM   news ORDER BY   news_date ASC;", null); //retrive all information and return to res
        return res;
    }

}