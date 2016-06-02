package com.gemptc.wd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.utils.PrefUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText userPhone;
    private EditText userPass;
    private Button btn_Login;
    private ImageView qq_Login;
    private ImageView weibo_Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化界面上的数据
        initViews();
        //空间的监听事件
        initOnClickListeners();
    }

    //初始化控件
    private void initViews() {
        userPhone= (EditText) this.findViewById(R.id.userPhone);
        userPass= (EditText) this.findViewById(R.id.userPass);
        btn_Login= (Button) this.findViewById(R.id.login);
        qq_Login= (ImageView) this.findViewById(R.id.qqLogin);
        weibo_Login= (ImageView) this.findViewById(R.id.weiboLogin);
    }

    //初始化控件的监听事件
    private void initOnClickListeners() {
        MyOnClickListener listener=new MyOnClickListener();
        userPhone.setOnClickListener(listener);
        userPass.setOnClickListener(listener);
        btn_Login.setOnClickListener(listener);
        qq_Login.setOnClickListener(listener);
        weibo_Login.setOnClickListener(listener);
    }

    //点击返回按钮时
    public void fanhui(View view) {
        startActivity(new Intent(this,LoginAndRegisterActivity.class));
        finish();
    }


    //控件的监听事件
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userPhone = LoginActivity.this.userPhone.getText().toString();
            String userPass = LoginActivity.this.userPass.getText().toString();
            switch (v.getId()){
                //点击登录按钮
                case R.id.login:
                    //Toast.makeText(LoginActivity.this, "点击了登录按钮", Toast.LENGTH_SHORT).show();
                    if (!"".equals(userPhone)&&!"".equals(userPass)){
                        if (userPhone.equals("123")&&userPass.equals("456")){
                            //存储用户是否进行了登录
                            PrefUtils.setBoolean(LoginActivity.this,"isLogin",true);
                            //吐司一个登陆成功
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            LoginActivity.this.finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                break;
                //点击第三方qq登录按钮
                case R.id.qqLogin:
                    Toast.makeText(LoginActivity.this, "点击了qq登录按钮", Toast.LENGTH_SHORT).show();
                    break;
                //点击第三方微博登录
                case R.id.weiboLogin:
                    Toast.makeText(LoginActivity.this, "点击了weibo登录按钮", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
