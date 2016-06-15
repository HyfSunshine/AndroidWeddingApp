package com.gemptc.wd.activities.home.invitation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.InvitationBean;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

public class InviteFirstFragment extends Fragment {
    private View view;
    private ImageView iv_invite_detail_bg,iv_invite_detail;
    private TextView tv_invite_xinlang,tv_invite_xinniang,tv_invite_time,tv_invite_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  View.inflate(getContext(), R.layout.fragment_invite_first,null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        x.image().bind(iv_invite_detail_bg, UrlAddress.MUBAN_IMAGE_ADDRESS+"invite_background.jpg");
        Intent intent = getActivity().getIntent();
        InvitationBean invite = (InvitationBean) intent.getSerializableExtra("inviteBean");
        String fromWho = intent.getStringExtra("fromWho");
        if ("模板".equals(fromWho)){
            x.image().bind(iv_invite_detail,invite.getInvate_imageName());
        }else{
            if (invite.getInvate_imageName()!=null) {
                x.image().bind(iv_invite_detail, UrlAddress.INVITE_IMAGE_ADDRESS + invite.getInvate_imageName());
            }else{
                x.image().bind(iv_invite_detail, UrlAddress.MUBAN_IMAGE_ADDRESS+"invite_moren.jpg");
            }
        }
        String xinlang = invite.getInvite_name1();
        String xinniang=invite.getInvite_name2();
        String time = invite.getInvite_time();
        String address = invite.getInvate_address();
        tv_invite_xinlang.setText(xinlang);
        tv_invite_xinniang.setText(xinniang);
        tv_invite_time.setText(time);
        tv_invite_address.setText(address);
    }

    private void initView() {
        iv_invite_detail_bg= (ImageView) view.findViewById(R.id.iv_invite_detail_bg);
        iv_invite_detail= (ImageView) view.findViewById(R.id.iv_invite_detail);
        tv_invite_xinlang= (TextView) view.findViewById(R.id.tv_invite_xinlang);
        tv_invite_xinniang= (TextView) view.findViewById(R.id.tv_invite_xinniang);
        tv_invite_time= (TextView) view.findViewById(R.id.tv_invite_time);
        tv_invite_address= (TextView) view.findViewById(R.id.tv_invite_address);
    }
}
