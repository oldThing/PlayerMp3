package com.huangyinghao.playermp3.utils;

import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.ui.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by deny on 2015/12/27.
 */
public class ToolUtils {

    //判断服务是否存在
    public static boolean isServiceRun(Context context , String className){
        ActivityManager manager = (ActivityManager) context.getApplicationContext().
                getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(50);

        for(int i = 0 ; i < runningServices.size() ; i++){
            if(runningServices.get(i).service.getClassName().equals(className)){
                return true;
            }
        }
        return false;

    }

    //显示时间
    public static String toTime(int time){
        int minute = time / 1000 / 60;
        int s = time / 1000 % 60;
        String mm = null;
        String ss = null;
        if(minute<10)mm = "0" + minute;
        else mm = minute + "";

        if(s <10)ss = "0" + s;
        else ss = "" + s;
        return mm + ":" + ss;
    }

    //加载图片 圆圈
    public static void loadPic(Context context , long albumId , ImageView imageView){
        final ImageLoader loader = ImageLoader.getInstance();
        if(!loader.isInited()){
            loader.init(ImageLoaderConfiguration.createDefault(context));
        }
        final DisplayImageOptions option = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .displayer(new CircleBitmapDisplayer())
                .build();

        Uri artistUri = Uri.parse("content://media/external/audio/albumart");
        ContentUris uris = new ContentUris();
        Uri uri = uris.withAppendedId(artistUri , albumId);

        if (uri != null) {
        //加载图片
        String URL = uri.toString();
//            Log.i("tag" , "URL:" + URL);
            loader.displayImage(URL, imageView, option, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }
                //如果没有专辑图片，用默认的。
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageResource(R.mipmap.img_album_background);

                }
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }

    public static void loadCirPic(Context context , long albumId , ImageView imageView){
        final ImageLoader loader = ImageLoader.getInstance();
        if(!loader.isInited()){
            loader.init(ImageLoaderConfiguration.createDefault(context));
        }
        final DisplayImageOptions option = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new CircleBitmapDisplayer())
                .build();

        Uri artistUri = Uri.parse("content://media/external/audio/albumart");
        ContentUris uris = new ContentUris();
        Uri uri = uris.withAppendedId(artistUri , albumId);

        if (uri != null) {
            //加载图片
            String URL = uri.toString();
//            Log.i("tag" , "URL:" + URL);
            loader.displayImage(URL, imageView, option, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }
                //如果没有专辑图片，用默认的。
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageResource(R.mipmap.img_album_background);

                }
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                }
                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
    }
}
