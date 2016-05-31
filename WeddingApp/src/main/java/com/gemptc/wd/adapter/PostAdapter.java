package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.wedding.R;
import com.gemptc.wd.bean.Post;

import java.util.List;

/**
 * Created by zhaozhifei on 2016/5/21.
 */
public class PostAdapter extends BaseAdapter{
    Context mContext;
    List<Post> mList;
    //layoutinflater主要是用来初始化布局文件，而findviewbyid主要用来初始化布局中的控件

    LayoutInflater mInflater;

    public PostAdapter(Context context, List<Post> list) {
        mContext = context;
        mList = list;
        mInflater=LayoutInflater.from(context);//初始化
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

    /**
     *
     * position表示当前显示的是第几行数据，从0开始
     * convertview表示当前显示的布局是哪个布局
     * parent包含当前行布局的父布局
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView=mInflater.inflate(R.layout.social_item_layout,null);
        //确定每一行布局控件中显示的内容
        //先把置顶的内容填充完
        LinearLayout mLinearLayout= (LinearLayout) convertView.findViewById(R.id.post_zhidinglayout);
        ImageView mPostimageView= (ImageView) convertView.findViewById(R.id.post_imageview_1);
        TextView mPosttextView= (TextView) convertView.findViewById(R.id.post_textview_1);
        mPosttextView.setText(mList.get(position).getPostTitle());


        //头像
        ImageView mPicImageView= (ImageView) convertView.findViewById(R.id.social_userpic);
        //用户名
        TextView mNameTextView= (TextView) convertView.findViewById(R.id.social_username);
        //发帖时间
        TextView mTimeTextView= (TextView) convertView.findViewById(R.id.social_posttime);
        //帖子标题
        TextView mTitleTextView= (TextView) convertView.findViewById(R.id.social_posttitle);

        //把内容填充到具体的布局中去
        mPicImageView.setImageResource(mList.get(position).getUserPicName());
        mNameTextView.setText(mList.get(position).getUserName());
        mTimeTextView.setText(mList.get(position).getPostTime());
        mTitleTextView.setText(mList.get(position).getPostTitle());

        //根据是否置顶修改显示部分
        if (mList.get(position).getZhiding()){
            mLinearLayout.setVisibility(View.VISIBLE);
            LinearLayout mfeiLinearLayout= (LinearLayout) convertView.findViewById(R.id.post_feilayout);
            mfeiLinearLayout.setVisibility(View.GONE);
        }
        return convertView;
    }
}
