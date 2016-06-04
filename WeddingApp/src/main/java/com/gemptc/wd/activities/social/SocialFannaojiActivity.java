package com.gemptc.wd.activities.social;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.PostAdapter;
import com.gemptc.wd.bean.PostBean;
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

public class SocialFannaojiActivity extends AppCompatActivity {
    PostBean mPostBean;
    //第一步：找数据
    List<PostBean> mList;
    //第二步：找到每行的视图
    //第三步：确定适配器
    PostAdapter mPostAdapter;
    ListView mListView;
    int moduleType=4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_fannaoji);
        mList=new ArrayList<>();
        mListView= (ListView)findViewById(R.id.lv_fannaoji);
        mPostAdapter=new PostAdapter(this,mList,moduleType);
        mListView.setAdapter(mPostAdapter);
        String result = PrefUtils.getString(SocialFannaojiActivity.this, "fannaoji_post", null);
        if (result != null) {
            parseData(result);
        }
        getDatas();
    }

    //获取网络数据
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addQueryStringParameter("postop", "showType4");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据",result);
                PrefUtils.setString(SocialFannaojiActivity.this, "fannaoji_post", result);
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

//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo1.jpg");
//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo2.png");
//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo3.jpg");
    }

    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostBean>>() {
        }.getType();
        List<PostBean> list = gson.fromJson(result, type);

        mList.clear();
        for (int i = 0; i < list.size(); i++) {
            mList.add(list.get(i));
        }
        mPostAdapter.notifyDataSetChanged();
    }

        //先获取置顶部分的内容
//        Post post1=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",true);
//        Post post2=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",true);
//
//        //先获取非置顶部分的内容
//        Post post3=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",false);
//        Post post4=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post5=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",false);
//        Post post6=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post7=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",false);
//        Post post8=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post9=new Post(R.mipmap.u107,"良品铺子","1分钟前","婚礼来临的时刻",false);
//        Post post10=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//
//        mList.add(post1);
//        mList.add(post2);
//        mList.add(post3);
//        mList.add(post4);
//        mList.add(post5);
//        mList.add(post6);
//        mList.add(post7);
//        mList.add(post8);
//        mList.add(post9);
//        mList.add(post10);
    }
