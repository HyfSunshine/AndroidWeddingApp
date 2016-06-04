package com.gemptc.wd.activities.social;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.PostAdapter;
import com.gemptc.wd.bean.Post;
import com.gemptc.wd.bean.PostBean;

import java.util.ArrayList;
import java.util.List;

public class SocialFannaojiActivity extends AppCompatActivity {
    //第一步：找数据
    List<PostBean> mList;
    //第二步：找到每行的视图
    //第三步：确定适配器
    PostAdapter mPostAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_fannaoji);
        Intent intent=getIntent();
        mListView= (ListView)findViewById(R.id.lv_fannaoji);
        initData();
        mPostAdapter=new PostAdapter(this,mList,4);
        mListView.setAdapter(mPostAdapter);
    }

    private void initData() {
    }
}
