package com.gemptc.wd.activities.home;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.bean.TaskBean;
import com.gemptc.wd.tools.DateTimePickDialogUtil;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditTaskActivity extends AppCompatActivity {
    //完成时间布局
    RelativeLayout mfinishTime;
    //提醒时间布局
    RelativeLayout mremindTime;
    //描述
    RelativeLayout mdescripeText;
    ImageView mDescripeImageView;
    //完成时间text，提醒时间，描述
   public TextView mFinshTimeTxt, mRemindmTimeTxt, mDescripeTextView;
    //描述内容
    EditText mDescripeEditText;
    TextView mSaveTxt;
    Calendar c = Calendar.getInstance();
    String remindTime;
    private TaskBean mTaskBean;
    int mTaskId;
    private SweetAlertDialog mSweetAlertDialog;
    long remindTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        //manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = getIntent();
        mTaskId= (int) intent.getSerializableExtra("edittaskid");
        initView();
        //从网络获取该任务并显示
        initListener();
        initData();
    }

    private void initData() {
        //先从缓存中取，有的话直接解析，没有的话，再请求
        String result = PrefUtils.getString(EditTaskActivity.this, "renwu" + mTaskId, null);
        if (result != null) {
            parseData(result);
        }
        getDatas();
    }

    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","taskdetail");
        params.addBodyParameter("taskid",""+mTaskId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //开始存储数据
                PrefUtils.setString(EditTaskActivity.this, "renwu" + mTaskId, result);
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
    private void parseData(String result) {
        String remindTime = PrefUtils.getString(EditTaskActivity.this, "remindtime" + mTaskId, null);
        Gson gson = new Gson();
        TaskBean task= gson.fromJson(result, TaskBean.class);
        mTaskBean=new TaskBean();
        mTaskBean.setTaskId(task.getTaskId());
        mTaskBean.setuId(task.getuId());
        mTaskBean.setTaskName(task.getTaskName());
        mTaskBean.setTaskTime(task.getTaskTime());
        mTaskBean.setTaskDescription(task.getTaskDescription());
        mTaskBean.setTaskState(task.getTaskState());
        //显示数据
        if(remindTime!=null){
            mRemindmTimeTxt.setText(remindTime);
        }if(mTaskBean.getTaskTime().length()>0){
            mFinshTimeTxt.setText(mTaskBean.getTaskTime());
        }if(mTaskBean.getTaskDescription()!=null && mTaskBean.getTaskDescription().length()>0){
            Log.e("cmm",mTaskBean.getTaskDescription()+",,,lll");
            String mm=mTaskBean.getTaskDescription();
            mDescripeEditText.setText(mm);
        }
    }

   /* //按返回键时，
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //获取修改后的数据并保存
        saveTask();
        finish();
    }*/

    private void initListener() {
        mfinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        mFinshTimeTxt.setText(DateFormat.format("yyy-MM-dd", c));
                        // Toast.makeText(EditTaskActivity.this,DateFormat.format("yyy-MM-dd", c), Toast.LENGTH_SHORT).show();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.setIcon(R.mipmap.icon_task_remind);
                dialog.show();
            }
        });
        mremindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        EditTaskActivity.this, "2016年4月23日 17:44");
                dateTimePicKDialog.dateTimePicKDialog();
                /*remindTime = dateTimePicKDialog.getDateTime();
                Log.e("cmmtime",remindTime);
                mRemindmTimeTxt.setText(remindTime);
                //闹钟功能*/

            }

        });
        mSaveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetAlertDialog=null;
                mSweetAlertDialog=new SweetAlertDialog(EditTaskActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                mSweetAlertDialog.setTitleText("正在保存任务......").show();
                saveTask();
            }
        });

    }

    public void saveTask() {

        //当点击编辑来保存的时候，tasktype=weiwancheng，
        //当点击完成来保存的时候，tasktype=
        //当点击删除来保存的时候，tasktype=
        getDatas();
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop", "updatetask");
        params.addBodyParameter("tasktype", "weiwancheng");
        params.addBodyParameter("taskid", "" + mTaskBean.getTaskId());
        params.addBodyParameter("userid", "" + mTaskBean.getuId());
        params.addBodyParameter("taskname", mTaskBean.getTaskName());
        params.addBodyParameter("taskdescription", mDescripeEditText.getText().toString());
        params.addBodyParameter("tasktime", mFinshTimeTxt.getText().toString());

        Log.e("cmm", String.valueOf(params));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mSweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                mSweetAlertDialog .setTitleText("保存成功！")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                mSweetAlertDialog.dismiss();
                                finish();
                                Intent intent=new Intent(EditTaskActivity.this,HomeWeddingTaskActivity.class);
                                intent.putExtra("finshtime",mFinshTimeTxt.getText().toString());
                              //  intent.putExtra("remindtime",remindTimes);
                                startActivity(intent);
                                HomeWeddingTaskActivity.mHandler.sendEmptyMessage(508);
                            }
                        });
               // Toast.makeText(EditTaskActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mSweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                mSweetAlertDialog.setTitleText("错误").setContentText("网络出现错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        //同时把提醒时间保存到偏好设置里
        PrefUtils.setString(EditTaskActivity.this, "remindtime" + mTaskBean.getTaskId(), mRemindmTimeTxt.getText().toString());

    }

    private void initView() {
        mfinishTime = (RelativeLayout) findViewById(R.id.finishTimeRl);
        mremindTime = (RelativeLayout) findViewById(R.id.remindTimeRL);
        mFinshTimeTxt = (TextView) findViewById(R.id.finshTxt);
        mRemindmTimeTxt = (TextView) findViewById(R.id.remindTxt);
        mDescripeTextView = (TextView) findViewById(R.id.descriptionTxt);
        mDescripeEditText = (EditText) findViewById(R.id.editText);
        mSaveTxt = (TextView) findViewById(R.id.saveTxt);

    }

}
