package com.gemptc.wd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.bean.TaskBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by C5-0 on 2016/5/24.
 */
public class TotalTaskAdapter extends BaseAdapter {
    //初始化布局的类，可以找到layout文件夹中的所有布局
    LayoutInflater mInflater;
    Context mContext;
    List<TaskBean> list;
    //定义一个存放checkbox是否被选中的Hhashmap
    private  HashMap<Integer,Boolean> isSelected;

    public TotalTaskAdapter(List<TaskBean> list, Context context) {
        this.list = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        isSelected=new HashMap<Integer, Boolean>();
        initCheckBoxState();
    }
    //初始化checkbox的状态
    public void initCheckBoxState(){
        for (int i=0;i<list.size();i++){
            getIsSelected().put(i,false);
        }
    }
    public HashMap<Integer,Boolean> getIsSelected(){
        return isSelected;
    }
    /*public static HashMap<Integer,Boolean> getIsSelected(){
        return isSelected;
    }*/
  /*  public static void setIsSelected(HashMap<Integer,Boolean> isSelected){
        TotalTaskAdapter.isSelected=isSelected;
    }*/
    @Override
    public int getCount() {
        //返回数据总量
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        //返回每一行的数据，这里就是一个MyData对象
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        //返回每一行的id，几乎从来不用
        return position;
    }

    //缓存布局中的控件
    class ViewHolder{
        TextView textView;
        CheckBox checkBox;
    }

    /**
     * 指定列表中每一行的布局，并且为每一行中布局的控件赋初值
     * @param position 当前绘制的是第几行界面，从0开始
     * @param convertView 缓存视图的一个类
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        //找到每一行的布局
        if (convertView == null){
            //说明是第一次绘制整屏列表，例如1-6行
            convertView =mInflater.inflate(R.layout.total_task_item,null);
            viewHolder = new ViewHolder();
            //初始化当前行布局中的所有控件
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview2);
            viewHolder.checkBox= (CheckBox) convertView.findViewById(R.id.checkbox_addtask);
            //把当前的控件缓存到布局视图中
            convertView.setTag(viewHolder);
        } else {
            //说明开始上下滑动，后面的所有行布局采用第一次绘制时的缓存布局
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //动态修改每一行控件的内容
        final TaskBean taskBean = list.get(position);
        viewHolder.textView.setText(taskBean.getTaskName());
        // 根据isSelected来设置checkbox的选中状况
       // viewHolder.checkBox.setChecked(getIsSelected().get(position));
        /*
        第二种思路：List<Integer> list = new ArrayList<>();成员变量
                   若选中list.add(position);
                   若取消list.remove((Integer)position);
                   重置每一行的状态：list.contains(position)
         */

      final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_addtask);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //说明用户选中了第position行
                    isSelected.put(position,true);

                } else {
                    //说明用户取消了第position行
                    isSelected.put(position,false);
                }
            }
        });
/*
        //重置每一行checkbox状态，必须放到监听事件后面
        boolean isChecked = taskBean.isChecked();
        checkBox.setChecked(isChecked);
        *//*if (isChecked) {
            //当前行被选中
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }*/
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变CheckBox的状态
                //viewHolder.checkBox.toggle();
                if(isSelected.get(position)){
                    isSelected.put(position,false);
                }else {
                    isSelected.put(position,true);
                }

            }
        });*/
        return convertView;
    }
}
