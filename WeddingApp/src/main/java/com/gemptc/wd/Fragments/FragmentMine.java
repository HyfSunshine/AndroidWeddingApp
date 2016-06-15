package com.gemptc.wd.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.home.invitation.InvitationListActivity;
import com.gemptc.wd.activities.mine.MineDetailActivity;
import com.gemptc.wd.activities.mine.MineFocusAndColleActivity;
import com.gemptc.wd.activities.mine.MinePostActivity;
import com.gemptc.wd.activities.mine.UpdatePasswordActivity;
import com.gemptc.wd.activities.startApp.LoginAndRegisterActivity;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentMine extends Fragment{
    private LinearLayout ll_mine_detal,ll_myfocus,ll_mycollect,ll_mypost,ll_myinvite,ll_mytask,ll_updatePassword,ll_about;
    private Button btn_exit;

    private TextView tv_mine_userName,tv_mine_userMarryTime;
    private ImageView iv_mine_userPic;

    private View view;
    private UserBean user;
    private SweetAlertDialog mine_SweetProgress;
    private SweetAlertDialog mine_SweetError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_mine,null);
            initView();
            initListener();
            //初始化数据
            initData();
        return view;
    }

    public void initData() {
        //在获取数据成功之前显示正在加载的Dialog
        //自定义Dialog
        mine_SweetProgress=null;

        mine_SweetProgress = new SweetAlertDialog(MainActivity.mainActivity,SweetAlertDialog.PROGRESS_TYPE);
        //显示一个进程的Dialog
        mine_SweetProgress.setTitleText("Loading");
        mine_SweetProgress.show();
        //设置按返回键可以退出
        mine_SweetProgress.setCancelable(true);
        //设置圆圈的颜色
        mine_SweetProgress.getProgressHelper().setBarColor(R.color.zhuColor);
        //分三步
        //1.从缓存中获取数据
        String userData = PrefUtils.getString(MainActivity.mainActivity,"user_self_info",null);
        if (userData!=null){
            //解析Json数据
            parseUserData(userData);
        }
        //2.联网更新数据
        getInternetData();
    }

    private void getInternetData() {
        //获取登陆成功后存在本地的手机号码
        String phoneNum=PrefUtils.getString(MainActivity.mainActivity,"userPhoneNum","");
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","getUserByPhoneNum");
        params.addQueryStringParameter("phoneNum",phoneNum);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //让Dialog消失
                mine_SweetProgress.dismiss();
                Log.e("onCreateView方法中获取用户的Json信息",result);
                //存储数据
                PrefUtils.setString(MainActivity.mainActivity,"user_self_info",result);
                //解析数据
                parseUserData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                mine_SweetProgress.dismiss();
//                mine_SweetError.setTitleText("错误...")
//                        .setContentText("网络出现问题了，请检查您的网络!")
//                        .show();
                mine_SweetProgress.setTitleText("错误").setContentText("网络出现问题了，请检查您的网络!")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void parseUserData(String userData) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserBean>() {
        }.getType();
        user = gson.fromJson(userData, type);
        Log.e("获取的结果：",userData);
        Log.e("头像名",user.getU_picname());
        //3.设置控件的值
        setViewData();
    }

    private void setViewData() {
        if (!"".equals(user.getU_picname())){
            Glide.with(MainActivity.mainActivity).load(UrlAddress.USER_IMAGE_ADDRESS+user.getU_picname()).into(iv_mine_userPic);
        }
        tv_mine_userName.setText(user.getU_name());
        tv_mine_userMarryTime.setText(user.getU_marrytime());
    }

    private void initView(){
        ll_mine_detal= (LinearLayout) view.findViewById(R.id.ll_mine_detal);
        ll_myfocus= (LinearLayout) view.findViewById(R.id.ll_myfocus);
        ll_mycollect= (LinearLayout) view.findViewById(R.id.ll_mycollect);
        ll_mypost= (LinearLayout) view.findViewById(R.id.ll_mypost);
        ll_myinvite= (LinearLayout) view.findViewById(R.id.ll_myinvite);
        ll_mytask= (LinearLayout) view.findViewById(R.id.ll_mytask);
        ll_updatePassword= (LinearLayout) view.findViewById(R.id.ll_updatePassword);
        ll_about= (LinearLayout) view.findViewById(R.id.ll_about);

        btn_exit= (Button) view.findViewById(R.id.btn_exit);

        iv_mine_userPic= (ImageView) view.findViewById(R.id.iv_mine_userPic);
        tv_mine_userName= (TextView) view.findViewById(R.id.tv_mine_userName);
        tv_mine_userMarryTime= (TextView) view.findViewById(R.id.tv_mine_userMarryTime);
    }
    private void initListener() {
        MyListener listener=new MyListener();
        ll_mine_detal.setOnClickListener(listener);
        ll_myfocus.setOnClickListener(listener);
        ll_mycollect.setOnClickListener(listener);
        ll_mypost.setOnClickListener(listener);
        ll_myinvite.setOnClickListener(listener);
        ll_mytask.setOnClickListener(listener);
        ll_updatePassword.setOnClickListener(listener);
        ll_about.setOnClickListener(listener);
        btn_exit.setOnClickListener(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mine_SweetProgress=null;
        mine_SweetError=null;
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_mine_detal:
                    if (user!=null){
                        Intent intent0=new Intent(MainActivity.mainActivity, MineDetailActivity.class);
                        intent0.putExtra("user_id",user.getU_id()+"");
//                        MainActivity.mainActivity.startActivity(intent0);
                        MainActivity.mainActivity.startActivityForResult(intent0,200);
                    }else{
                        mine_SweetError=null;
                        mine_SweetError = new SweetAlertDialog(MainActivity.mainActivity, SweetAlertDialog.ERROR_TYPE);
                        mine_SweetError.setTitleText("错误...")
                                .setContentText("网络出现问题了，请检查您的网络!")
                                .show();
                    }
                break;
                case R.id.ll_myfocus:
                    Intent intent1 = new Intent(getContext(),MineFocusAndColleActivity.class);
                    intent1.putExtra("left","用户");
                    intent1.putExtra("right","商家");
                    startActivity(intent1);
                break;
                case R.id.ll_mycollect:
                    Intent intent2 = new Intent(getContext(),MineFocusAndColleActivity.class);
                    intent2.putExtra("left","案例");
                    intent2.putExtra("right","帖子");
                    startActivity(intent2);
                break;
                //点击我的帖子
                case R.id.ll_mypost:
                    startActivity(new Intent(getActivity(), MinePostActivity.class));
                break;
                //我的请帖
                case R.id.ll_myinvite:
                    startActivity(new Intent(getContext(), InvitationListActivity.class));
                break;
                case R.id.ll_mytask:
                break;
                //修改密码
                case R.id.ll_updatePassword:
                    startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
                break;
                //关于
                case R.id.ll_about:
                break;
                case R.id.btn_exit:
                    new SweetAlertDialog(MainActivity.mainActivity, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("退出后返回到登录界面")
                            .setTitleText("退出当前账号")
                            .setConfirmText("真的走了")
                            .showCancelButton(true)
                            .setCancelText("继续逛逛")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    PrefUtils.setBoolean(getActivity(),"isLogin",false);
                                    startActivity(new Intent(getActivity(), LoginAndRegisterActivity.class));
                                    sweetAlertDialog.dismiss();
                                    getActivity().finish();
                                }
                            }).show();
                    break;
            }
        }
    }
}
