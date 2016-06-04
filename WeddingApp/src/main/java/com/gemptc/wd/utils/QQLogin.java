package com.gemptc.wd.utils;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gemptc.wd.activities.LoginAndRegisterActivity;
import com.gemptc.wd.activities.MainActivity;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/6/3.
 */
public abstract class QQLogin extends AppCompatActivity {
    private static final String APP_ID="1105446072";
    private Tencent tencent;
    private Handler handler;
    private BaseIUiListener listener;
    private String openid;

    private int InternetCode;
    private RequestParams params;
    private String qqUserName;

    private void initTencent(){
        listener = new BaseIUiListener();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==100){
                    //判断用户是否存在
                    userIsExist(openid);
                }
                if (msg.what==200){
                    startRegister(openid);
                }
            }
        };

            tencent = Tencent.createInstance(APP_ID, getApplicationContext());
            tencent.login(this,"all",listener);
    }

    class BaseIUiListener implements IUiListener{

        @Override
        public void onComplete(Object response) {
            JSONObject responseJsonobject = (JSONObject) response;
            openid = responseJsonobject.optString("openid");
            final String access_token = responseJsonobject.optString("access_token");
//            final String expires_in = responseJsonobject.optString("expires_in");
            ToastUtils.longToast(QQLogin.this,response.toString());

            Log.e("用户登录成功",response.toString());

            getUserData(APP_ID, openid,access_token);
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    //点击开始登录
    public void startQQLogin(){
        initTencent();
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode,resultCode,data,listener);
        }
    }

    //从网络上获取数据
    private void getUserData(String APPID,String openid,String access_token){
        InternetCode=10;

        String url="https://graph.qq.com/user/get_user_info";
        params = new RequestParams(url);
        //https://graph.qq.com/user/get_user_info?access_token=11111111111111111111111111111111&oauth_consumer_key=101137684&openid=333333333333333333333333333
        params.addQueryStringParameter("access_token",access_token);
        params.addQueryStringParameter("oauth_consumer_key",APPID);
        params.addQueryStringParameter("openid",openid);
        x.http().get(params, new MyCommonCallback());
    }

    //判断用户是否存在
    private void userIsExist(String oppenid){
        InternetCode=20;

        Log.e("userIsExist方法被调用","userIsExist方法被调用");

        String url = UrlAddress.HOST_ADDRESS_PROJECT+"UserController";
        params = new RequestParams(url);
        params.addQueryStringParameter("userop","userisexist");
        params.addQueryStringParameter("userphonenum",oppenid);
        x.http().post(params, new MyCommonCallback());
    }

    //qq用户登录注册
    private void startRegister(String openid){
        InternetCode=30;
        String url = UrlAddress.HOST_ADDRESS_PROJECT+"UserController";
        params=new RequestParams(url);
        params.addQueryStringParameter("userop","adduser");
        params.addQueryStringParameter("userphonenum",openid);
        //MD5加密密码
        params.addQueryStringParameter("userpassword", MD5Util.MD5("1234567890"));
        params.addQueryStringParameter("username",qqUserName);
        //设置操作码
        x.http().post(params,new MyCommonCallback());
    };

    class MyCommonCallback implements Callback.CommonCallback<String>{

        @Override
        public void onSuccess(String result) {
            if (InternetCode==10){
                try {
                    JSONObject jsonResult=new JSONObject(result);
                    qqUserName = jsonResult.getString("nickname");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(100);

                //跳转到主页面
                ToastUtils.shortToast(QQLogin.this,"登录成功");
                PrefUtils.setBoolean(QQLogin.this,"isLogin",true);
                PrefUtils.setString(QQLogin.this,"userPhoneNum",openid);
                startActivity(new Intent(QQLogin.this,MainActivity.class));
                if (LoginAndRegisterActivity.loginAndRegister!=null) {
                    LoginAndRegisterActivity.loginAndRegister.finish();
                }

                Log.e("获取用户信息成功",result);
            }
            if (InternetCode==20){
                if (result.startsWith("用户不存在")){
                    handler.sendEmptyMessage(200);
                }
                Log.e("用户是否存在",result);
                QQLogin.this.finish();
            }
            if (InternetCode==30){
                Log.e("注册是否成功",result);
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
}
