package com.gemptc.wd.activities.home;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.Window;

import com.android.wedding.R;
import com.gemptc.wd.adapter.MerchantFragmentAdapter;
import com.gemptc.wd.fragments.JiuDianFragment;
import com.gemptc.wd.fragments.LvXingFragment;
import com.gemptc.wd.fragments.SheYingFragment;
import com.gemptc.wd.fragments.JieZhiFragment;
import com.gemptc.wd.fragments.LiFuFragment;
import com.gemptc.wd.fragments.ZuLinFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class HomeFindMerchantActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private TabPageIndicator  mTabPageIndicator;
    private MerchantFragmentAdapter mAdapter ;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_find_merchant);
        Intent intent = getIntent();
        initView();
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.merchant_viewpager);
       mTabPageIndicator= (TabPageIndicator) findViewById(R.id.merchant_indicator);
        mFragments=new ArrayList<Fragment>();
        Fragment liFuFragment=new LiFuFragment();
        Fragment jieZhiFragment=new JieZhiFragment();
        Fragment jiuDianFragment=new JiuDianFragment();
        Fragment lvXingFragment=new LvXingFragment();
        Fragment sheYingFragment=new SheYingFragment();
        Fragment zuLinFragment=new ZuLinFragment();


        mFragments.add(liFuFragment);
        mFragments.add(jieZhiFragment);
        mFragments.add(jiuDianFragment);
        mFragments.add(lvXingFragment);
        mFragments.add(sheYingFragment);
        mFragments.add(zuLinFragment);
        mAdapter = new MerchantFragmentAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mTabPageIndicator.setViewPager(mViewPager, 0);


    }

}
