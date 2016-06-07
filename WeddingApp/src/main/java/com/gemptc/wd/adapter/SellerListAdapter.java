package com.gemptc.wd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.home.SellerDetailActivity;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.UrlAddress;


import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class SellerListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    List<Seller> mlist;

    public SellerListAdapter(Context context, List<Seller> mlist) {
        this.mContext = context;
        this.mlist = mlist;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView name;
        ImageView pic;
        /*RelativeLayout relativeLayout;*/
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //找到每一行的布局
        if (convertView == null) {
            //说明是第一次绘制整屏列表，例如1-6行
            convertView = mInflater.inflate(R.layout.item_seller, null);
            viewHolder = new ViewHolder();
            //初始化当前行布局中的所有控件
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_textview);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.imageView);
/*
            viewHolder.relativeLayout= (RelativeLayout) convertView.findViewById(R.id.RLseller);
*/
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);

        } else {
            //说明开始上下滑动，后面的所有行布局采用第一次绘制时的缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

//
//        ImageView img;
        final Seller seller = mlist.get(position);
        //加载网络图片

        String imgUrl = UrlAddress.SELLER_IMAGE_ADDRESS + seller.getSellerPicName();
        Glide.with(mContext)
                .load(imgUrl)
                .thumbnail(0.5f)
                .into(viewHolder.pic);

        //viewHolder.pic.setImageResource(R.drawable.deng3);
        //  viewHolder.pic.setImageDrawable();
        viewHolder.name.setText(seller.getSellerName());
        convertView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, SellerDetailActivity.class);
        intent.putExtra("sellerdata",mlist.get(position));
        mContext.startActivity(intent);
    }
});
        return convertView;
    }
}
