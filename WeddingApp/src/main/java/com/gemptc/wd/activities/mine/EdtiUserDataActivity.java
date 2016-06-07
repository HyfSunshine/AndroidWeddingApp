package com.gemptc.wd.activities.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.wedding.R;
import com.gemptc.wd.utils.ToastUtils;

public class EdtiUserDataActivity extends AppCompatActivity {

    private ImageView iv_edt_fanhui;
    private LinearLayout ll_edt_userPic;
    private LinearLayout ll_edt_userName;
    private LinearLayout ll_edt_userMarryTime;
    private LinearLayout ll_edt_userSex;
    private LinearLayout ll_edt_userAddress;
    private LinearLayout ll_edt_userSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edti_user_data);
        initView();
        initListener();
    }

    private void initView() {
        iv_edt_fanhui= (ImageView) findViewById(R.id.iv_edt_fanhui);
        ll_edt_userPic= (LinearLayout) findViewById(R.id.ll_edt_userPic);
        ll_edt_userName= (LinearLayout) findViewById(R.id.ll_edt_userName);
        ll_edt_userMarryTime= (LinearLayout) findViewById(R.id.ll_edt_userMarryTime);
        ll_edt_userSex= (LinearLayout) findViewById(R.id.ll_edt_userSex);
        ll_edt_userAddress= (LinearLayout) findViewById(R.id.ll_edt_userAddress);
        ll_edt_userSign= (LinearLayout) findViewById(R.id.ll_edt_userSign);
    }

    private void initListener() {
        MyListener listener = new MyListener();
        iv_edt_fanhui.setOnClickListener(listener);
        ll_edt_userPic.setOnClickListener(listener);
        ll_edt_userName.setOnClickListener(listener);
        ll_edt_userMarryTime.setOnClickListener(listener);
        ll_edt_userSex.setOnClickListener(listener);
        ll_edt_userAddress.setOnClickListener(listener);
        ll_edt_userSign.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_edt_fanhui:
                    finish();
                break;
                case R.id.ll_edt_userPic:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户图片");
                break;
                case R.id.ll_edt_userName:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户昵称");
                break;
                case R.id.ll_edt_userMarryTime:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑我的结婚时间");
                break;
                case R.id.ll_edt_userSex:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户性别");
                break;
                case R.id.ll_edt_userAddress:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户地址");
                break;
                case R.id.ll_edt_userSign:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户签名");
                break;
            }
        }
    }
}
