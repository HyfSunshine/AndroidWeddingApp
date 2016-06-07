package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.android.wedding.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class SellerGridAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context mContext;
    List<String> mlist;
    public SellerGridAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.mlist = list;
        mInflater=LayoutInflater.from(mContext);
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
    class ViewHolder{
        TextView classfiy;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            //说明是第一次绘制整屏列表，例如1-6行
            convertView =mInflater.inflate(R.layout.seller_top_item,null);
            viewHolder = new ViewHolder();
            //初始化当前行布局中的所有控件
            viewHolder.classfiy= (TextView) convertView.findViewById(R.id.classfiytext);
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String title=mlist.get(position);
        viewHolder.classfiy.setText(title);
        return convertView;
    }
}
