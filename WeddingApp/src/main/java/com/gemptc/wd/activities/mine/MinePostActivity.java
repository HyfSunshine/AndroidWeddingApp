package com.gemptc.wd.activities.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.mine.adapter.CollePostAdapter;
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

public class MinePostActivity extends AppCompatActivity {

    private ImageView iv_myPost_back;
    private ListView lv_MyPost;
    private List<PostBean> postList;
    private CollePostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_post);
        initView();
        initData();
        initListener();
        //判断是否有缓存
        String user_self_post = PrefUtils.getString(this,"user_self_post",null);
        if (user_self_post!=null){
            parseUserData(user_self_post);
        }
        getUserSelfData();
    }

    private void initView() {
        iv_myPost_back= (ImageView) findViewById(R.id.iv_mypost_back);
        lv_MyPost= (ListView) findViewById(R.id.lv_MyPost);
    }

    private void initData() {
        postList = new ArrayList<>();
        postAdapter = new CollePostAdapter(this,postList);
        lv_MyPost.setAdapter(postAdapter);
    }

    private void initListener() {
        iv_myPost_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //获取自己的帖子
    private void getUserSelfData() {
        RequestParams params = new RequestParams(UrlAddress.POST_Controller);
        //http://10.201.1.9:8080/WeddingJson/PostController?postop=getUserPost&userid=4
        params.addQueryStringParameter("postop","getUserPost");
        params.addQueryStringParameter("userid",PrefUtils.getString(this,"user_self_id",null));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //将自己帖子的信息存储起来
                PrefUtils.setString(MinePostActivity.this,"user_self_post",result);
                //解析数据
                parseUserData(result);
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

    private void parseUserData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostBean>>(){}.getType();
        List<PostBean> postBeanList = gson.fromJson(result,type);
        postList.clear();
        for (int i = 0; i < postBeanList.size(); i++) {
            postList.add(postBeanList.get(i));
        }
    }
}
