package com.gemptc.wd.activities.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.startApp.LoginAndRegisterActivity;
import com.gemptc.wd.utils.MD5Util;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdatePasswordActivity extends AppCompatActivity {

    private ImageView iv_updatepassword_back;
    private EditText edt_oldPassword,edt_NewPassword,edt_ReNewPassword;
    private Button btn_updatepassword;
    private SweetAlertDialog sweetAlertDialog;

    //上网验证码
    private String internetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initView();
        initListenser();
    }

    private void initListenser() {
        MyListener listener = new MyListener();
        iv_updatepassword_back.setOnClickListener(listener);
        btn_updatepassword.setOnClickListener(listener);
    }

    private void initView() {
        iv_updatepassword_back= (ImageView) findViewById(R.id.iv_updatepassword_back);
        edt_oldPassword= (EditText) findViewById(R.id.edt_oldPassword);
        edt_NewPassword= (EditText) findViewById(R.id.edt_NewPassword);
        edt_ReNewPassword= (EditText) findViewById(R.id.edt_ReNewPassword);
        btn_updatepassword= (Button) findViewById(R.id.btn_updatepassword);
    }

    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String oldPassWord = edt_oldPassword.getText().toString();
            String newPassWord = edt_NewPassword.getText().toString();
            String reNewPassWord = edt_ReNewPassword.getText().toString();
            switch (v.getId()){
                case R.id.iv_updatepassword_back:
                    finish();
                break;
                case R.id.btn_updatepassword:
                    //首先判断是否为空
                    if (!"".equals(oldPassWord)&&!"".equals(newPassWord)&&!"".equals(reNewPassWord)){
                        //然后判断两次密码是否一致
                        if (newPassWord.equals(reNewPassWord)){
                            //联网判断密码是否正确
                            wordIsRight(oldPassWord);
                        }else{
                            sweetAlertDialog=null;
                            sweetAlertDialog=new SweetAlertDialog(UpdatePasswordActivity.this,SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("错误").setContentText("两次密码输入不一致!请重新输入...").show();
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    edt_oldPassword.setText("");
                                    edt_NewPassword.setText("");
                                    edt_ReNewPassword.setText("");
                                    sweetAlertDialog.dismiss();
                                }
                            });
                        }
                    }else{
                        sweetAlertDialog=null;
                        sweetAlertDialog=new SweetAlertDialog(UpdatePasswordActivity.this,SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("错误").setContentText("不能为空!").show();
                    }
                    break;
            }
        }
    }

    //联网判断用户原密码是否正确
    private void wordIsRight(String oldPassWord) {
        internetCode="验证密码";
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","yanzhengmima");
        params.addBodyParameter("userid", PrefUtils.getString(this,"user_self_id",null));
        params.addBodyParameter("password", MD5Util.MD5(oldPassWord));
        x.http().post(params,new MyCommonCallback());
    }

    private class MyCommonCallback implements Callback.CommonCallback<String>{

        @Override
        public void onSuccess(String result) {
            if ("验证密码".equals(internetCode)){
                if (result.startsWith("验证成功")){
                    //开始修改密码
                    startUpdatePassword();
                }else{
                    sweetAlertDialog=null;
                    sweetAlertDialog=new SweetAlertDialog(UpdatePasswordActivity.this,SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("错误").setContentText("旧密码输入错误,请重新输入...").show();
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            edt_oldPassword.setText("");
                            edt_NewPassword.setText("");
                            edt_ReNewPassword.setText("");
                            sweetAlertDialog.dismiss();
                        }
                    });
                }
            }
            if ("修改密码".equals(internetCode)){
                if (result.startsWith("修改成功")){
                    sweetAlertDialog=null;
                    sweetAlertDialog=new SweetAlertDialog(UpdatePasswordActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("修改密码成功").show();
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            PrefUtils.setBoolean(UpdatePasswordActivity.this,"isLogin",false);
                            startActivity(new Intent(UpdatePasswordActivity.this, LoginAndRegisterActivity.class));
                            ToastUtils.shortToast(UpdatePasswordActivity.this,"密码已改变，请重新登录");
                            finish();
                            MainActivity.mainActivity.finish();
                        }
                    });
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

    //修改密码的方法
    private void startUpdatePassword() {
        internetCode="修改密码";
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","updateUserPassword");
        params.addBodyParameter("userid",PrefUtils.getString(this,"user_self_id",null));
        params.addBodyParameter("newPassword",MD5Util.MD5(edt_NewPassword.getText().toString()));

        x.http().post(params,new MyCommonCallback());
    }
}
