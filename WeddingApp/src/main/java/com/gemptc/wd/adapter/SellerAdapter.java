package com.gemptc.wd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.home.SellerDetailActivity;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.x;

import java.util.List;

/**
 * Created by C5-0 on 2016/6/1.
 */
public class SellerAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    Context mContext;
    List<Seller> list;
     public SellerAdapter(Context context, List<Seller> list) {
        mContext = context;
        this.list = list;
        mInflater=LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    //缓存布局中的控件
    class ViewHolder{
        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //找到每一行的布局
        if (convertView == null){
            //说明是第一次绘制整屏列表，例如1-6行
            convertView =mInflater.inflate(R.layout.item_seller,null);
            //view=View.inflate(mContext,R.layout.item_seller,null);
            viewHolder = new ViewHolder();
            //初始化当前行布局中的所有控件
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.name_textview);
            viewHolder.relativeLayout= (RelativeLayout) convertView.findViewById(R.id.RLseller);
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);
        } else {
            //说明开始上下滑动，后面的所有行布局采用第一次绘制时的缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //动态修改每一行控件的内容
        final Seller seller =list.get(position);
        x.image().bind(viewHolder.imageView, UrlAddress.SELLER_IMAGE_ADDRESS+seller.getSellerPicName());
        /*viewHolder.imageView.setImageResource(seller.getSellerPicName());*/
        viewHolder.textView.setText(seller.getSellerName());
        //设置每一行item的单击事件
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到商家页面详情
                Intent intent = new Intent(mContext, SellerDetailActivity.class);
                intent.putExtra("sellerdata",list.get(position));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
