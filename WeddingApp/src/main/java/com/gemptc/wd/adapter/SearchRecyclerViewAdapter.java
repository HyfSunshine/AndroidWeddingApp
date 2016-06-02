package com.gemptc.wd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.bean.SearchBean;

import java.util.List;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.EventRecycleViewHolder> implements View.OnClickListener{
    private List<SearchBean> mList;
    private Context mContext;

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public SearchRecyclerViewAdapter(List<SearchBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    //监听接口OnRecyclerViewItemClickListener

    public static interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view, SearchBean searchBean);

    }
    @Override
    public EventRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //定义一个view
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_postdetail_item,parent,false);
        EventRecycleViewHolder eventRecycleViewHolder=new EventRecycleViewHolder(view);

        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, int position) {
        holder.postTitle.setText(mList.get(position).getPostTitle());
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    //实现点击事件
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener!=null){
            mOnRecyclerViewItemClickListener.OnItemClick(v, (SearchBean) v.getTag());
        }


    }
    public class EventRecycleViewHolder extends RecyclerView.ViewHolder{

        TextView postTitle;
        public EventRecycleViewHolder(View itemView) {
            super(itemView);
            postTitle= (TextView) itemView.findViewById(R.id.social_posttitle);

        }
    }
}
