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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

/**
 * Created by zhanghao on 2017/4/20.
 */

public class GankDataRepository implements GankDataSource{

    private static GankDataRepository INSTANCE;
    private SQLiteDatabase DB;

    private GankDataRepository() {
    }

    public static synchronized GankDataRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new GankDataRepository();
        return INSTANCE;
    }


    @Override
    public Observable<GankItem> getTypeData(String type, String page, User user) {
        return GankApi.getInstance().service.getTypeData(type,page,user);
    }

    @Override
    public Observable<Gank> getDailyData(String [] array, User user) {
        String year=array[0];
        String month=array[1];
        String day=array[2];
        return GankApi.getInstance().service.getDailyData(year,month,day,user);
    }

    @Override
    public Observable<CommonResponse<String>> deleteOneFav(User user, String _id) {
        return GankApi.getInstance().service.deleteFav(user,_id);
    }


    @Override
    public Observable<CommonResponse<String>> getDateByPage(String page) {
        return GankApi.getInstance().service.getDateByPage(page);
    }

    @Override
    public Observable<CommonResponse<String>> addOneFav(GankContent item, String token) {
        return GankApi.getInstance().service.addOneFav(item,token);
    }

    @Override
    public Observable<GankFavs> getFavs(User user,String type, String page, String count) {
        return GankApi.getInstance().service.getFavs(user,type,page,count);
    }

    @Override
    public Call<CommonResponse<String>> getStartImage() {
        return GankApi.getInstance().service.getStartImage();
    }
/*---------------------------------------------------------------------------------------------------*/

    private void initDB(Context context){
        MyDatabaseHelper helper=new MyDatabaseHelper(context,"gank.db",null,1);
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
                MoreEntity moreEntity = new MoreEntity(type, Integer.valueOf(id));
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
        list.add(new MoreEntity(Constant.ANDROID,R.drawable.android));
        list.add(new MoreEntity(Constant.IOS,R.drawable.ios));
        list.add(new MoreEntity(Constant.WEB,R.drawable.web));
        list.add(new MoreEntity(Constant.PHOTO,R.drawable.phot));
        list.add(new MoreEntity(Constant.EXPANDRES,R.drawable.expended));
        list.add(new MoreEntity(Constant.RECOMMEND,R.drawable.recommend));
        list.add(new MoreEntity(Constant.APP,R.drawable.app));
        list.add(new MoreEntity(Constant.VIDEO,R.drawable.video));
        for (MoreEntity moreEntity : list) {
            contentValues.put("type",moreEntity.getType());
            contentValues.put("resId",String.valueOf(moreEntity.getResId()));
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

    @Override
    public Observable<CommonResponse<List<GankSearchItem>>> getSearchResult(String words) {
        return GankApi.getInstance().service.getSearch(words);
    }
}
