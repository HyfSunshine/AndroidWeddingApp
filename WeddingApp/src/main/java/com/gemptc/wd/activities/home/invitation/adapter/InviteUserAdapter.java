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
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/6/12.
 */
public class InviteUserAdapter extends BaseAdapter{
    private Context context;
    private List<InvitationBean> invitationList;

    public InviteUserAdapter(Context context, List<InvitationBean> invitationList) {
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

        if (invitationList.get(position).getInvate_imageName()!=null) {
            x.image().bind(imageView, UrlAddress.INVITE_IMAGE_ADDRESS + invitationList.get(position).getInvate_imageName());
        }else{
            x.image().bind(imageView, UrlAddress.MUBAN_IMAGE_ADDRESS+"invite_moren.jpg");
        }
        tv_invite_content.setText(invitationList.get(position).getInvate_content());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,InviteDetailActivity.class);
                intent.putExtra("inviteBean",invitationList.get(position));
                intent.putExtra("fromWho","用户");
                InvitationListActivity activity = (InvitationListActivity) context;
                activity.startActivityForResult(intent,InvitationListActivity.ADD_OK);
            }
        });

        //长按进行删除此请帖的操作
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String userid = invitationList.get(position).getUser_id()+"";
                final String inviteid = invitationList.get(position).getInvite_id()+"";
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("删除").setContentText("确定要删除此请帖？").setCancelText("取消删除")
                        .setConfirmText("确认删除").showCancelButton(true).setConfirmClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                                sweetAlertDialog.setTitleText("正在删除...");
                                RequestParams params = new RequestParams(UrlAddress.USER_Controller);
                                params.addBodyParameter("userop","deleteInvite");
                                params.addBodyParameter("userid",userid);
                                params.addBodyParameter("inviteid",inviteid);
                                x.http().post(params, new Callback.CommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        sweetAlertDialog.setTitleText("删除成功").showCancelButton(false).setCancelClickListener(null)
                                                .showContentText(false).setConfirmText("OK")
                                                .setConfirmClickListener(null);
                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                InvitationListActivity activity = (InvitationListActivity) context;
                                                activity.getInviteData();
                                                sweetAlertDialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Throwable ex, boolean isOnCallback) {
                                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        sweetAlertDialog.setTitleText("删除失败").setContentText("请检查您的网络")
                                                .showCancelButton(false).setCancelClickListener(null)
                                                .showContentText(false)
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null);
                                    }

                                    @Override
                                    public void onCancelled(CancelledException cex) {

                                    }

                                    @Override
                                    public void onFinished() {

                                    }
                                });
                            }
                        }
                );
                sweetAlertDialog.show();

                return true;
            }
        });
        return convertView;
    }
}
