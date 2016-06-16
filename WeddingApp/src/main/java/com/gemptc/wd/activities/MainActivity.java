package com.gemptc.wd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.wedding.R;
import com.gemptc.wd.adapter.FragmentAdapter;
import com.gemptc.wd.fragments.FragmentHome;
import com.gemptc.wd.fragments.FragmentKinds;
import com.gemptc.wd.fragments.FragmentMine;
import com.gemptc.wd.fragments.FragmentSocial;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.view.LazyViewPager;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends FragmentActivity {

    private LazyViewPager vp;
    private FragmentHome home;
    private RadioGroup radioGroup;
    private List<Fragment> fragmentList;
    private  EditText mEditText;
    public static Handler handler;
   public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       mainActivity=this;
        initViews();
        initFragments();
        initListeners();
        //初始化Handler，为的是实现图片轮播的速度不变，系统只创建一个Handler
        initHandler();
        //存储用户ID
        getMySelfData();
    }

    //获取用户自己的数据
    private void getMySelfData() {
        //当Activity创建的时候存储用户的id
        //获取登陆成功后存在本地的手机号码
        String phoneNum = PrefUtils.getString(MainActivity.mainActivity,"userPhoneNum","");
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","getUserByPhoneNum");
        params.addQueryStringParameter("phoneNum", phoneNum);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //存储用户自己的ID
                Log.e("FragmentMine的Oncreate方法中获取用户的Json信息",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String user_id = jsonObject.getString("u_id");
                    String user_name=jsonObject.getString("u_name");
                    //存储用户的ID
                    PrefUtils.setString(MainActivity.mainActivity,"user_self_id",user_id);
                    //存储用户的ID
                    PrefUtils.setString(MainActivity.mainActivity,"user_self_name",user_name);
                    //存储数据
                    PrefUtils.setString(MainActivity.mainActivity,"user_self_info",result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("连接网络失败？",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
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
        vp = (LazyViewPager) findViewById(R.id.viewPager);
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

        vp.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
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
    //在按返回键的时候弹出弹框，询问用户是否退出应用
    @Override
    public void onBackPressed() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MainActivity.mainActivity, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setContentText("真的要退出吗？")
                .setTitleText("退出应用")
                .setConfirmText("退出应用")
                .showCancelButton(true)
                .setCancelText("留下逛逛")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mainActivity.finish();
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                sweetAlertDialog=null;
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200){
            if (resultCode==222) {
                FragmentMine fragmentMine = (FragmentMine) fragmentList.get(3);
                fragmentMine.initData();
            }
        }
    }
}
