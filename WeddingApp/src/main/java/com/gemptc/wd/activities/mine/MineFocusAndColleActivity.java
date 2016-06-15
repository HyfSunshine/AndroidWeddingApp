package com.gemptc.wd.activities.mine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.mine.fragments.FragmentCollPost;
import com.gemptc.wd.activities.mine.fragments.FragmentCollProduct;
import com.gemptc.wd.activities.mine.fragments.FragmentFocusSeller;
import com.gemptc.wd.activities.mine.fragments.FragmentFocusUser;
import com.gemptc.wd.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MineFocusAndColleActivity extends AppCompatActivity {

    private RadioButton rb_left,rb_right;
    private RadioGroup rg_focus_coll;
    private ViewPager vp_collection;
    private ImageView iv_fanhui;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_focus_and_colle);
        initView();
        //初始化数据
        initData();
        initViewPager();
        initListener();
    }

    //初始化ViewPage中的内容
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        FragmentFocusUser userFragment = new FragmentFocusUser();
        FragmentFocusSeller sellerFragment=new FragmentFocusSeller();
        FragmentCollProduct productFragment = new FragmentCollProduct();
        FragmentCollPost posttFragment = new FragmentCollPost();
        if ("用户".equals(rb_left.getText().toString())){
            fragmentList.add(userFragment);
            fragmentList.add(sellerFragment);
        }else{
            fragmentList.add(productFragment);
            fragmentList.add(posttFragment);
        }
        //初始化适配器
        FragmentManager manager = getSupportFragmentManager();
        FragmentAdapter fragmentAdapter=new FragmentAdapter(manager,fragmentList);
        vp_collection.setAdapter(fragmentAdapter);
    }

    private void initListener() {
        MyListener listener = new MyListener();
        iv_fanhui.setOnClickListener(listener);
        vp_collection.addOnPageChangeListener(listener);
        rg_focus_coll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.rb_left){
                    vp_collection.setCurrentItem(0);
                }
                if (checkedId==R.id.rb_right){
                    vp_collection.setCurrentItem(1);
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String left = intent.getStringExtra("left");
        String right = intent.getStringExtra("right");
        rb_left.setText(left);
        rb_right.setText(right);
    }

    private void initView() {
        rb_left= (RadioButton) findViewById(R.id.rb_left);
        rb_right= (RadioButton) findViewById(R.id.rb_right);
        vp_collection= (ViewPager) findViewById(R.id.vp_collection);
        iv_fanhui = (ImageView) findViewById(R.id.iv_fanhui);
        rg_focus_coll= (RadioGroup) findViewById(R.id.rg_focus_coll);
    }

    //监听事件
    class MyListener implements View.OnClickListener,ViewPager.OnPageChangeListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_fanhui:
                    finish();
                break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position==0){
                rb_left.setChecked(true);
            }else {
                rb_right.setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
