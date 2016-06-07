package com.gemptc.wd.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.social.SocialHuiyiluDetailActivity;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.ReplyBean;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ClickNineGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaozhifei on 2016/6/3.
 */
public class PostDetailAdapter extends BaseAdapter {
    //初始化布局的类，可以找到layout文件夹中所有的布局
    private LayoutInflater mInflater;
    Context mContext;
    private List<ReplyBean> replyList;
    List<String> imagesUrlList;
    PostBean mPostBean;

    public PostDetailAdapter(Context context, PostBean postBean, List<String> imagesUrlList,List<ReplyBean> replyList) {
        mContext = context;
        mPostBean = postBean;
        this.imagesUrlList = imagesUrlList;
        this.replyList=replyList;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return replyList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return replyList.get(position);
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
        ViewHolder viewHolder;
        //找到每一行的布局
        if (convertView==null){
            //说明是第一次绘制整屏的列表
            convertView=mInflater.inflate(R.layout.detail_post_item,null);
            viewHolder=new ViewHolder();
            //初始化当前布局中的控件
            viewHolder.post_huiyilulayout= (LinearLayout) convertView.findViewById(R.id.post_huiyilulayout);
            //viewHolder.userPic_imageview= (ImageView) convertView.findViewById(R.id.userPic);
            viewHolder.userName_textview= (TextView) convertView.findViewById(R.id.userName);
            viewHolder.postTime_textview= (TextView) convertView.findViewById(R.id.postTime);
            //楼层数
            viewHolder.position_textview= (TextView) convertView.findViewById(R.id.position);
            viewHolder.postContent_textview= (TextView) convertView.findViewById(R.id.postContent);
            //加载上传的图片
            viewHolder.nineGridView= (NineGridView) convertView.findViewById(R.id.gridview_imageview);
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);
        }else {
            //说明开始上下滑动，后面的所有行布局采用第一次绘制时的缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position==0){
            viewHolder.userName_textview.setText(mPostBean.getUserName());
            viewHolder.postTime_textview.setText(mPostBean.getPostTime());
            viewHolder.position_textview.setText((position+1)+"");
            viewHolder.postContent_textview.setText(mPostBean.getPostContent());
        }else {
            //动态的修改每一行控件的内容
            ReplyBean reply=replyList.get(position);
            //获取用户头像
            //viewHolder.userPic_imageview.setImageResource(postBean.);
            viewHolder.userName_textview.setText(reply.getUserName());
            viewHolder.postTime_textview.setText(reply.getReplyTime());
            viewHolder.position_textview.setText((position+1)+"");
            viewHolder.postContent_textview.setText(reply.getReplyContent());
        }

        //使用框架去加载图片
        ArrayList<ImageInfo> imageInfo=new ArrayList<>();
       if (imagesUrlList!=null){
           for (String url:imagesUrlList) {
               ImageInfo info = new ImageInfo();
               info.setThumbnailUrl(url);
               info.setBigImageUrl(url);
               imageInfo.add(info);
           }
       }
        //调用框架的适配器
        viewHolder.nineGridView.setAdapter(new ClickNineGridViewAdapter(mContext,imageInfo));

        //



        return convertView;
    }


    //缓存布局中的控件
    class ViewHolder{
        //ImageView userPic_imageview;
        LinearLayout post_huiyilulayout;
        TextView userName_textview,postTime_textview,position_textview,postContent_textview;
        NineGridView nineGridView;
    }
}
