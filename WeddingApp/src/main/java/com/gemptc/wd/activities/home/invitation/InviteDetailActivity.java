package com.gemptc.wd.activities.home.invitation;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.facebook.imageutils.BitmapUtil;
import com.gemptc.wd.activities.home.invitation.fragment.InviteFirstFragment;
import com.gemptc.wd.activities.home.invitation.fragment.InviteSecondFragment;
import com.gemptc.wd.adapter.FragmentAdapter;
import com.gemptc.wd.bean.InvitationBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InviteDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Fragment> inviteFragmentList;
    private ViewPager vp_invite_detail;
    private TextView tv_invite_headTitle,tv_invite_youshangjiao;
    private ImageView iv_invite_fanhui;
    private String fromWho;
    private Intent intent;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_detail);
        initView();
        initData();
        initListener();
        initFragment();
    }

    private void initData() {
        intent = getIntent();
        fromWho = intent.getStringExtra("fromWho");
        if ("模板".equals(fromWho)){
            tv_invite_headTitle.setText("模板");
            tv_invite_youshangjiao.setText("保存");
        }else{
            tv_invite_headTitle.setText("我的请帖");
            tv_invite_youshangjiao.setText("编辑");
        }
    }


    private void initView() {
        vp_invite_detail= (ViewPager) findViewById(R.id.vp_invite_detail);

        tv_invite_headTitle= (TextView) findViewById(R.id.tv_invite_headTitle);
        tv_invite_youshangjiao= (TextView) findViewById(R.id.tv_invite_youshangjiao);
        iv_invite_fanhui= (ImageView) findViewById(R.id.iv_invite_fanhui);
    }

    private void initFragment() {
        inviteFragmentList= new ArrayList<>();

        InviteFirstFragment inviteFirstFragment = new InviteFirstFragment();
        InviteSecondFragment inviteSecondFragment = new InviteSecondFragment();
        inviteFragmentList.add(inviteFirstFragment);
        inviteFragmentList.add(inviteSecondFragment);

        //初始化适配器
        FragmentManager manager = getSupportFragmentManager();
        FragmentAdapter fragmentAdapter=new FragmentAdapter(manager,inviteFragmentList);
        vp_invite_detail.setAdapter(fragmentAdapter);
    }


    private void initListener() {
        iv_invite_fanhui.setOnClickListener(this);
        tv_invite_youshangjiao.setOnClickListener(this);
    }

    //上传模板到服务器
    private void upLoadMuBan(InvitationBean invitation) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","addInvite");
        params.addBodyParameter("userid", PrefUtils.getString(this,"user_self_id",null));
        params.addBodyParameter("xinlangName",invitation.getInvite_name1());
        params.addBodyParameter("xinniangName",invitation.getInvite_name2());
        params.addBodyParameter("weddingTime",invitation.getInvite_time());
        params.addBodyParameter("weddingAddress",invitation.getInvate_address());
        params.addBodyParameter("inviteContent",invitation.getInvate_content());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("保存成功");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent();
                        intent.putExtra("data_return","helloworld");
                        setResult(101,intent);
                        finish();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("网络连接失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Log.e("添加请帖错误","网络访问完毕");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_invite_fanhui:
                finish();
            break;

            case R.id.tv_invite_youshangjiao:
                if ("模板".equals(fromWho)){
                    sweetAlertDialog=null;
                    sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.setTitleText("正在保存，请稍候");
                    sweetAlertDialog.show();
                    //保存模板
                    InvitationBean invitation = (InvitationBean) intent.getSerializableExtra("inviteBean");
                    upLoadMuBan(invitation);
                }else{
                    //修改用户已经有的模板
                    Intent intent02 = new Intent(InviteDetailActivity.this,AddInviteActivity.class);
                    intent02.putExtra("fromWho","用户");
                    intent02.putExtra("inviteBean",intent.getSerializableExtra("inviteBean"));
                    startActivity(intent02);
                    finish();
                }
                break;
        }
    }
}
