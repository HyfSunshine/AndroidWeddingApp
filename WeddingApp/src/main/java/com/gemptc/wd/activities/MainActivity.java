package com.gemptc.wd.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.wedding.R;
import com.gemptc.wd.adapter.FragmentAdapter;
import com.gemptc.wd.fragments.FragmentHome;
import com.gemptc.wd.fragments.FragmentKinds;
import com.gemptc.wd.fragments.FragmentMine;
import com.gemptc.wd.fragments.FragmentSocial;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager vp;
    private FragmentHome home;
    private RadioGroup radioGroup;
    private List<Fragment> fragmentList;
    private  EditText mEditText;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initFragments();
        initListeners();

        //初始化Handler，为的是实现图片轮播的速度不变，系统只创建一个Handler
        initHandler();
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    FragmentHome fragmentHome = (FragmentHome) fragmentList.get(0);
                    ViewPager homeViewPager = fragmentHome.viewPager;
                    List<String> imagesUrlList=fragmentHome.imagesUrlList;

                    int currentPosition = homeViewPager.getCurrentItem();
                    if (currentPosition<imagesUrlList.size()-1){
                        currentPosition++;
                    }else{
                        currentPosition=0;
                    }
                    homeViewPager.setCurrentItem(currentPosition);
                    if (!isFinishing()){
                    handler.sendEmptyMessageDelayed(0,3000);
                    }
                }
                if (msg.what==1){
                    FragmentSocial fragmentSocial = (FragmentSocial) fragmentList.get(2);
                    ViewPager socialViewPager = fragmentSocial.viewPager;
                    List<String> imagesUrlList=fragmentSocial.imagesUrlList;

                    int currentPosition = socialViewPager.getCurrentItem();
                    if (currentPosition<imagesUrlList.size()-1){
                        currentPosition++;
                    }else{
                        currentPosition=0;
                    }
                    socialViewPager.setCurrentItem(currentPosition);
                    if (!isFinishing()) {
                        handler.sendEmptyMessageDelayed(1, 3000);
                    }
                }
            }
        };
    }

    //初始化View对象的方法
    public void initViews(){
        vp = (ViewPager) findViewById(R.id.viewPager);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupBottom);
        mEditText= (EditText) findViewById(R.id.search_et_input);
    }
    //初始化Fragment
    public void initFragments(){
        fragmentList = new ArrayList<>();

        FragmentHome fragmentHome=new FragmentHome();
        FragmentKinds fragmentKinds=new FragmentKinds();
        FragmentSocial fragmentSocial=new FragmentSocial();
        FragmentMine fragmentMine=new FragmentMine();

        //加注释
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
                    vp.setCurrentItem(0,false);
                }
                if (checkedId==R.id.radioButtonKinds){
                    vp.setCurrentItem(1,false);
                }
                if (checkedId==R.id.radioButtonSocial){
                    vp.setCurrentItem(2,false);
                }
                if (checkedId==R.id.radioButtonMine){
                    vp.setCurrentItem(3,false);
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
