package com.gemptc.wd.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import com.android.FragmentApplication.R;
import com.gemptc.wd.adapter.FragmentAdapter;
import com.gemptc.wd.fragments.FragmentHome;
import com.gemptc.wd.fragments.FragmentKinds;
import com.gemptc.wd.fragments.FragmentMine;
import com.gemptc.wd.fragments.FragmentSocial;

public class MainActivity extends FragmentActivity {

    private ViewPager vp;
    private FragmentHome home;
    private RadioGroup radioGroup;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragments();
        initListeners();
    }

    //初始化View对象的方法
    public void initViews(){
        vp = (ViewPager) findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupBottom);
    }
    //初始化Fragment
    public void initFragments(){
        fragmentList = new ArrayList<>();

        FragmentHome fragmentHome=new FragmentHome();
        FragmentKinds fragmentKinds=new FragmentKinds();
        FragmentSocial fragmentSocial=new FragmentSocial();
        FragmentMine fragmentMine=new FragmentMine();

        fragmentList.add(fragmentHome);
        fragmentList.add(fragmentKinds);
        fragmentList.add(fragmentSocial);
        fragmentList.add(fragmentMine);

        //初始化适配器
        FragmentManager manager = getSupportFragmentManager();
        FragmentAdapter fragmentAdapter=new FragmentAdapter(manager,fragmentList);
        vp.setAdapter(fragmentAdapter);
    }

    public void initListeners(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radioButtonHome){
                    vp.setCurrentItem(0);
                }
                if (checkedId==R.id.radioButtonKinds){
                    vp.setCurrentItem(1);
                }
                if (checkedId==R.id.radioButtonSocial){
                    vp.setCurrentItem(2);
                }
                if (checkedId==R.id.radioButtonMine){
                    vp.setCurrentItem(3);
                }
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton)radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
