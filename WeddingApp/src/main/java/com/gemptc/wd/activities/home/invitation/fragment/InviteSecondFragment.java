package com.gemptc.wd.activities.home.invitation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.home.invitation.AddInviteActivity;
import com.gemptc.wd.bean.InvitationBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.share.cn.sharesdk.onekeyshare.OnekeyShare;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/6/12.
 */
public class InviteSecondFragment extends Fragment implements View.OnClickListener{

    private ImageView iv_invite_userImage,iv_invite_sendByQQ,iv_invite_sendByWX;
    private TextView tv_invite_content;
    private Button btn_shiyongmuban,btn_sendFriends;
    private View view;

    private LinearLayout ll_invite_share;
    private InvitationBean invitation;
    private String fromWho;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.fragment_invite_second,null);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        btn_shiyongmuban.setOnClickListener(this);
        btn_sendFriends.setOnClickListener(this);
    }

    private void initView() {
        iv_invite_userImage= (ImageView) view.findViewById(R.id.iv_invite_userImage);
        iv_invite_sendByQQ= (ImageView) view.findViewById(R.id.iv_invite_sendByQQ);
        iv_invite_sendByWX= (ImageView) view.findViewById(R.id.iv_invite_sendByWX);

        tv_invite_content= (TextView) view.findViewById(R.id.tv_invite_content);
        btn_shiyongmuban= (Button) view.findViewById(R.id.btn_shiyongmuban);

        ll_invite_share= (LinearLayout) view.findViewById(R.id.ll_invite_share);
        btn_sendFriends= (Button) view.findViewById(R.id.btn_sendFriends);
    }

    private void initData() {
        invitation = (InvitationBean) getActivity().getIntent().getSerializableExtra("inviteBean");
        fromWho = getActivity().getIntent().getStringExtra("fromWho");
        if ("模板".equals(fromWho)){
            ll_invite_share.setVisibility(View.GONE);
        }else{
            btn_shiyongmuban.setVisibility(View.GONE);
        }
        tv_invite_content.setText(invitation.getInvate_content());
        //设置头像
        setUserPic();
    }

    private void setUserPic() {
        String user_self_info = PrefUtils.getString(getActivity(),"user_self_info",null);
        if (user_self_info!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<UserBean>() {
            }.getType();
            UserBean user = gson.fromJson(user_self_info, type);
            Glide.with(getActivity()).load(UrlAddress.USER_IMAGE_ADDRESS+user.getU_picname()).into(iv_invite_userImage);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_shiyongmuban){
            Intent intent = new Intent(getActivity(), AddInviteActivity.class);
            intent.putExtra("inviteBean",invitation);
            if ("模板".equals(fromWho)){
                intent.putExtra("fromWho","模板");
            }else{
                intent.putExtra("fromWho","用户");
            }
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        if (v.getId()==R.id.btn_sendFriends){
            ToastUtils.shortToast(getActivity(),"您点击了发送给好友");
            ShareSDK.initSDK(getActivity());
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("您收到一封喜帖");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(UrlAddress.HOST_ADDRESS_PROJECT+"share.jsp?xinlang="+invitation.getInvite_name1()+"&xinniang="+invitation.getInvite_name2()+"&time="+invitation.getInvite_time()+"&address="+invitation.getInvate_address()+"&content="+invitation.getInvate_content());
            // text是分享文本，所有平台都需要这个字段
            oks.setText("诚挚的邀请您参加我们的婚礼.......");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("https://www.baidu.com");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("www.qq.com");
            // 启动分享GUI
            oks.show(getActivity());
        }
    }
}
