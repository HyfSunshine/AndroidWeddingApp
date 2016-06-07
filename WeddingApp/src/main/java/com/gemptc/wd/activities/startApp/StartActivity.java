package com.gemptc.wd.activities.startApp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
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

<<<<<<< HEAD:WeddingApp/src/main/java/com/gemptc/wd/activities/startApp/StartActivity.java
                boolean isLogin= PrefUtils.getBoolean(StartActivity.this,"isLogin",true);
=======
               // boolean isLogin= PrefUtils.getBoolean(StartActivity.this,PrefUtils.PREF_NAME,true);

                boolean isLogin= PrefUtils.getBoolean(StartActivity.this,"isLogin",false);
>>>>>>> 6e2149986941524f80c5d5115a5e860d19a3a0a0:WeddingApp/src/main/java/com/gemptc/wd/activities/startApp/StartActivity.java

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
