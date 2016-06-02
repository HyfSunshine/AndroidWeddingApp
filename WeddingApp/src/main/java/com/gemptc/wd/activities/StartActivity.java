package com.gemptc.wd.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.wedding.R;
import com.gemptc.wd.utils.PrefUtils;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取到用户是否登录的信息
                boolean isLogin= PrefUtils.getBoolean(StartActivity.this,PrefUtils.PREF_NAME,true);
                if (!isLogin){
                    //说明用户没有登录，跳转到登录的界面
                    startActivity(new Intent(StartActivity.this,LoginAndRegisterActivity.class));
                }else{
                    //用户登陆了，直接跳转到主页面
                    startActivity(new Intent(StartActivity.this,MainActivity.class));
                }
                //这是一个切换画面的效果，淡入淡出
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }, 3000);
    }
}
