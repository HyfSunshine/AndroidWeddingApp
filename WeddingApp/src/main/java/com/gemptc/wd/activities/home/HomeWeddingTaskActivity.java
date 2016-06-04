package com.gemptc.wd.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.MyTaskAdapter;
import com.gemptc.wd.bean.TaskBean;

import java.util.ArrayList;
import java.util.List;

public class HomeWeddingTaskActivity extends AppCompatActivity {
    ListView mListView;
    List<TaskBean> mTaskBeanList;
    MyTaskAdapter mMyTaskAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wedding_task);
        Intent intent=getIntent();
        mListView= (ListView) findViewById(R.id.my_task_listView);
        initData();
        mMyTaskAdapter=new MyTaskAdapter(mTaskBeanList,HomeWeddingTaskActivity.this);
        mListView.setAdapter(mMyTaskAdapter);
    }

    private void initData() {
        mTaskBeanList=new ArrayList<>();
        TaskBean task1=new TaskBean("买喜糖");
        TaskBean task2=new TaskBean("挑礼服");
        TaskBean task3=new TaskBean("发喜帖");
        TaskBean task4=new TaskBean("拍婚纱照");
        TaskBean task5=new TaskBean("买婚礼用品");
        TaskBean task6=new TaskBean("订酒店");
        TaskBean task7=new TaskBean("试婚纱");
        TaskBean task8=new TaskBean("挑伴娘礼服");
        TaskBean task9=new TaskBean("挑伴郎礼服");
        TaskBean task10=new TaskBean("选婚庆公司");
        TaskBean task11=new TaskBean("选司仪");
        TaskBean task12=new TaskBean("拿照片");
        TaskBean task13=new TaskBean("买喜糖");
        TaskBean task14=new TaskBean("挑礼服");
        TaskBean task15=new TaskBean("发喜帖");
        TaskBean task16=new TaskBean("拍婚纱照");
        TaskBean task17=new TaskBean("买婚礼用品");
        TaskBean task18=new TaskBean("订酒店");
        TaskBean task19=new TaskBean("试婚纱");
        TaskBean task20=new TaskBean("挑伴娘礼服");
        mTaskBeanList.add(task1);
        mTaskBeanList.add(task2);
        mTaskBeanList.add(task3);
        mTaskBeanList.add(task5);
        mTaskBeanList.add(task7);
        mTaskBeanList.add(task8);
        mTaskBeanList.add(task9);
        mTaskBeanList.add(task10);
        mTaskBeanList.add(task11);
        mTaskBeanList.add(task12);
        mTaskBeanList.add(task13);
        mTaskBeanList.add(task14);
        mTaskBeanList.add(task15);
        mTaskBeanList.add(task16);
        mTaskBeanList.add(task17);
        mTaskBeanList.add(task18);
        mTaskBeanList.add(task19);
        mTaskBeanList.add(task20);

    }

    public void addTask(View view) {
        Intent intent=new Intent(HomeWeddingTaskActivity.this,SelectWeddingTaskActivity.class);
        startActivity(intent);
        Intent intent1=getIntent();
    }

    public void editTask(View view) {
        Intent intent=new Intent(HomeWeddingTaskActivity.this,EditTaskActivity.class);
        startActivity(intent);
    }
}
