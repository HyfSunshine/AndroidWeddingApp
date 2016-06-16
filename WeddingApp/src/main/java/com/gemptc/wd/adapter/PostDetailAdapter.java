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
import com.bumptech.glide.Glide;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.ReplyBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.ClickNineGridViewAdapter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
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
    public PostDetailAdapter(Context context, PostBean postBean,List<String> imagesUrlList,List<ReplyBean> replyList) {
        mContext = context;
        mPostBean = postBean;
        this.imagesUrlList = imagesUrlList;
        this.replyList=replyList;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return replyList.size();
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
            //加载不同的板块
            viewHolder.post_huiyilulayout= (LinearLayout) convertView.findViewById(R.id.post_layout);
            viewHolder.post_jinxingcelayout= (LinearLayout) convertView.findViewById(R.id.post_layout);
            viewHolder.post_shenghuolayout= (LinearLayout) convertView.findViewById(R.id.post_layout);
            viewHolder.post_fannaojilayout= (LinearLayout) convertView.findViewById(R.id.post_layout);
            //加载主贴的内容
            viewHolder.userPic_imageview= (ImageView) convertView.findViewById(R.id.userPic);
            viewHolder.userName_textview= (TextView) convertView.findViewById(R.id.userName);
            viewHolder.postTime_textview= (TextView) convertView.findViewById(R.id.postTime);
            //楼层数
            viewHolder.position_textview= (TextView) convertView.findViewById(R.id.position);
            viewHolder.postContent_textview= (TextView) convertView.findViewById(R.id.postContent);
            //加载上传的图片
            viewHolder.nineGridView= (NineGridView) convertView.findViewById(R.id.gridview_imageview);

            getUserPic(replyList.get(position).getUserID(),viewHolder.userPic_imageview);
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);
        }else {
            //说明开始上下滑动，后面的所有行布局采用第一次绘制时的缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //判断图片加载的位置，主贴还是回复
        if (position==0) {
            viewHolder.userName_textview.setText(mPostBean.getUserName());
            viewHolder.postTime_textview.setText(mPostBean.getPostTime());
            viewHolder.position_textview.setText("1");
            viewHolder.postContent_textview.setText(mPostBean.getPostContent());

            //使用框架去加载主贴图片
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
            notifyDataSetChanged();
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
        return convertView;
    }

    private void getUserPic(int userID, final ImageView userPic_imageview) {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"UserController");
        params.addBodyParameter("userop","detailsuser");
        params.addBodyParameter("userid",userID+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<UserBean>(){}.getType();
                UserBean userbean=gson.fromJson(result,type);
                Glide.with(mContext).load(UrlAddress.USER_IMAGE_ADDRESS+userbean.getU_picname()).into(userPic_imageview);
                notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //缓存布局中的控件
    class ViewHolder{
        LinearLayout post_huiyilulayout,post_jinxingcelayout,post_shenghuolayout,post_fannaojilayout;
        TextView  userName_textview,postTime_textview,position_textview,postContent_textview;
        //用户头像
        ImageView userPic_imageview;
        NineGridView nineGridView;
    }
}

