package com.huangyinghao.playermp3.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.huangyinghao.playermp3.domain.MusicInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deny on 2015/12/24.
 */
public class MusicLoadUtils {
    private List<MusicInfo> mMusicInfos = new ArrayList<>();
    private static MusicLoadUtils mMusicLoadUtils;
    private static ContentResolver mContentResolver;


    //Uri，指向external的database
    private Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };
    private MusicLoadUtils(){
        //先把数据库里的数据清除
        DataSupport.deleteAll(MusicInfo.class);

        Cursor cursor = mContentResolver.query(contentUri, projection, null, null, null);
        //判断这个cursor是否为空，是否为第一个
        if(cursor == null){
            return;
        } else if (!cursor.moveToFirst()){
            return;
        } else {

            int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int name = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int singer = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int artId = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);

            do{
                int _id = cursor.getInt(id);
                String _name = cursor.getString(name);
                String _singer = cursor.getString(singer);
                int _duration = cursor.getInt(duration);
                String _data = cursor.getString(data);
                int _isMusic = cursor.getInt(isMusic);
                long _albumId = cursor.getLong(albumId);
                long _artId = cursor.getLong(artId);

                if (_isMusic == 0){
//                    Log.i("size" , mMusicInfos.size() + "");
                    continue;
                }
                MusicInfo musicInfo = new MusicInfo(_albumId , _duration , _name , _singer ,_id , _data ,_artId);

                //添加
                musicInfo.saveThrows();
            } while(cursor.moveToNext());
        }
    }

    public static MusicLoadUtils Instance(ContentResolver pContentResolver){
        if(null == mMusicLoadUtils){
            mContentResolver = pContentResolver;
            mMusicLoadUtils = new MusicLoadUtils();
        }

        return mMusicLoadUtils;
    }
}
