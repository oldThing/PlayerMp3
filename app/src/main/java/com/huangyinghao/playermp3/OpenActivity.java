package com.huangyinghao.playermp3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by deny on 2016/1/8.
 */
public class OpenActivity extends AppCompatActivity{

    private ImageView openIage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏系统状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.open_activity);
        openIage = (ImageView) findViewById(R.id.open_image);

        AlphaAnimation aa = new AlphaAnimation(1f, 1f);

        //设置动画时长（2.5秒）,以毫秒计算
        aa.setDuration(2500);
        //开启动画
        openIage.startAnimation(aa);

        //对aa设置动画监听
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //结束动画后进入的界面
                Intent it = new Intent(OpenActivity.this, MainActivity.class);
                startActivity(it);
                //（2.5秒后）关闭
                finish();
            }
        });
    }
}
