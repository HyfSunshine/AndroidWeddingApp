package com.gemptc.wd.activities.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.mine.MineDetailActivity;
import com.gemptc.wd.bean.SellerBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class FocusSellerAdapter extends BaseAdapter{

    private List<SellerBean> sellerList;
    private Context context;

    public FocusSellerAdapter(Context context, List<SellerBean> sellerList) {
        this.context=context;
        this.sellerList = sellerList;
    }

    @Override
    public int getCount() {
        return sellerList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellerList.get(position);
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

        Glide.with(context).load(UrlAddress.SELLER_IMAGE_ADDRESS+sellerList.get(position).getSellerPicName()).into(holder.iv_user_seller_pic);
        holder.tv_user_seller_name.setText(sellerList.get(position).getSellerName());
        holder.tv_user_seller_sign.setText(sellerList.get(position).getSellerSign());

//        点击商家进入商家的详情页面
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.shortToast(context,"您点击进入了商家的详情页面");
            }
        });
        return convertView;
    }

    class ViewHolder{
        private ImageView iv_user_seller_pic;
        private TextView tv_user_seller_name,tv_user_seller_sign;
    }
}
