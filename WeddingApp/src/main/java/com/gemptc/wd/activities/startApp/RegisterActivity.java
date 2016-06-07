package com.gemptc.wd.activities.startApp;

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
import com.gemptc.wd.utils.MD5Util;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

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

    //网络访问标识符，用来区分是用来进行什么操作的
    public int InternetCode;
    private RequestParams params;
    private Handler loginHandler;
    private MyCommonCallback callback;

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
        //Wedding
//        String APPKEY="136ada2450f70";
//        String APPSECRET="3d0badd15f6d3ae88f5698a24a8ad74c";

        //SMSCode
//        String APPKEY="13504fa6f2ba8";
//        String APPSECRET="7aaa84aeb776cfaa66c57f4bfe500e82";

        //3
        String APPKEY="134f2c0a441b1";
        String APPSECRET="31ca2c1e67116779c9fbcf29d46d73c0";


        SMSSDK.initSDK(this,APPKEY,APPSECRET);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        isSuccess=true;
                        loginHandler.sendEmptyMessage(1000);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    //验证失败
                    isSuccess=false;
                    loginHandler.sendEmptyMessage(2000);
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
        String registerPhoneNum=registerUserPhone.getText().toString();
        //验证码
        String yanZhengMa=registerYanZhengMa.getText().toString();
        //密码
        String passWord = registerUserPass.getText().toString();
        //重复密码
        String rePassword = reRegisterUserPass.getText().toString();
        
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
                if (!"".equals(registerPhoneNum)&&!"".equals(yanZhengMa)&&!"".equals(passWord)&&!"".equals(rePassword)){
                    if (xieYiCheckBox.isChecked()){
                        //判断两次密码是否一致
                        if (passWord.equals(rePassword)){
                            userIsExist(registerPhoneNum);
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

    //从网络上验证用户是否存在
    private void userIsExist(String userPhoneNum){
        String url = UrlAddress.HOST_ADDRESS_PROJECT+"UserController";
        params = new RequestParams(url);
        params.addQueryStringParameter("userop","userisexist");
        params.addQueryStringParameter("userphonenum",userPhoneNum);
        InternetCode=100;
        callback = new MyCommonCallback();
        x.http().post(params, callback);
    }

    class MyCommonCallback implements Callback.CommonCallback<String> {
        private String registerPhoneNum=registerUserPhone.getText().toString();
        private String yanZhengMa=registerYanZhengMa.getText().toString();
        private String password=registerUserPass.getText().toString();

        @Override
        public void onSuccess(String result) {
            if (InternetCode==100) {
                if (result.startsWith("用户已存在")) {
                    Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                } else if (result.startsWith("用户不存在")) {
                    loginHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            if (msg.what==1000){
                                startRegister(registerPhoneNum,password);
                            }else if (msg.what==2000){
                                Toast.makeText(RegisterActivity.this, "手机号码验证失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    //提交验证码，验证是否正确
                    SMSSDK.submitVerificationCode("86", registerPhoneNum,yanZhengMa);
                }
            }
            if (InternetCode==200){
                if (result.startsWith("注册成功")) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }
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
    }


    private void startRegister(String userPhoneNum,String password){
        String url = UrlAddress.HOST_ADDRESS_PROJECT+"UserController";
        params=new RequestParams(url);
        params.addQueryStringParameter("userop","adduser");
        params.addQueryStringParameter("userphonenum",userPhoneNum);
        //MD5加密密码
        params.addQueryStringParameter("userpassword", MD5Util.MD5(password));
        //设置操作码
        InternetCode=200;
        callback = new MyCommonCallback();
        x.http().post(params,callback);
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
