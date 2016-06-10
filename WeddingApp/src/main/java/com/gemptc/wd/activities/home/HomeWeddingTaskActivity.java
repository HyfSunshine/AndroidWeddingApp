package com.gemptc.wd.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.MyTaskAdapter;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.TaskBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeWeddingTaskActivity extends AppCompatActivity {
   private ListView mListView;
   private List<TaskBean> mTaskBeanList=new ArrayList<>();
  private   MyTaskAdapter mMyTaskAdapter;
   private ImageView mEmptyImgView;
    private List<TaskBean> mAddTask=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wedding_task);
        initView();
        downloadTask();
        mMyTaskAdapter=new MyTaskAdapter(mTaskBeanList,HomeWeddingTaskActivity.this);
        mListView.setAdapter(mMyTaskAdapter);
    }


    private void initView() {
        mListView= (ListView) findViewById(R.id.my_task_listView);
        mEmptyImgView= (ImageView) findViewById(R.id.emptyImgView);

    }

    private void downloadTask() {
       /* TaskBean task1=new TaskBean("买喜糖");
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
        mTaskBeanList.add(task4);
        mTaskBeanList.add(task5);
        mTaskBeanList.add(task6);
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
*/
        //从网络上下载数据
        String result = PrefUtils.getString(HomeWeddingTaskActivity.this,"usertasklist",null);
        if (result != null) {
            parseData(result);
        }
        getDatas();
        if(mTaskBeanList.size()==0){
            mEmptyImgView.setVisibility(View.VISIBLE);
        }
    }
    //请求服务器，获取我的任务
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop", "taskList");
        params.addBodyParameter("userid",""+4);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PrefUtils.setString(HomeWeddingTaskActivity.this,"usertasklist", result);
                parseData(result);
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
    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<TaskBean>>() {
        }.getType();
        List<TaskBean> taskBeanList= gson.fromJson(result, type);
        mTaskBeanList.clear();
        for (int i = 0; i < taskBeanList.size(); i++) {
            mTaskBeanList.add(taskBeanList.get(i));
            /*Log.e("数据456",sellerList.get(i).toString());*/
        }
        mMyTaskAdapter.notifyDataSetChanged();
    }
//跳转到添加任务界面
    public void addTask(View view) {
        Intent intent=new Intent(HomeWeddingTaskActivity.this,SelectWeddingTaskActivity.class);
        intent.putExtra("mytask",(Serializable) mTaskBeanList);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==202){
            //取出传来的数据
            mAddTask= (List<TaskBean>) data.getSerializableExtra("addMyTask");
            //刷新界面
            for(int i=0;i<mAddTask.size();i++){
                mTaskBeanList.add(mAddTask.get(i));
                mMyTaskAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void backActivity(View view) {
        finish();
    }
}
