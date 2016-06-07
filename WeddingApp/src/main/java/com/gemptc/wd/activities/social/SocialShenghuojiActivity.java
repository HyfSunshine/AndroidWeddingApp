package com.gemptc.wd.activities.social;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.bean.PostBean;

import java.util.List;

public class SocialShenghuojiActivity extends AppCompatActivity {
    //第一步：找数据
    List<PostBean> mList;
    //第二步：找到每行的视图
    //第三步：确定适配器

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_shenghuoji);
        Intent intent=getIntent();
        mListView= (ListView)findViewById(R.id.lv_shenghuoji);
        initData();

        //mListView.setAdapter(mPostAdapter);
    }

    private void initData() {
//        mList=new ArrayList<>();
//        //先获取置顶部分的内容
//        Post post1=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",true);
//        Post post2=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",true);
//
//        //先获取非置顶部分的内容
//        Post post3=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post4=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post5=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post6=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post7=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post8=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
//        Post post9=new Post(R.mipmap.u107,"小白兔","三小时前","大家聊聊自己的爱好",false);
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
}
