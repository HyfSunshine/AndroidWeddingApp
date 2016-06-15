package com.gemptc.wd.activities.home.invitation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.home.invitation.InvitationListActivity;
import com.gemptc.wd.activities.home.invitation.InviteDetailActivity;
import com.gemptc.wd.bean.InvitationBean;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/12.
 */
public class InviteMuBanAdapter extends BaseAdapter{
    private Context context;
    private List<InvitationBean> invitationList;

    public InviteMuBanAdapter(Context context, List<InvitationBean> invitationList) {
        this.context = context;
        this.invitationList = invitationList;
    }
    @Override
    public int getCount() {
        return invitationList.size();
    }

    @Override
    public Object getItem(int position) {
        return invitationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.gridview_item_invite,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_gridView_item);
        TextView tv_invite_content = (TextView) convertView.findViewById(R.id.tv_invite_content);

        x.image().bind(imageView,invitationList.get(position).getInvate_imageName());
        tv_invite_content.setText(invitationList.get(position).getInvate_content());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InviteDetailActivity.class);
                intent.putExtra("inviteBean",invitationList.get(position));
                intent.putExtra("fromWho","模板");
                InvitationListActivity activity = (InvitationListActivity) context;
                activity.startActivityForResult(intent,InvitationListActivity.ADD_OK);
            }
        });
        return convertView;
    }
}
