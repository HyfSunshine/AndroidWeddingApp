package com.gemptc.wd.activities.social;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;


import com.android.wedding.R;

/**
 * Created by zhaozhifei on 2016/6/12.
 */
public class AddMenuPopupWindow extends PopupWindow {
    public Button btn_Collection,btn_Cancel;
    private ImageView iv_weixin,iv_qq,iv_weibo;
    private View addmenuView;

    public AddMenuPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addmenuView=inflater.inflate(R.layout.item_addmenu_popupwindow,null);
        iv_weixin= (ImageView) addmenuView.findViewById(R.id.wxShare);
        iv_qq= (ImageView) addmenuView.findViewById(R.id.qqShare);
        iv_weibo= (ImageView) addmenuView.findViewById(R.id.weiboShare);
        btn_Collection= (Button) addmenuView.findViewById(R.id.item_addmenupopupwindow_Collection);
        btn_Cancel= (Button) addmenuView.findViewById(R.id.item_addmenupopupwindow_Cancel);
        //取消按钮
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });

        //设置按钮监听
        iv_weixin.setOnClickListener(itemsOnClick);
        iv_qq.setOnClickListener(itemsOnClick);
        iv_weibo.setOnClickListener(itemsOnClick);

        btn_Collection.setOnClickListener(itemsOnClick);
        //设置AddMenuPopupWindow的View
        this.setContentView(addmenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //addmenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        addmenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height=addmenuView.findViewById(R.id.ll_popup_addmenu).getTop();
                int y= (int) event.getY();
                if (event.getAction()==MotionEvent.ACTION_UP){
                    if (y<height){
                        dismiss();
                    }
                }
                return false;
            }
        });
    }
}
