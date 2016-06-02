package com.gemptc.wd.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.utils.PrefUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText registerUserPhone;
    private EditText registerYanZhengMa;
    private EditText registerUserPass;
    private EditText reRegisterUserPass;
    private Button getYanZhengMa;
    private CheckBox xieYiCheckBox;
    private TextView yongHuXieYi;
    private Button register;

    private ImageView qqLogin;
    private ImageView weiboLogin;
    private Timer timer;
    private int saveTime;
    private Handler handler;
    private int currentTime;

    //短信是否验证成功的标识,默认是验证失败的
    public boolean isSuccess=false;
    private Handler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化布局
        initViews();
        //初始化Handler
        initHandler();
        //初始化获取验证码的时间
        initGetCodeTime();
        //初始化监听事件
        initListeners();

        //短信验证
        inintSMSCode();
    }


    private void inintSMSCode() {
        String APPKEY="136ada2450f70";
        String APPSECRET="3d0badd15f6d3ae88f5698a24a8ad74c";
        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        isSuccess=true;
                        loginHandler.sendEmptyMessage(100);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    //验证失败
                    isSuccess=false;
                    loginHandler.sendEmptyMessage(100);
                    //handler.sendEmptyMessage(1);
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int time= (int) msg.obj;
                if (time>0) {
                    getYanZhengMa.setText("正在获取(" + time + "S)");
                    getYanZhengMa.setClickable(false);
                }else if (time==0){
                    getYanZhengMa.setText("获取验证码");
                    getYanZhengMa.setClickable(true);
                    if (timer!=null){
                        PrefUtils.setInt(RegisterActivity.this, "register_getYanZhengMa_time", currentTime);
                        timer.cancel();
                        timer=null;
                    }
                }
            }
        };
    }

    private void initGetCodeTime() {
        saveTime = PrefUtils.getInt(this,"register_getYanZhengMa_time",0);

        if (saveTime>0){
            getYanZhengMa.setClickable(false);
            currentTime=saveTime;
            if (timer==null) {
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = handler.obtainMessage();
                        if (currentTime > 0) {
                            message.obj = currentTime;
                            handler.sendMessage(message);
                        } else {
                            message.obj = 0;
                            handler.sendMessage(message);
                        }
                        currentTime--;
                    }
                }, 5, 1000);
            }
        }
    }

    private void initViews() {
        registerUserPhone= (EditText) findViewById(R.id.registerUserPhone);
        registerYanZhengMa= (EditText) findViewById(R.id.registerYanZhengMa);
        registerUserPass= (EditText) findViewById(R.id.registerPassWord);
        reRegisterUserPass= (EditText) findViewById(R.id.reRegisterPassWord);
        getYanZhengMa = (Button) findViewById(R.id.btn_YanZhengMa);
        xieYiCheckBox= (CheckBox) findViewById(R.id.XieYiCheckBox);
        yongHuXieYi = (TextView) findViewById(R.id.YongHuXieYi);

        register = (Button) findViewById(R.id.register);

        qqLogin = (ImageView) this.findViewById(R.id.qqLogin);
        weiboLogin = (ImageView) this.findViewById(R.id.weiboLogin);
    }

    private void initListeners() {
        getYanZhengMa.setOnClickListener(this);
        yongHuXieYi.setOnClickListener(this);
        register.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        weiboLogin.setOnClickListener(this);
    }

    //点击返回时
    public void fanhui(View view) {
        finish();
    }

    //该activity的所有点击事件
    @Override
    public void onClick(View v) {
        //手机号码
        final String registerPhoneNum=registerUserPhone.getText().toString();
        //验证码
        final String yanZhengMa=registerYanZhengMa.getText().toString();
        //密码
        final String passWord = registerUserPass.getText().toString();
        //重复密码
        final String rePassword = reRegisterUserPass.getText().toString();
        
        switch (v.getId()){
            //点击获取验证码
            case R.id.btn_YanZhengMa:
                //开始获取验证码
                if (registerPhoneNum.length()==11){
                    currentTime = 60;
                    if (timer==null) {
                        timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Message message = handler.obtainMessage();
                                if (currentTime > 0) {
                                    message.obj = currentTime;
                                    handler.sendMessage(message);
                                    currentTime--;
                                } else {
                                    message.obj = 0;
                                    handler.sendMessage(message);
                                }
                            }
                        }, 5, 1000);
                    }

                    SMSSDK.getVerificationCode("86",registerPhoneNum);
                }else if ("".equals(registerPhoneNum)){
                    Toast.makeText(RegisterActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "手机号码长度不正确", Toast.LENGTH_SHORT).show();
                }
            break;
            //点击用户协议
            case R.id.YongHuXieYi:
                startActivity(new Intent(this,RegisterXieYiActivity.class));
                break;
            //点击注册按钮时
            case R.id.register:
                //判断是否可以进行注册
                if (!"".equals(registerPhoneNum)&&!"".equals(yanZhengMa)&&!"".equals(passWord)&&!"".equals(passWord)&&!"".equals(rePassword)){
                    if (xieYiCheckBox.isChecked()){
                        //判断两次密码是否一致
                        if (passWord.equals(rePassword)){
                            //判断验证码是否通过
                            loginHandler = new Handler(){
                                @Override
                                public void handleMessage(Message msg) {
                                    if (msg.what==100){
                                        if (isSuccess){
                                            Toast.makeText(RegisterActivity.this, "正在注册.......", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            };
                            //提交验证码，验证是否正确
                            SMSSDK.submitVerificationCode("86",registerPhoneNum,yanZhengMa);
                        }else{
                            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "请先同意协议", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
            //点击qq登录时
            case R.id.qqLogin:
                Toast.makeText(RegisterActivity.this, "点击了QQ登录按钮", Toast.LENGTH_SHORT).show();
                break;
            //点击微博登录时
            case R.id.weiboLogin:
                Toast.makeText(RegisterActivity.this, "点击了微博登录按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            PrefUtils.setInt(this, "register_getYanZhengMa_time", currentTime);
            timer.cancel();
            timer = null;
        }
    }
}
