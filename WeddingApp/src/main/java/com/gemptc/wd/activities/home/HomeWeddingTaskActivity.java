package com.gemptc.wd.activities.home;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.social.EditPostActivity;
import com.gemptc.wd.adapter.MyTaskAdapter;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.TaskBean;
import com.gemptc.wd.utils.DateUtil;
import com.gemptc.wd.utils.NumberCircleProgressBar;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeWeddingTaskActivity extends AppCompatActivity {
    private ListView mListView;
    private List<TaskBean> mTaskBeanList = new ArrayList<>();
    private MyTaskAdapter mMyTaskAdapter;
    private ImageView mEmptyImgView;
    private List<TaskBean> mAddTask = new ArrayList<>();
    private ImageButton mTaskSettingsBtn;
    private TextView mNoFinshName,shijian;
     int mNoFinshTaskNum;
    private SweetAlertDialog mSweetAlertDialog;
    public static Handler mHandler;
    private  String finshtime;
    private long remindTime;
    private AlarmManager manager;
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_wedding_task);
       /* manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=new Intent();
        remindTime=intent.getLongExtra("remindtime",0);*/
        mSweetAlertDialog=null;
        mSweetAlertDialog=new SweetAlertDialog(HomeWeddingTaskActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.setTitleText("正在加载数据......").show();
        initView();
        mMyTaskAdapter = new MyTaskAdapter(mTaskBeanList, HomeWeddingTaskActivity.this);
        mListView.setAdapter(mMyTaskAdapter);
        downloadTask();
        initHandler();
        getFinshTime();
    }
    private void getFinshTime() {
        finshtime="2016年08月01日";
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date marrytime=null;
        try {
            marrytime = df.parse(finshtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long marryTime = marrytime.getTime();
        long currentTime = System.currentTimeMillis();
        int shengyushijian = (int) ((marryTime-currentTime)/(1000*60*60*24));
        if (shengyushijian>0){
            shijian.setText(shengyushijian+"");
        }
    }



    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==500){
                    Log.e("收到消息","");
                    int currentNum = Integer.parseInt(mNoFinshName.getText().toString());
                    mNoFinshName.setText((currentNum-1)+"");
                }else if(msg.what==508){
                    mMyTaskAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void initNoFinshNum() {
        mNoFinshTaskNum=0;
        Log.e("cmm",mNoFinshTaskNum+"开始,,,,"+mTaskBeanList.size());
        for(int i=0;i<mTaskBeanList.size();i++){
            Log.e("cmm",mTaskBeanList.get(i).getTaskState()+",,,,kkk");
            if(mTaskBeanList.get(i).getTaskState()==0){
                mNoFinshTaskNum++;

            }
        }
        Log.e("cmm",mNoFinshTaskNum+",,,,");
        mNoFinshName.setText(""+mNoFinshTaskNum);
    }
    private void initListener() {
        mTaskSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View DialogView = getLayoutInflater().inflate ( R.layout.tasksettings, null);
                AlertDialog.Builder builder=new AlertDialog.Builder(HomeWeddingTaskActivity.this);
                builder.setMessage("Settings")
                        .setCancelable(false)
                        .setView(DialogView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               //按时间排序
                                Collections.sort(mTaskBeanList, new Comparator<TaskBean>() {
                                    /**
                                     *
                                     * @param lhs
                                     * @param rhs
                                     * @return an integer < 0 if lhs is less than rhs, 0 if they are
                                     *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                                     */
                                    @Override
                                    public int compare(TaskBean lhs, TaskBean rhs) {
                                        Date date1 = DateUtil.stringToDate(lhs.getTaskTime());
                                        Date date2 = DateUtil.stringToDate(rhs.getTaskTime());
                                        // 对日期字段进行升序，如果欲降序可采用after方法
                                        if (date1!=null&&date2!=null&&date1.after(date2)) {
                                                return 1;
                                            }
                                            return -1;
                                        }

                                });
                               Log.e("time",mTaskBeanList.get(0).getTaskTime());
                                Log.e("time",mTaskBeanList.get(1).getTaskTime());
                                Log.e("time",mTaskBeanList.get(2).getTaskTime());
                               mListView.setAdapter(new MyTaskAdapter(mTaskBeanList,HomeWeddingTaskActivity.this));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }) .show();
            }
        });

    }
    private void initView() {
        mListView = (ListView) findViewById(R.id.my_task_listView);
        mEmptyImgView = (ImageView) findViewById(R.id.emptyImgView);
        mTaskSettingsBtn= (ImageButton) findViewById(R.id.taskSettingsBtn);
        mNoFinshName= (TextView) findViewById(R.id.txtNoFinsh);
        shijian= (TextView) findViewById(R.id.shijian);
    }

    private void downloadTask() {
        //从网络上下载数据
        String result = PrefUtils.getString(HomeWeddingTaskActivity.this, "usertasklist", null);
        if (result != null) {
            parseData(result);
        }
        getDatas();

    }
    //请求服务器，获取我的任务
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop", "taskList");
        params.addBodyParameter("userid", PrefUtils.getString(this,"user_self_id",null));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("gettask",result);

                PrefUtils.setString(HomeWeddingTaskActivity.this, "usertasklist", result);
                parseData(result);
                mSweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                mSweetAlertDialog .setTitleText("数据加载成功！")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mSweetAlertDialog.dismiss();
                            }
                        });
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
        Log.e("获取的任务数据",result);
        Gson gson = new Gson();
        Type type = new TypeToken<List<TaskBean>>() {
        }.getType();
        if(result.equals("用户还没有任何任务")){
        }else{
            List<TaskBean> taskBeanList = gson.fromJson(result, type);
            mTaskBeanList.clear();
            for (int i = 0; i < taskBeanList.size(); i++) {
                mTaskBeanList.add(taskBeanList.get(i));
            /*Log.e("数据456",sellerList.get(i).toString());*/
            }
            mListView.setAdapter(new MyTaskAdapter(mTaskBeanList,HomeWeddingTaskActivity.this));
            //mMyTaskAdapter.notifyDataSetChanged();
            initListener();
            initNoFinshNum();
            if (mTaskBeanList.size() == 0) {
                mEmptyImgView.setVisibility(View.VISIBLE);
            }
        }
    }
    //跳转到添加任务界面
    public void addTask(View view) {
        Intent intent = new Intent(HomeWeddingTaskActivity.this, SelectWeddingTaskActivity.class);
        intent.putExtra("mytask", (Serializable) mTaskBeanList);
        startActivity(intent);
        HomeWeddingTaskActivity.this.finish();
    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SweetAlertDialog addDialog = new SweetAlertDialog(HomeWeddingTaskActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        addDialog.setTitleText("正在刷新界面.....");
        addDialog.getProgressHelper().getBarColor(getResources().getColor(R.color.textDown));
        addDialog.show();
        if (requestCode == 202) {
            //取出传来的数据
           //mAddTask = (List<TaskBean>) data.getSerializableExtra("addMyTask");
            Log.e("refersh",""+mAddTask.size());
            //刷新界面
            *//*mTaskBeanList.clear();
            for (int i = 0; i < mAddTask.size(); i++) {
                mTaskBeanList.add(mAddTask.get(i));
                mMyTaskAdapter.notifyDataSetChanged();
            }*//*
        }
        addDialog.dismiss();
    }*/
    public void backActivity(View view) {
        finish();
    }
}
