package com.gemptc.wd.activities.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.mine.MineDetailActivity;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class FocusUserAdapter extends BaseAdapter{

    private List<UserBean> userList;
    private Context context;

    public FocusUserAdapter(Context context,List<UserBean> userList) {
        this.context=context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView=View.inflate(context, R.layout.mine_items_focus_listview,null);
            holder.iv_user_seller_pic= (ImageView) convertView.findViewById(R.id.iv_user_seller_pic);
            holder.tv_user_seller_name= (TextView) convertView.findViewById(R.id.tv_user_seller_name);
            holder.tv_user_seller_sign= (TextView) convertView.findViewById(R.id.tv_user_seller_sign);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        if (!"".equals(userList.get(position).getU_picname())){
            Glide.with(context).load(UrlAddress.USER_IMAGE_ADDRESS+userList.get(position).getU_picname()).into(holder.iv_user_seller_pic);
        }
        holder.tv_user_seller_name.setText(userList.get(position).getU_name());
        holder.tv_user_seller_sign.setText(userList.get(position).getU_sign());

//        点击用户进入用户的个人中心界面
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MineDetailActivity.class);
                intent.putExtra("user_id",userList.get(position).getU_id()+"");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private ImageView iv_user_seller_pic;
        private TextView tv_user_seller_name,tv_user_seller_sign;
    }
}
