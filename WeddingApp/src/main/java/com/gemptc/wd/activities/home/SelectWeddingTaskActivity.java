package com.gemptc.wd.activities.home;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.TotalTaskAdapter;
import com.gemptc.wd.bean.TaskBean;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectWeddingTaskActivity extends AppCompatActivity {
   private ListView mListView;
   private List<TaskBean> mTaskBeanList=new ArrayList<>();
   private TotalTaskAdapter mTotalTaskAdapter;
    private Button mBtnAddTask;
    private List<TaskBean> mMyTaskList=new ArrayList<>();
    private HashMap<Integer,Boolean> mIsChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wedding_task);
        Intent intent=getIntent();
        mMyTaskList= (List<TaskBean>) intent.getSerializableExtra("mytask");
        initView();
        initData();
        initListener();
        mTotalTaskAdapter=new TotalTaskAdapter(mTaskBeanList,SelectWeddingTaskActivity.this);
        mListView.setAdapter(mTotalTaskAdapter);
        //获取每个item中checkbox的状态并显示未被选中的item
    }

    private void initListener() {
        //用回调方法把用户选择的任务传到，home的任务界面里，并在那里刷新
        mBtnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取自己选择的任务
                mIsChecked=mTotalTaskAdapter.getIsSelected();
                List<TaskBean> addTaskList=new ArrayList<>();
                for(int i=0;i<mIsChecked.size();i++){
                    if(mIsChecked.get(i)){
                        addTaskList.add(mTaskBeanList.get(i));
                    }
                }
                //上传我的任务
                if(addTaskList.size()!=0){
                    for(int i=0;i<addTaskList.size();i++){
                        upload(addTaskList.get(i).getTaskName());
                    }
                }
                //调回
                Intent intent=new Intent(SelectWeddingTaskActivity.this,HomeWeddingTaskActivity.class);
                intent.putExtra("addMyTask", (Serializable) addTaskList);
                startActivityForResult(intent,202);
                finish();
            }
        });

    }

    private void initView() {
        mListView= (ListView) findViewById(R.id.total_Task_listView);
        mBtnAddTask= (Button) findViewById(R.id.btn_add_task);
    }

    private void initData() {
        TaskBean task1=new TaskBean("双方父母见面商议结婚相关事宜");
        TaskBean task2=new TaskBean("为拍摄前准备，保养皮肤");
        TaskBean task3=new TaskBean("挑选拍婚纱照需要的礼服");
        TaskBean task4=new TaskBean("拍婚纱照");
        TaskBean task5=new TaskBean("拿照片");
        TaskBean task6=new TaskBean("选婚庆公司");
        TaskBean task7=new TaskBean("选定租或借婚车");
        TaskBean task8=new TaskBean("选定娘伴郎礼服");
        TaskBean task9=new TaskBean("选定双方父母礼服");
        TaskBean task10=new TaskBean("选定婚庆公司");
        TaskBean task11=new TaskBean("选司仪");
        TaskBean task12=new TaskBean("选定伴娘伴郎");
        TaskBean task13=new TaskBean("选定花童");
        TaskBean task14=new TaskBean("预定婚宴酒店");
        TaskBean task15=new TaskBean("确定参加婚礼的好友名单");
        TaskBean task16=new TaskBean("购买喜字·红包·喜帖·喜糖·喜烟·喜酒等");
        TaskBean task17=new TaskBean("发喜糖");
        TaskBean task18=new TaskBean("发请帖");
        TaskBean task19=new TaskBean("准备双方及双方父母的发言稿");
        TaskBean task20=new TaskBean("准备蜜月行李物品");
        TaskBean task21=new TaskBean("安排蜜月行程");
        TaskBean task22=new TaskBean("预定蜜月酒店");
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
        mTaskBeanList.add(task21);
        mTaskBeanList.add(task22);
        //先与home界面中传来的任务作比较，把已添加的任务去掉，然后显示出现在的集合
        for(int i=0;i<mMyTaskList.size();i++){
            for(int j=0;j<mTaskBeanList.size();j++){
                if((mMyTaskList.get(i).getTaskName()).equals(mTaskBeanList.get(j).getTaskName())){
                    mTaskBeanList.remove(j);
                }
            }
        }

    }
    private void upload(String taskName){
        RequestParams params=new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","addtask");
        params.addBodyParameter("userid",""+4);
        params.addBodyParameter("taskname",taskName);
        params.addBodyParameter("taskdescription","");
        params.addBodyParameter("tasktime","");
    }
    public void editTask(View view) {
        Intent intent=new Intent(SelectWeddingTaskActivity.this,EditTaskActivity.class);
        startActivity(intent);
    }
}
