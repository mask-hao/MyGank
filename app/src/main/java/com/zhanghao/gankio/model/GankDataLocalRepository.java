package com.zhanghao.gankio.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zhanghao.gankio.R;
import com.zhanghao.gankio.api.GankApi;
import com.zhanghao.gankio.entity.CommonResponse;
import com.zhanghao.gankio.entity.Constant;
import com.zhanghao.gankio.entity.Gank;
import com.zhanghao.gankio.entity.GankContent;
import com.zhanghao.gankio.entity.GankFavs;
import com.zhanghao.gankio.entity.GankItem;
import com.zhanghao.gankio.entity.GankSearchItem;
import com.zhanghao.gankio.entity.MoreEntity;
import com.zhanghao.gankio.entity.User;
import com.zhanghao.gankio.entity.enumconstant.TypeConstant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankDataLocalRepository implements GankDataSource.GankLocalDataSource{

    private static GankDataLocalRepository INSTANCE;
    private SQLiteDatabase DB;

    private GankDataLocalRepository() {
    }

    public static synchronized GankDataLocalRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new GankDataLocalRepository();
        return INSTANCE;
    }


    private void initDB(Context context){
        MyDatabaseHelper helper=new MyDatabaseHelper(context,"gank.db",null,2);
        DB=helper.getWritableDatabase();
    }


    @Override
    public void initLocalBD(Context context) {
        initDB(context);
        if (getMoreData(context).size()==0){
           initMoreValues();
        }
    }

    @Override
    public List<MoreEntity> getMoreData(Context context) {
        String SQL = "SELECT * FROM more ; ";
        List<MoreEntity> list = new ArrayList<>();
        initDB(context);
        Cursor cursor = DB.rawQuery(SQL, null);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String id = cursor.getString(cursor.getColumnIndex("resId"));
                MoreEntity moreEntity = new MoreEntity(type, id);
                list.add(moreEntity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateMoreData(Context context,List<MoreEntity> list) {
        String DEL_SQL="DELETE FROM more ; ";
        initDB(context);
        DB.execSQL(DEL_SQL);
        updateMoreData(list);
    }

    @Override
    public void updateSearchHistory(Context context,String value) {
        String SQL="INSERT OR IGNORE INTO search_history (searchStr) VALUES (  '"+value+"' );";
        initDB(context);
        DB.execSQL(SQL);
    }


    @Override
    public void deleteOneHistory(Context context, String value) {
        String SQL="DELETE FROM search_history WHERE searchStr =  '"+value+"';";
        initDB(context);
        DB.execSQL(SQL);
    }


    @Override
    public void deleteAllHistory(Context context) {
        String SQL="DELETE FROM search_history ;";
        initDB(context);
        DB.execSQL(SQL);
    }

    @Override
    public List<String> getSearchHistory(Context context) {
        List<String> strings=new ArrayList<>();
        String SQL="SELECT * FROM  search_history ; ";
        initDB(context);
        Cursor cursor=DB.rawQuery(SQL,null);
        if (cursor.moveToFirst()){
            do {
                String searchStr=cursor.getString(cursor.getColumnIndex("searchStr"));
                strings.add(searchStr);
            }while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(strings);
        return strings;
    }


    private void initMoreValues(){
        ContentValues contentValues=new ContentValues();
        List<MoreEntity> list=new ArrayList<>();
        list.add(new MoreEntity(TypeConstant.ANDROID));
        list.add(new MoreEntity(TypeConstant.IOS));
        list.add(new MoreEntity(TypeConstant.WEB));
        list.add(new MoreEntity(TypeConstant.PHOTO));
        list.add(new MoreEntity(TypeConstant.EXPANDRES));
        list.add(new MoreEntity(TypeConstant.RECOMMEND));
        list.add(new MoreEntity(TypeConstant.APP));
        list.add(new MoreEntity(TypeConstant.VIDEO));
        for (MoreEntity moreEntity : list) {
            contentValues.put("type",moreEntity.getType());
            contentValues.put("resId",moreEntity.getResId());
            DB.insert("more",null,contentValues);
            contentValues.clear();
        }
    }

    private void updateMoreData(List<MoreEntity> list){
        ContentValues values=new ContentValues();
        for (MoreEntity moreEntity : list) {
            values.put("type",moreEntity.getType());
            values.put("resId",String.valueOf(moreEntity.getResId()));
            DB.insert("more",null,values);
            values.clear();
        }
    }

}
