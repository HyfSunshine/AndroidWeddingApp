package com.gemptc.wd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.wedding.R;

import com.gemptc.wd.activities.social.SocialFannaojiDetailActivity;

import com.gemptc.wd.activities.social.SocialHuiyiluDetailActivity;

import com.gemptc.wd.activities.social.SocialJinxingceDetailActivity;

import com.gemptc.wd.activities.social.SocialShenghuojiDetailActivity;
import com.gemptc.wd.bean.PostBean;

import java.util.List;

/**
 * Created by zhaozhifei on 2016/5/21.
 */
public class PostAdapter extends BaseAdapter{
    int moduleType;
    Context mContext;
    List<PostBean> mList;
    //layoutinflater主要是用来初始化布局文件，而findviewbyid主要用来初始化布局中的控件
    LayoutInflater mInflater;

    public PostAdapter(Context context, List<PostBean> list, int moduleType) {
        mContext = context;
        mList = list;
        this.moduleType = moduleType;
        mInflater=LayoutInflater.from(mContext);
    }


//    public PostAdapter(Context context, List<PostBean> list) {
//        mContext = context;
//        mList = list;
//        mInflater=LayoutInflater.from(context);//初始化
//    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView=mInflater.inflate(R.layout.social_item_layout,null);
        //确定每一行布局控件中显示的内容
        //头像
       // ImageView mPicImageView= (ImageView) convertView.findViewById(R.id.social_userpic);
        //用户名
        TextView mNameTextView= (TextView) convertView.findViewById(R.id.userName);
        //发帖时间
        TextView mTimeTextView= (TextView) convertView.findViewById(R.id.postTime);
        //帖子标题
        TextView mTitleTextView= (TextView) convertView.findViewById(R.id.postTitle);
        //帖子回复
        TextView mReplyNum= (TextView) convertView.findViewById(R.id.replyNum);

        //把内容填充到具体的布局中去
       // mPicImageView.setImageResource(mList.get(position).getUserPicName());
        mNameTextView.setText(mList.get(position).getUserName());
        mTimeTextView.setText(mList.get(position).getPostTime());
        mTitleTextView.setText(mList.get(position).getPostTitle());
        mReplyNum.setText(""+mList.get(position).getReplyNum());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (moduleType){
                    case 1:
                        Intent intent1=new Intent(mContext, SocialHuiyiluDetailActivity.class);
                        intent1.putExtra("posthuiyilu",mList.get(position));
                        mContext.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2=new Intent(mContext, SocialJinxingceDetailActivity.class);
                        intent2.putExtra("postjinxingce",mList.get(position));
                        mContext.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3=new Intent(mContext, SocialShenghuojiDetailActivity.class);
                        intent3.putExtra("postshenghuoji",mList.get(position));
                        mContext.startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4=new Intent(mContext, SocialFannaojiDetailActivity.class);
                        intent4.putExtra("postfannaoji",mList.get(position));
                        mContext.startActivity(intent4);
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }

}
