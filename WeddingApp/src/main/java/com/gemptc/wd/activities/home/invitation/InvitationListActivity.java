package com.gemptc.wd.activities.home.invitation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.wedding.R;
import com.gemptc.wd.activities.home.invitation.adapter.InviteMuBanAdapter;
import com.gemptc.wd.activities.home.invitation.adapter.InviteUserAdapter;
import com.gemptc.wd.bean.InvitationBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InvitationListActivity extends AppCompatActivity {

    private ImageView iv_invite_back,iv_invite_add;
    private ScrollView sv_invite;
    private GridView gv_inviteMuBan,gv_inviteUser;
    private List<InvitationBean> invitationList,muBanList;
    private InviteMuBanAdapter inviteMubanAdapter;
    private InviteUserAdapter inviteUserAdapter;

    public static InvitationListActivity activity;

    public static final int ADD_OK = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_list);
        activity=this;

        initView();
        initMuBanGridView();
        initData();
        initListener();

        getInviteData();
    }

    public void getInviteData() {
        String user_invite_info = PrefUtils.getString(this,"user_invite_info",null);
        if (user_invite_info!=null){
            parseInviteData(user_invite_info);
        }
        //获取用户请帖的数据
        getUserInviteData();
    }

    private void initMuBanGridView() {
        muBanList=new ArrayList<>();
        InvitationBean mubanInvite01 = new InvitationBean();
        mubanInvite01.setInvite_name1("吴奇隆");
        mubanInvite01.setInvite_name2("刘诗诗");
        mubanInvite01.setInvite_time("2016年3月20日10时");
        mubanInvite01.setInvate_address("江苏省苏州市吴中区仁爱路1号");
        mubanInvite01.setInvate_content("亲爱的朋友，今日大婚，务必前来！");
        mubanInvite01.setInvate_imageName(UrlAddress.MUBAN_IMAGE_ADDRESS+"muban01.jpg");
        muBanList.add(mubanInvite01);

        InvitationBean mubanInvite02 = new InvitationBean();
        mubanInvite02.setInvite_name1("黄晓明");
        mubanInvite02.setInvite_name2("杨颖");
        mubanInvite02.setInvite_time("2016年3月20日10时");
        mubanInvite02.setInvate_address("江苏省苏州市吴中区仁爱路1号");
        mubanInvite02.setInvate_content("尊敬的领导，今日大婚，务必前来！");
        mubanInvite02.setInvate_imageName(UrlAddress.MUBAN_IMAGE_ADDRESS+"muban02.jpg");
        muBanList.add(mubanInvite02);

        InvitationBean mubanInvite03 = new InvitationBean();
        mubanInvite03.setInvite_name1("李晨");
        mubanInvite03.setInvite_name2("范冰冰");
        mubanInvite03.setInvite_time("2016年3月20日10时");
        mubanInvite03.setInvate_address("江苏省苏州市吴中区仁爱路1号");
        mubanInvite03.setInvate_content("尊敬的同事，今日大婚，务必前来！");
        mubanInvite03.setInvate_imageName(UrlAddress.MUBAN_IMAGE_ADDRESS+"muban03.jpg");
        muBanList.add(mubanInvite03);

        InvitationBean mubanInvite04 = new InvitationBean();
        mubanInvite04.setInvite_name1("邓超");
        mubanInvite04.setInvite_name2("孙俪");
        mubanInvite04.setInvite_time("2016年3月20日10时");
        mubanInvite04.setInvate_address("江苏省苏州市吴中区仁爱路1号");
        mubanInvite04.setInvate_content("尊敬的亲人，今日大婚，务必前来！");
        mubanInvite04.setInvate_imageName(UrlAddress.MUBAN_IMAGE_ADDRESS+"muban04.jpg");
        muBanList.add(mubanInvite04);

        inviteMubanAdapter = new InviteMuBanAdapter(this,muBanList);
        gv_inviteMuBan.setAdapter(inviteMubanAdapter);
    }

    private void initListener() {
        iv_invite_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_invite_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvitationListActivity.this,AddInviteActivity.class);
                intent.putExtra("fromWho","添加");
                startActivityForResult(intent,ADD_OK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==ADD_OK&&resultCode==101){
                getInviteData();
            }
        }
    }

    private void initView() {
        iv_invite_back= (ImageView) findViewById(R.id.iv_invite_back);
        iv_invite_add= (ImageView) findViewById(R.id.iv_invite_add);
        sv_invite= (ScrollView) findViewById(R.id.sv_invite);
        gv_inviteMuBan= (GridView) findViewById(R.id.gv_inviteMoBan);
        gv_inviteUser= (GridView) findViewById(R.id.gv_inviteUser);
    }

    private void initData() {
        invitationList=new ArrayList<>();
        inviteUserAdapter = new InviteUserAdapter(this,invitationList);
        gv_inviteUser.setAdapter(inviteUserAdapter);
    }


    //获取用户请帖的数据
    private void getUserInviteData() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","inviteList");
        params.addBodyParameter("userid", PrefUtils.getString(this,"user_self_id",null));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                if (!result.startsWith("用户")){
                    Log.e("用户的请帖信息",result);
                    //将用户的请帖数据存储起来
                    PrefUtils.setString(InvitationListActivity.this,"user_invite_info",result);
                    //解析数据
                    parseInviteData(result);
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
    }

    private void parseInviteData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<InvitationBean>>(){}.getType();
        List<InvitationBean> invitationBeanList = gson.fromJson(result,type);
        invitationList.clear();
        for (int i = 0; i < invitationBeanList.size(); i++) {
            invitationList.add(invitationBeanList.get(i));
        }

        inviteUserAdapter.notifyDataSetChanged();
    }

}
