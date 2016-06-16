package com.gemptc.wd.activities.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.mine.MineDetailActivity;
import com.gemptc.wd.activities.social.SocialHuiyiluDetailActivity;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2016/6/8.
 */
public class CollePostAdapter extends BaseAdapter{

    private List<PostBean> postList;
    private Context context;
    private UserBean user;

    public CollePostAdapter(Context context, List<PostBean> postList) {
        this.context=context;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView=View.inflate(context, R.layout.mine_items_coll_post_listview,null);
            holder.iv_user_pic= (ImageView) convertView.findViewById(R.id.iv_user_pic);
            holder.tv_user_name= (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_user_postName= (TextView) convertView.findViewById(R.id.tv_user_post);
            holder.tv_user_posttime= (TextView) convertView.findViewById(R.id.tv_user_posttime);
            convertView.setTag(holder);
            //设置头像(传入holder和用户id)
            setUserPic(holder,postList.get(position).getUserID(),position);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        //设置用户名字
        holder.tv_user_name.setText(postList.get(position).getUserName());
        //设置帖子标题
        holder.tv_user_postName.setText(postList.get(position).getPostTitle());
        //设置帖子发布时间
        holder.tv_user_posttime.setText(postList.get(position).getPostTime());

//        点击帖子进入帖子详情界面
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context,MineDetailActivity.class));
                //ToastUtils.shortToast(context,"您点击了第【"+position+"】条帖子");
                Intent intent = new Intent(context,SocialHuiyiluDetailActivity.class);
                intent.putExtra("posthuiyilu",postList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    //获取用户头像的方法
    private void setUserPic(final ViewHolder holder, int userid, final int position) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","detailsuser");
        params.addQueryStringParameter("userid",userid+"");
        //internetCode="获取别人数据";
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析获得的数据
                Gson gson = new Gson();
                Type type = new TypeToken<UserBean>() {
                }.getType();
                user = gson.fromJson(result, type);
                //3.设置控件的值
                if (!"".equals(user.getU_picname())){
                    Glide.with(context).load(UrlAddress.USER_IMAGE_ADDRESS+user.getU_picname()).into(holder.iv_user_pic);
                }
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

    class ViewHolder{
        private ImageView iv_user_pic;
        private TextView tv_user_name,tv_user_postName,tv_user_posttime;
    }
}
