package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.GridBean;

import java.util.List;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class GridAdapter extends BaseAdapter{
    //上下文对象
    private Context mContext;
    List<GridBean> mList;
    LayoutInflater mInflater;

    public GridAdapter(Context context, List<GridBean> list) {
        mContext = context;
        mList = list;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new GridViewHolder();
            convertView=mInflater.inflate(R.layout.search_grid_item,null);
            viewHolder.text= (TextView) convertView.findViewById(R.id.tv_grid_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (GridViewHolder) convertView.getTag();
        }
        GridBean gridBean=mList.get(position);
        String content=gridBean.getText();
        viewHolder.text.setText(content);
        return convertView;
    }

    private class GridViewHolder {
        TextView text;
    }
}
