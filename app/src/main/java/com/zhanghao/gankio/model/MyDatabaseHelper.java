package com.zhanghao.gankio.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MORE);
        db.execSQL(CREATE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
