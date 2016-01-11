package com.huangyinghao.playermp3.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.huangyinghao.playermp3.domain.SingerInfo;

import org.litepal.crud.DataSupport;

/**
 * Created by deny on 2015/12/27.
 */
public class SingerLoadUtils {
    private static SingerLoadUtils singerLoadUtils;
    private static ContentResolver mContentResolver;

    private Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private String[] projection = {
            "distinct  "+ MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.IS_MUSIC
    };


    private SingerLoadUtils(){
//        String sql = "select distinct artid from singerinfo";
        //String selection = "1=1) "+GROUPBY +" number";
//        query(Calls.ContentUri,selection,null,null,null,null);
        Cursor cursor = mContentResolver.query(uri, projection,null, null, null, null);

        //先把数据库里的数据清除
        DataSupport.deleteAll(SingerInfo.class);
        if(cursor == null){
            return;
        } else if(!cursor.moveToFirst()){
            return;
        } else {
            int isMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);
            int artId = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            do{

                int mIsMusic = cursor.getInt(isMusic);
                if(mIsMusic == 0){
                    continue;
                }
                long pArtId = cursor.getLong(artId);

                SingerInfo singerInfo = new SingerInfo( pArtId );
                singerInfo.saveThrows();

            }while(cursor.moveToNext());
        }
    }

    public static SingerLoadUtils  INSTANCE(ContentResolver contentResolver){
        if (null == singerLoadUtils){
            mContentResolver = contentResolver;
            singerLoadUtils = new SingerLoadUtils();
        }
        return singerLoadUtils;
    }
}
