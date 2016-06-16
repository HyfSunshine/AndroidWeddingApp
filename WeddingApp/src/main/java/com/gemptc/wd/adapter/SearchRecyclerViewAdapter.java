package com.gemptc.wd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.social.SocialHuiyiluDetailActivity;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.SearchBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.EventRecycleViewHolder> implements View.OnClickListener{
    private List<PostBean> mList;
    private Context mContext;

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private View view;

    public SearchRecyclerViewAdapter(List<PostBean> list, Context context) {
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_postdetail_item,parent,false);
        EventRecycleViewHolder eventRecycleViewHolder=new EventRecycleViewHolder(view);

        view.setOnClickListener(this);
        return eventRecycleViewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecycleViewHolder holder, final int position) {
        holder.postTitle.setText(mList.get(position).getPostTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SocialHuiyiluDetailActivity.class);
                intent.putExtra("posthuiyilu", mList.get(position));
                mContext.startActivity(intent);
            }
        });
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
