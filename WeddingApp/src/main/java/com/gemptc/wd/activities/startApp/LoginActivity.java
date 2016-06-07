package com.gemptc.wd.activities.startApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.utils.MD5Util;
import com.gemptc.wd.utils.PrefUtils;

import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import com.gemptc.wd.utils.QQLogin;
import com.gemptc.wd.utils.UrlAddress;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoginActivity extends QQLogin {

    private EditText userPhone;
    private EditText userPass;
    private Button btn_Login;
    private ImageView qq_Login;
    private ImageView weibo_Login;

    public static LoginActivity loginActivity;
    public static Handler qqLoginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity=this;

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
        finish();
    }

    //点击手机自带的返回按钮
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //控件的监听事件
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final String userPhone = LoginActivity.this.userPhone.getText().toString();
            String userPass = LoginActivity.this.userPass.getText().toString();
            switch (v.getId()){
                //点击登录按钮
                case R.id.login:
                    //Toast.makeText(LoginActivity.this, "点击了登录按钮", Toast.LENGTH_SHORT).show();
                    if (!"".equals(userPhone)&&!"".equals(userPass)){
                        String url = UrlAddress.USER_Controller;
                        RequestParams params = new RequestParams(url);
                        params.addQueryStringParameter("userop","login");
                        params.addQueryStringParameter("userphonenum",userPhone);
                        params.addQueryStringParameter("userpassword", MD5Util.MD5(userPass));
                        x.http().post(params, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.startsWith("登录成功")){
                                    LoginAndRegisterActivity.loginAndRegister.finish();



                                    //存储是否登录的信息

                                    PrefUtils.setBoolean(LoginActivity.this,"isLogin",true);
                                    PrefUtils.setString(LoginActivity.this,"userPhoneNum",userPhone);
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    LoginActivity.this.finish();
                                }else if (result.startsWith("登录失败")){
                                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {

                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this, "账号或者密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                break;
                //点击第三方qq登录按钮
                case R.id.qqLogin:
                    Toast.makeText(LoginActivity.this, "点击了qq登录按钮", Toast.LENGTH_SHORT).show();
                    startQQLogin();
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
