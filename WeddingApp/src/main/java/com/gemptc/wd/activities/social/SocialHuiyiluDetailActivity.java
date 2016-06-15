package com.gemptc.wd.activities.social;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.adapter.PostDetailAdapter;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.ReplyBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SocialHuiyiluDetailActivity extends AppCompatActivity {

    PostBean mPostBean;
    //定义视图
    View mHuiyiluDetailView;
    //定义适配器
    PostDetailAdapter mPostDetailAdapter;
    //回复集合
    List<ReplyBean> replyList;
    //发帖图片的集合
    List<String> imagesUrlList;

    //回忆录的listview
    ListView huiyiluListView;

    //回复按钮
    Button btn_replypost;
    //回复编辑框
    EditText et_replypost;
    //用户头像
    ImageView iv_userpic;

    //回复成功弹出框
    SweetAlertDialog replyDialog;

    //定义帖子详情的headview
    TextView tv_headviewTitle;

    //自定义的弹出框类
    AddMenuPopupWindow mMenuPopupWindow;
    //三个点
    ImageButton ib_addmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        initView();
        initListeners();
        replyList=new ArrayList<>();
        imagesUrlList=new ArrayList<>();
        Intent intent=getIntent();
        mPostBean= (PostBean) intent.getSerializableExtra("posthuiyilu");
        tv_headviewTitle.setText(mPostBean.getPostTitle());
        mPostDetailAdapter=new PostDetailAdapter(SocialHuiyiluDetailActivity.this,mPostBean,imagesUrlList,replyList);
        huiyiluListView.setAdapter(mPostDetailAdapter);

        //获取帖子内容
        String result = PrefUtils.getString(SocialHuiyiluDetailActivity.this, "huiyilu_ninepic_detail", null);
        if (result != null) {
            parseData(result);
        }
        //获取主贴的图片
        getPostImagesDatas();

        //获取回复贴的内容
        String results=PrefUtils.getString(SocialHuiyiluDetailActivity.this,"huiyilu_replypost",null);
        if (results!=null){
            parseDatas(results);
        }
        getReplyPostDatas();


    }




    //获取网络上的回复贴数据
    private void getReplyPostDatas() {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addQueryStringParameter("postop","getreply");
        params.addBodyParameter("postid",""+mPostBean.getPostID());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String results) {
                PrefUtils.setString(SocialHuiyiluDetailActivity.this,"huiyilu_replypost",null);
                parseDatas(results);
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

    //解析从缓存里读取的回复内容
    private void parseDatas(String results) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ReplyBean>>() {
        }.getType();
        List<ReplyBean> list = gson.fromJson(results, type);

        replyList.clear();
        for (int i = 0; i < list.size(); i++) {
            replyList.add(list.get(i));
        }
        mPostDetailAdapter.notifyDataSetChanged();
    }


    //上传回复内容数据
    private void uploadreplyData() {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addBodyParameter("postop","reply");
        params.addBodyParameter("postid",""+mPostBean.getPostID());
        params.addBodyParameter("userid","4");
        params.addBodyParameter("replycontent",et_replypost.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据","回复成功");
                replyDialog=null;
                replyDialog=new SweetAlertDialog(SocialHuiyiluDetailActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                replyDialog.setTitleText("上传成功").show();
                replyDialog.setCancelable(true);
                //通知适配器刷新界面
                mPostDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("回复错误",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                replyDialog.dismiss();
                Log.e("回复结束","提交完成");
            }
        });
    }

    //帖子回复按钮的监听事件
    private void initListeners() {
        btn_replypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sendreplyPost:
                        //上传回复内容
                        Log.e("点击了回复按钮","1234");
                        uploadreplyData();
                }
            }
        });
        //对三个点进行监听，点击弹出自定义的弹出框
        ib_addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化AddMenuPopupWindow
                mMenuPopupWindow=new AddMenuPopupWindow(SocialHuiyiluDetailActivity.this,itemsOnClick);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                mMenuPopupWindow.showAtLocation(SocialHuiyiluDetailActivity.this.findViewById(R.id.ll_addmenu),Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        //点击用户头像的点击事件
//        iv_userpic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.userPic:
//                        Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了用户头像", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private View.OnClickListener itemsOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMenuPopupWindow.dismiss();
            switch (v.getId()){
                case R.id.wxShare:
                    Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了微信分享", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qqShare:
                    Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了QQ分享", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.weiboShare:
                    Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了微博分享", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_addmenupopupwindow_Collection:
                    uploadCollectPost();
                    Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了收藏", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_addmenupopupwindow_Cancel:
                    Toast.makeText(SocialHuiyiluDetailActivity.this, "你点击了退出", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    //收藏帖子
    private void uploadCollectPost() {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addBodyParameter("postop","collection");
        params.addBodyParameter("userid",""+mPostBean.getUserID());
        params.addBodyParameter("postid",""+mPostBean.getPostID());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                new SweetAlertDialog(SocialHuiyiluDetailActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("收藏帖子成功!")
                        .show();
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

    //从网络获取主贴图片数据
    private void getPostImagesDatas() {
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addQueryStringParameter("postop", "getpostpic");
        params.addQueryStringParameter("postid", ""+mPostBean.getPostID());

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据",result);
                PrefUtils.setString(SocialHuiyiluDetailActivity.this, "huiyilu_ninepic_detail", result);
                Log.e("数据", "456");
                parseData(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("数据", ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
                Log.e("数据","请求完成");
            }
        });
    }

    //解析从网络下载的图片数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = gson.fromJson(result, type);
        imagesUrlList.clear();
        for (int i = 0; i < list.size(); i++) {
            imagesUrlList.add(UrlAddress.POST_IMAGE_ADDRESS+list.get(i));
        }
        mPostDetailAdapter.notifyDataSetChanged();
    }

    private void initView() {
        //初始化所有控件
        huiyiluListView= (ListView) this.findViewById(R.id.listview_detailview);
        btn_replypost= (Button) this.findViewById(R.id.sendreplyPost);
        et_replypost= (EditText) this.findViewById(R.id.et_replypost);
        //找到用户头像的id
        iv_userpic= (ImageView) this.findViewById(R.id.userPic);
        //找到headview
        LinearLayout ll_headview= (LinearLayout) getLayoutInflater().inflate(R.layout.detail_post_headerview,null);
        //添加headview到详情界面中
        huiyiluListView.addHeaderView(ll_headview);
        //帖子详情的headview的id
        tv_headviewTitle= (TextView) this.findViewById(R.id.displayTitle);

        //找到addmenu的id
        ib_addmenu= (ImageButton) this.findViewById(R.id.post_addMenu);
    }

    public void detail_post_back(View view) {
        finish();
    }
}
