package com.gemptc.wd.activities.social;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    ListView huiyiluListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        initView();
        replyList=new ArrayList<>();
        imagesUrlList=new ArrayList<>();
        Intent intent=getIntent();
        mPostBean= (PostBean) intent.getSerializableExtra("posthuiyilu");
        mPostDetailAdapter=new PostDetailAdapter(SocialHuiyiluDetailActivity.this,mPostBean,imagesUrlList,replyList);
        huiyiluListView.setAdapter(mPostDetailAdapter);

        String result = PrefUtils.getString(SocialHuiyiluDetailActivity.this, "huiyilu_ninepic_detail", null);
        if (result != null) {
            parseData(result);
        }
        getPostImagesDatas();
    }

    //获取网络数据
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

    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = gson.fromJson(result, type);
        imagesUrlList.clear();
        for (int i = 0; i < 4; i++) {
            imagesUrlList.add(UrlAddress.POST_IMAGE_ADDRESS+list.get(i));
        }
        mPostDetailAdapter.notifyDataSetChanged();
    }

    private void initView() {
        //初始化所有控件
        huiyiluListView= (ListView) this.findViewById(R.id.listview_detailview);
    }
}
