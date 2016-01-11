package com.huangyinghao.playermp3.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by deny on 2015/12/24.
 */
public class MusicInfo extends DataSupport implements Parcelable  {
    private int _id;
    private String name;
    private String singer;
    private int duration;
    private String url;
    private long song_id;
    private long albun_id; // 图片的id
    private long artId;


    public MusicInfo(){

    }

    public MusicInfo(long albun_id, int duration, String name, String singer, long song_id, String url , long artId) {
        this.albun_id = albun_id;
        this.duration = duration;
        this.name = name;
        this.singer = singer;
        this.song_id = song_id;
        this.url = url;
        this.artId = artId;
    }

    protected MusicInfo(Parcel in) {
        _id = in.readInt();
        name = in.readString();
        singer = in.readString();
        duration = in.readInt();
        url = in.readString();
        song_id = in.readLong();
        albun_id = in.readLong();
        artId = in.readLong();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
    }
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getAlbun_id() {
        return albun_id;
    }

    public void setAlbun_id(long albun_id) {
        this.albun_id = albun_id;
    }

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
        dest.writeInt(_id);
        dest.writeString(name);
        dest.writeString(singer);
        dest.writeInt(duration);
        dest.writeString(url);
        dest.writeLong(song_id);
        dest.writeLong(albun_id);
        dest.writeLong(artId);
    }
}
