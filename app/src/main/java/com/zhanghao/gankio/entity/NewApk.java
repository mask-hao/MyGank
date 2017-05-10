package com.zhanghao.gankio.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhanghao on 2016/12/18.
 */

public class NewApk implements Parcelable {

    public NewApk(){};


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public double getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    private int progress;
    private int currentFileSize;
    private int totalFileSize;



    private NewApk(Parcel in){
        progress=in.readInt();
        currentFileSize=in.readInt();
        currentFileSize=in.readInt();
    }



    public static final Creator<NewApk> CREATOR=new Creator<NewApk>(){

        @Override
        public NewApk createFromParcel(Parcel source) {
            return new NewApk(source);
        }

        @Override
        public NewApk[] newArray(int size) {
            return new NewApk[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(progress);
        dest.writeInt(currentFileSize);
        dest.writeInt(totalFileSize);
    }
}
