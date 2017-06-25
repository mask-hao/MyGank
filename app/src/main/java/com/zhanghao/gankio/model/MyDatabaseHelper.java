package com.zhanghao.gankio.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhanghao on 2017/5/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_MORE="CREATE TABLE more ("
            +"id integer primary key autoincrement, "
            +"type text, "
            +"resId text ) ";

    private static final String CREATE_SEARCH_HISTORY="CREATE TABLE search_history ("
            +"searchStr text primary key )";


    private static final String CREATE_TAGS="CREATE TABLE tag ("
            +"id integer primary key ,"
            +"type text )";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MORE);
        db.execSQL(CREATE_SEARCH_HISTORY);
        db.execSQL(CREATE_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion){
//            String SQL = " DELETE FROM more ; ";
//            db.execSQL(SQL);
            db.execSQL(CREATE_TAGS);

        }
    }
}
