package com.gemptc.wd.activities.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.gemptc.wd.utils.ToastUtils;

public class MineDetalActivity extends AppCompatActivity {

    private PullToZoomScrollViewEx scrollView;
    private ImageView iv_fanhui;
    private TextView tv_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_userdetal);
        //加载布局
        loadViewForCode();
        scrollView= (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        //初始化PullToZoomView
        initPullToZoomView();
        //初始化布局
        initView();

        initListener();
    }

    private void initPullToZoomView() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }


    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.mine_detai_head, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.mine_detai_zoom, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.mine_detai_content, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
    }

    private void initView() {
        iv_fanhui= (ImageView) scrollView.getZoomView().findViewById(R.id.iv_detal_fanhui);
        tv_edit= (TextView) scrollView.getZoomView().findViewById(R.id.tv_detal_edt);
    }

    private void initListener() {
        MyListener listener = new MyListener();
        iv_fanhui.setOnClickListener(listener);
        tv_edit.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_detal_fanhui:
                    finish();
                break;
                case R.id.tv_detal_edt:
                    ToastUtils.shortToast(MineDetalActivity.this,"您点击了编辑按钮");
                    startActivity(new Intent(MineDetalActivity.this,EdtiUserDataActivity.class));
                    break;
            }
        }
    }
}
