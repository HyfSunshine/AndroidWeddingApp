package com.gemptc.wd.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.activities.home.EditTaskActivity;
import com.gemptc.wd.activities.home.HomeWeddingTaskActivity;
import com.gemptc.wd.activities.startApp.StartActivity;
import com.gemptc.wd.bean.TaskBean;
import com.gemptc.wd.utils.NumberCircleProgressBar;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.android.wedding.R.layout.activity_home_wedding_task;

/**
 * Created by C5-0 on 2016/5/24.
 */
public class MyTaskAdapter extends BaseAdapter {
    private  List<TaskBean>  mTaskBeenList;
    // 布局加载器
    private LayoutInflater mInflater;
    // 上下文
    private Context context;
    // 布局缓存对象
    private ViewHolder holder;
    //记录当前展开项的索引
    private int expandPosition = -1;
    private NumberCircleProgressBar numberCircleProgressBar;


    public MyTaskAdapter(List<TaskBean> taskBeen, Context context) {
        super();
        this.mTaskBeenList=taskBeen;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null ==mTaskBeenList ? 0 :mTaskBeenList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskBeenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.my_task_item, null);
            holder = new ViewHolder();
            holder.task_tv = (TextView) convertView.findViewById(R.id.mytask_textview);
            holder.time_txt= (TextView) convertView.findViewById(R.id.mytasktime_textview);
            holder.task_undo_btn = (Button) convertView.findViewById(R.id.btn_task_undo);
            holder.img1= (ImageView) convertView.findViewById(R.id.task_img1);
            holder.img2= (ImageView) convertView.findViewById(R.id.task_img2);
            holder.img3= (ImageView) convertView.findViewById(R.id.task_img3);
            holder.edit_tv = (TextView) convertView.findViewById(R.id.task_tv1);
            holder.finish_tv = (TextView) convertView.findViewById(R.id.task_tv2);
            holder.delete_tv = (TextView) convertView.findViewById(R.id.task_tv3);
            holder.TaskItemTop = (RelativeLayout) convertView.findViewById(R.id.task_item_top);
            holder.TaskItemBottom = (LinearLayout) convertView.findViewById(R.id.task_item_bottom);
            holder.llEdit = (LinearLayout) convertView.findViewById(R.id.ll_edit);
            holder.llFinish = (LinearLayout) convertView.findViewById(R.id.ll_finish);
            holder.llDelete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       TaskBean taskBean = mTaskBeenList.get(position);
        if (null != taskBean) {
            holder.TaskItemTop.setOnClickListener(new OnTaskItemClickListener(position));
            holder.task_undo_btn.setOnClickListener(new OnTaskItemClickListener(position));
            //编辑，完成，删除的点击事件
            holder.llEdit.setOnClickListener(new OnLLClickListener(position));
            holder.llFinish.setOnClickListener(new OnLLClickListener(position));
            holder.llDelete.setOnClickListener(new OnLLClickListener(position));
            //如果点击的是当前项，则将其展开，否则将其隐藏
            if(expandPosition == position){
                holder.TaskItemBottom.setVisibility(View.VISIBLE);
            }else{
                holder.TaskItemBottom.setVisibility(View.GONE);
            }
        }
        holder.task_tv.setText(mTaskBeenList.get(position).getTaskName());
        holder.time_txt.setText(mTaskBeenList.get(position).getTaskTime());
        return convertView;
    }
    class OnTaskItemClickListener implements View.OnClickListener {
        private int position;
        public OnTaskItemClickListener(int position) {
            super();
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            //如果当前项为展开，则将其置为-1，目的是为了让其隐藏，如果当前项为隐藏，则将当前位置设置给全局变量，让其展开，这也就是借助于中间变量实现布局的展开与隐藏
            if(expandPosition == position){
                expandPosition = -1;
            }else{
                expandPosition = position;
            }
            notifyDataSetChanged();
        }
    }
    class OnLLClickListener implements View.OnClickListener{
        public int position;
        public OnLLClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ll_edit:
                    //回调
                    Intent intent=new Intent(context,EditTaskActivity.class);
                    intent.putExtra("edittaskid",mTaskBeenList.get(position).getTaskId());
                   context.startActivity(intent);
                    /*EditTaskActivity activity= (EditTaskActivity) context;
                    activity.startActivityForResult(intent,508);*/

                    break;
                case R.id.ll_finish:
                    //上传到服务器，把用户的此任务状态设置为1，并删除
                    changeTask(position);
                    mTaskBeenList.remove(position);
                    notifyDataSetChanged();
                    break;
                case R.id.ll_delete:
                    Log.e("任务帖id",mTaskBeenList.get(position).getTaskId()+"");
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("主人确定删除此任务吗？")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uploadDeleteTask(mTaskBeenList.get(position).getTaskId(),"删除成功");
                                    mTaskBeenList.remove(position);
                                    notifyDataSetChanged();
                                    //把删除的任务传到服务器
                                   /* final SweetAlertDialog addDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                                    addDialog.setTitleText("正在删除.....");
                                    addDialog.show();
                                    Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(10000);//让他显示10秒后，取消ProgressDialog
                                                } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                                }
                                            addDialog.dismiss();
                                         }
                                        });
                                    t.start();*/
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                    break;
            }
        }

        private void uploadDeleteTask(int id, final String tishi) {
            final SweetAlertDialog addDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            RequestParams params=new RequestParams(UrlAddress.USER_Controller);
            params.addBodyParameter("userop","deletetask");
            params.addBodyParameter("taskid",""+id);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    addDialog.setTitleText("正在删除任务.....");
                    addDialog.show();
                    Log.e("delete", result);
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                }
                @Override
                public void onCancelled(CancelledException cex) {
                }
                @Override
                public void onFinished() {
                    addDialog.dismiss();
                    Toast.makeText(context, tishi, Toast.LENGTH_LONG).show();
                }
            });

        }

    }


    private void changeTask(int position) {
        RequestParams params=new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","updatetask");
        params.addBodyParameter("taskid",""+mTaskBeenList.get(position).getTaskId());
        params.addBodyParameter("userid",""+mTaskBeenList.get(position).getuId());
        params.addBodyParameter("taskname",mTaskBeenList.get(position).getTaskName());
        params.addBodyParameter("taskdescription",mTaskBeenList.get(position).getTaskDescription());
        params.addBodyParameter("tasktime",mTaskBeenList.get(position).getTaskTime());
        params.addBodyParameter("tasktype","wancheng");
        final int taskId=mTaskBeenList.get(position).getTaskId();
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("change",result.toString());
                Toast.makeText(context, "此任务已完成", Toast.LENGTH_SHORT).show();
                //并把服务器上的任务删除
                RequestParams params=new RequestParams(UrlAddress.USER_Controller);
                params.addBodyParameter("userop","deletetask");
                params.addBodyParameter("taskid",taskId+"");
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("delete","完成任务时删除");
                        HomeWeddingTaskActivity.mHandler.sendEmptyMessage(500);
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
                //把页面显示更改

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

    class ViewHolder {
       TextView task_tv,time_txt;
        Button task_undo_btn;
         ImageView img1;
        ImageView img2;
        ImageView img3;
        TextView edit_tv;
        TextView delete_tv;
        TextView finish_tv;
        RelativeLayout TaskItemTop;
        LinearLayout TaskItemBottom;
        LinearLayout llEdit;
        LinearLayout llFinish;
        LinearLayout llDelete;
    }



}
