package com.huangyinghao.playermp3.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by deny on 2015/12/27.
 */
public class SingerInfo extends DataSupport implements Parcelable{

    private long artId;


    public SingerInfo( long artId ){
        this.artId = artId;
    }

    protected SingerInfo(Parcel in) {
        artId = in.readLong();
    }

    public static final Creator<SingerInfo> CREATOR = new Creator<SingerInfo>() {
        @Override
        public SingerInfo createFromParcel(Parcel in) {
            return new SingerInfo(in);
        }

        @Override
        public SingerInfo[] newArray(int size) {
            return new SingerInfo[size];
        }
    };


    public long getArtId() {
        return artId;
    }

    public void setArtId(long artId) {
        this.artId = artId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(artId);
    }
}
