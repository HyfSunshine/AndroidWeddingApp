package com.gemptc.wd.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.mine.adapter.CollePostAdapter;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MineDetailActivity extends AppCompatActivity {

    private PullToZoomScrollViewEx scrollView;
    private ImageView iv_fanhui,iv_userPic;
    private TextView tv_edit,tv_userName,tv_userSex,tv_userMarryTime,tv_userAddress,tv_userSign;
    private ListView lv_detail_post;

    private Intent intent;
    private String userID;

    private UserBean user;
    private List<PostBean> postList;

    private SweetAlertDialog mine_SweetProgress;
    //请求上网码
    private String internetCode;
    private CollePostAdapter postAdapter;
    private String user_self_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_userdetal);
        intent = getIntent();
        userID = intent.getStringExtra("user_id");
        user_self_id = PrefUtils.getString(this,"user_self_id","");
        //加载布局
        loadViewForCode();
        scrollView= (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        //初始化PullToZoomView
        initPullToZoomView();
        //初始化布局
        initView();
        initData();

        initListener();
    }

    private void initData() {
        //显示一个进程的Dialog
        mine_SweetProgress.setTitleText("Loading");
        mine_SweetProgress.show();
        //设置按返回键可以退出
        mine_SweetProgress.setCancelable(true);
        //设置圆圈的颜色
        mine_SweetProgress.getProgressHelper().setBarColor(R.color.zhuColor);
        //判断是否是自己
        if (userID.equals(user_self_id)){
            //1.如果是自己，则直接从本地获取数据
            tv_edit.setText("编辑");
            String cacheResult=PrefUtils.getString(this,"user_self_info",null);
            if (cacheResult!=null){
                parseUserData(cacheResult);
            }
            //获取自己的信息
            getUserSelfData();
        }else {
            //2.不是自己，根据ID进行查询这个用户的信息，不要缓存
            //首先判断用户是否关注了这个人,如果关注了，那么就设置按钮可点性为false。否则可以进行关注
            userIsFocusUser();
        }

        //设置帖子列表
        postList =  new ArrayList<>();
        postAdapter = new CollePostAdapter(this,postList);
        lv_detail_post.setAdapter(postAdapter);

    }

    //判断用户是否关注了此用户
    private void userIsFocusUser() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","checkUserUser");
        params.addBodyParameter("userid",user_self_id);
        params.addBodyParameter("collUserID",userID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.startsWith("用户已经关注")){
                    //说明用户已经关注过此用户了，那么就将右上角的文字设置成【已关注】,并且不能点击
                    tv_edit.setText("已关注");
                    tv_edit.setClickable(false);
                }else{
                    tv_edit.setText("关注");
                    tv_edit.setClickable(true);
                }
                getOtherUserData(userID);
                Log.e("用户是否关注？",result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mine_SweetProgress.setTitleText("错误").setContentText("网络好像出了一些问题哎~")
                        .setConfirmText("确定").changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getUserPost() {
        RequestParams params = new RequestParams(UrlAddress.POST_Controller);
        //http://10.201.1.9:8080/WeddingJson/PostController?postop=getUserPost&userid=4
        params.addQueryStringParameter("postop","getUserPost");
        params.addQueryStringParameter("userid",userID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                parsePostData(result);
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

    private void getOtherUserData(String otherUserId) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","detailsuser");
        params.addQueryStringParameter("userid",otherUserId);
        internetCode="获取别人数据";
        x.http().post(params, new MyCommonCallback());
    }

    private void getUserSelfData() {
        //获取登陆成功后存在本地的手机号码
        String phoneNum=PrefUtils.getString(MainActivity.mainActivity,"userPhoneNum","");
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","getUserByPhoneNum");
        params.addQueryStringParameter("phoneNum",phoneNum);
        internetCode="获取自己数据";
        x.http().post(params, new MyCommonCallback());
    }

    private void setViewData() {
        //设置用户头像

        if (!user.getU_picname().equals("")){
            Glide.with(this).load(UrlAddress.USER_IMAGE_ADDRESS+user.getU_picname()).into(iv_userPic);
        }
        //设置用户名字
        tv_userName.setText(user.getU_name());
        //设置用户性别
        tv_userSex.setText(user.getU_sex());
        //设置用户婚期
        tv_userMarryTime.setText(user.getU_marrytime());
        //设置地址
        tv_userAddress.setText(user.getU_address());
        //设置用户的签名
        tv_userSign.setText(user.getU_sign());
    }

    private void initPullToZoomView() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        scrollView.setParallax(false);
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
        iv_userPic= (ImageView) scrollView.getHeaderView().findViewById(R.id.iv_user_head);
        tv_userName= (TextView) scrollView.getHeaderView().findViewById(R.id.tv_user_name);
        tv_userSex= (TextView) scrollView.getHeaderView().findViewById(R.id.tv_user_sex);
        tv_userMarryTime= (TextView) scrollView.getHeaderView().findViewById(R.id.tv_user_marry_time);
        tv_userAddress= (TextView) scrollView.getHeaderView().findViewById(R.id.tv_user_address);
        tv_userSign= (TextView) scrollView.getPullRootView().findViewById(R.id.tv_user_sign);
        lv_detail_post= (ListView) scrollView.getPullRootView().findViewById(R.id.lv_detail_post);


        //初始化两个自定义的Dialog
        mine_SweetProgress = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
    }

    private void initListener() {
        MyListener listener = new MyListener();
        iv_fanhui.setOnClickListener(listener);
        tv_edit.setOnClickListener(listener);
    }

    class  MyCommonCallback implements Callback.CommonCallback<String>{

        @Override
        public void onSuccess(String result) {
            mine_SweetProgress.dismiss();
            if ("获取自己数据".equals(internetCode)){
                Log.e("onCreateView方法中获取用户的Json信息",result);
                //存储数据
                PrefUtils.setString(MainActivity.mainActivity,"user_self_info",result);
                //解析数据
                parseUserData(result);
            }
            if ("获取别人数据".equals(internetCode)){
                Log.e("到这里了，获取别人的数据是：",result);
                parseUserData(result);
            }

            //从网络上获取某个用户的帖子
            getUserPost();
        }
        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
                mine_SweetProgress.setTitleText("错误").setContentText("网络好像出了一些问题哎~")
                        .setConfirmText("确定").changeAlertType(SweetAlertDialog.ERROR_TYPE);
            Log.e("出错误了：",ex.toString());
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {
        }
    }

    private void parsePostData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostBean>>() {
        }.getType();
        List<PostBean> plist = gson.fromJson(result, type);
        postList.clear();
        for (int i = 0; i < plist.size(); i++) {
            postList.add(plist.get(i));
        }
        //通知适配器更新
        postAdapter.notifyDataSetChanged();
        //ScollView中嵌套listView，需要设定listView的高
        Utility.setListViewHeightBasedOnChildren(lv_detail_post);
        lv_detail_post.setFocusable(false);
        scrollView.scrollTo(0,0);
    }


    private void parseUserData(String userResult) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserBean>() {
        }.getType();
        user = null;
        user = gson.fromJson(userResult, type);
        //3.设置控件的值
        setViewData();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==520){
            if (resultCode==222) {
                String cacheResult = PrefUtils.getString(this, "user_self_info", null);
                if (cacheResult != null) {
                    parseUserData(cacheResult);
                }
                setResult(222);
            }
        }
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_detal_fanhui:
                    finish();
                break;
                case R.id.tv_detal_edt:
                    if (userID.equals(user_self_id)){
                        startActivityForResult(new Intent(MineDetailActivity.this,EdtiUserDataActivity.class),520);
                    }else{
                        mine_SweetProgress=null;
                        mine_SweetProgress=new SweetAlertDialog(MineDetailActivity.this,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("正在关注");
                        mine_SweetProgress.show();
                        //收藏用户的操作
                        userCollUser();
                    }
                    break;
            }
        }
    }

    //用户收藏用户的操作
    private void userCollUser() {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","focusUser");
        //用户自己的ID
        params.addBodyParameter("userid",user_self_id);
        //被收藏人的ID
        params.addBodyParameter("collUserID",userID);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mine_SweetProgress.setTitleText("关注成功")
                .setContentText("您已成功关注此用户...");
                mine_SweetProgress.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                tv_edit.setText("已关注");
                tv_edit.setClickable(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mine_SweetProgress.setTitleText("错误").setContentText("您的网络开了小差，请检查您的网络喔~");
                mine_SweetProgress.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                Log.e("关注用户时出现错误",ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
