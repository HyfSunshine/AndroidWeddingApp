package com.gemptc.wd.activities.social;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.PostAdapter;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.fragments.FragmentSocial;
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

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SocialHuiyiluActivity extends AppCompatActivity {
    PostBean mPostBean;
    //第一步：找数据
    List<PostBean> mList;
    //第二步：找到每行的视图
    //第三步：确定适配器
    PostAdapter mPostAdapter;
    ListView mListView;
    int moduleType=1;
    ImageButton huiyilu_edit_post;
    //加载帖子列表
    private SweetAlertDialog LoadingPostDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_huiyilu);
        mList=new ArrayList<>();
        mListView= (ListView)findViewById(R.id.lv_huiyilu);
        mPostAdapter=new PostAdapter(this,mList,moduleType);
        mListView.setAdapter(mPostAdapter);
        initView();
        initListener();

        String result = PrefUtils.getString(SocialHuiyiluActivity.this, "huiyilu_post", null);
        if (result != null) {
            parseData(result);
        }
        getDatas();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            LoadingPostDialog=null;
            LoadingPostDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
            LoadingPostDialog.setTitleText("正在刷新").show();
            LoadingPostDialog.setCancelable(true);
            getDatas();
        }
    }

    private void initView() {
        huiyilu_edit_post= (ImageButton) findViewById(R.id.huiyilu_edit_post);

    }

    private void initListener() {
       HuiyiluListener listener = new HuiyiluListener();
        huiyilu_edit_post.setOnClickListener(listener);
    }

    //获取网络数据
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addQueryStringParameter("postop", "showType1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据",result);
                PrefUtils.setString(SocialHuiyiluActivity.this, "huiyilu_post", result);
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
                LoadingPostDialog.dismiss();
            }
        });
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



    public void huiyilu_back(View view) {
        finish();
    }

    private class HuiyiluListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.huiyilu_edit_post:
                    startActivityForResult(new Intent(SocialHuiyiluActivity.this,EditPostActivity.class),100);
                    break;
            }
        }
    }
}


