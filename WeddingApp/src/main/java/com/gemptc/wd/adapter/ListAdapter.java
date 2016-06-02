package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.SearchHistoryBean;

import java.util.List;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class ListAdapter extends BaseAdapter {
    List<SearchHistoryBean> mList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public ListAdapter(List<SearchHistoryBean> list, Context context) {
        mList = list;
        mContext = context;
        mLayoutInflater=LayoutInflater.from(mContext);
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
        ListViewHolder holder=null;
        if (convertView==null){
            holder=new ListViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.search_list_item,null);
            holder.text= (TextView) convertView.findViewById(R.id.tv_list_item);
            convertView.setTag(holder);
        }else{
            holder= (ListViewHolder) convertView.getTag();
        }
        SearchHistoryBean searchHistoryBean=mList.get(position);
        String content=searchHistoryBean.getContent();
        holder.text.setText(content);
        return convertView;
    }
    class ListViewHolder{
        TextView text;
    }
}
