package com.gemptc.wd.adapter;

import android.content.Context;
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
import com.gemptc.wd.bean.TaskBean;

import java.util.List;

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
            holder.llEdit.setOnClickListener(new OnLLClickListener());
            holder.llFinish.setOnClickListener(new OnLLClickListener());
            holder.llDelete.setOnClickListener(new OnLLClickListener());
            //如果点击的是当前项，则将其展开，否则将其隐藏
            if(expandPosition == position){
                holder.TaskItemBottom.setVisibility(View.VISIBLE);
            }else{
                holder.TaskItemBottom.setVisibility(View.GONE);
            }
        }

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

    class OnLLClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ll_edit:

                    break;
                case R.id.ll_finish:
                    Toast.makeText(context, "完成按钮被点击", Toast.LENGTH_LONG).show();
                    break;
                case R.id.ll_delete:
                    Toast.makeText(context, "删除按钮被点击", Toast.LENGTH_LONG).show();
                    break;
            }
        }

    }

    class ViewHolder {
        TextView task_tv;
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
