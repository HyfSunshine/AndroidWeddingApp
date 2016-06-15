package com.gemptc.wd.activities.mine;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EdtiUserDataActivity extends AppCompatActivity{

    private ImageView iv_edt_fanhui;
    private LinearLayout ll_edt_userPic;
    private LinearLayout ll_edt_userName;
    private LinearLayout ll_edt_userMarryTime;
    private LinearLayout ll_edt_userSex;
    private LinearLayout ll_edt_userAddress;
    private LinearLayout ll_edt_userSign;

    private TextView tv_userMarryTime,tv_userName,tv_userSign,tv_userSex,tv_userAddress,tv_save;
    private ImageView iv_userImage;

    //日期选择控件
    private View vMasker;
    private TimePickerView pvTime;
    private OptionsPickerView pvOptions;

    //选择器（男女）
    private ArrayList<String> sexList=new ArrayList<>();
    //弹出照相，拍照的控件
    //定义图片选择器的属性
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    private UserBean user;
    private SweetAlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edti_user_data);
        initView();
        initData();
        initListener();
    }

    //初始化界面数据
    private void initData() {
        //从缓存中读取
        String userInfoJson = PrefUtils.getString(this,"user_self_info",null);
        if (userInfoJson!=null){
            //解析数据
            parseUserData(userInfoJson);
        }
    }

    private void parseUserData(String userInfoJson) {
        Gson gson = new Gson();
        Type type = new TypeToken<UserBean>() {
        }.getType();
        user = gson.fromJson(userInfoJson, type);
        Log.e("头像名", user.getU_picname());
        System.out.println("头像名:"+user.getU_picname());
        //3.设置控件的值
        setViewData();
    }

    private void setViewData() {

        //1.头像
        if (!"".equals(user.getU_picname())) {
            Glide.with(this).load(UrlAddress.USER_IMAGE_ADDRESS + user.getU_picname()).into(iv_userImage);
        }
        //2.昵称
        tv_userName.setText(user.getU_name());
        //3.婚期
        tv_userMarryTime.setText(user.getU_marrytime());
        //4.性别
        tv_userSex.setText(user.getU_sex());
        //5.家乡
        tv_userAddress.setText(user.getU_address());
        //6.个人简介
        tv_userSign.setText(user.getU_sign());

    }


    private void initView() {
        iv_edt_fanhui= (ImageView) findViewById(R.id.iv_edt_fanhui);
        ll_edt_userPic= (LinearLayout) findViewById(R.id.ll_edt_userPic);
        ll_edt_userName= (LinearLayout) findViewById(R.id.ll_edt_userName);
        ll_edt_userMarryTime= (LinearLayout) findViewById(R.id.ll_edt_userMarryTime);
        ll_edt_userSex= (LinearLayout) findViewById(R.id.ll_edt_userSex);
        ll_edt_userAddress= (LinearLayout) findViewById(R.id.ll_edt_userAddress);
        ll_edt_userSign= (LinearLayout) findViewById(R.id.ll_edt_userSign);
        tv_userMarryTime= (TextView) findViewById(R.id.tv_userMarryTime);
        tv_userName= (TextView) findViewById(R.id.tv_userName);
        tv_userSign= (TextView) findViewById(R.id.tv_userSign);
        tv_userSex= (TextView) findViewById(R.id.tv_userSex);
        tv_userAddress = (TextView) findViewById(R.id.tv_userAddress);
        iv_userImage= (ImageView) findViewById(R.id.iv_userImage);

        //保存的按钮
        tv_save= (TextView) findViewById(R.id.tv_saveUserInfo);

        //日期选择控件
        vMasker=findViewById(R.id.vMasker);
        //初始化时间选择器的控件
        initTimePickerView();
        //初始化选择器控件
        initSelectSex();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            String dataReturn = data.getStringExtra("data_return");
            if (requestCode==10){
                //表示设置用户的名字回调
                tv_userName.setText(dataReturn);
            }else if (requestCode==20){
                //表示设置用户的签名回调
                tv_userSign.setText(dataReturn);
            }
            if (requestCode==30){
                //表示设置用户的地址/家乡的回调
                tv_userAddress.setText(dataReturn);
            }
            if (requestCode==REQUEST_CODE){
                if (resultCode==RESULT_OK){
                    //这里存放的是路径
                    mResults=data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                    for (int i = 0; i < mResults.size(); i++) {
                        Log.e("文件的路径为",mResults.get(i));
                    }
                    Glide.with(this).load(new File(mResults.get(0))).into(iv_userImage);
                }
            }
        }
    }


    private void initSelectSex() {
        sexList.add("男");
        sexList.add("女");
        pvOptions = new OptionsPickerView(this);
        pvOptions.setPicker(sexList);
        pvOptions.setTitle("选择性别");
        //不循环
        pvOptions.setCyclic(false);
        //默认选择
        if ("男".equals(tv_userSex.getText().toString())){
            pvOptions.setSelectOptions(0);
        }else {
            pvOptions.setSelectOptions(1);
        }
        //监听选项
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                tv_userSex.setText(sexList.get(options1));
            }
        });
    }

    private void initTimePickerView() {
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR)+20);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(false);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tv_userMarryTime.setText(getYMD(date));
            }
        });
    }

    private void initListener() {
        MyListener listener = new MyListener();
        iv_edt_fanhui.setOnClickListener(listener);
        ll_edt_userPic.setOnClickListener(listener);
        ll_edt_userName.setOnClickListener(listener);
        ll_edt_userMarryTime.setOnClickListener(listener);
        ll_edt_userSex.setOnClickListener(listener);
        ll_edt_userAddress.setOnClickListener(listener);
        ll_edt_userSign.setOnClickListener(listener);

        tv_save.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{

        private Intent intent;

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //点击返回按钮
                case R.id.iv_edt_fanhui:
                    finish();
                break;
//                点击编辑用户头像
                case R.id.ll_edt_userPic:
                    // start multiple photos selector
                    Intent intent = new Intent(EdtiUserDataActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1);
// min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 1);
// show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                break;
//                点击编辑用户名字
                case R.id.ll_edt_userName:
                    intent = new Intent(EdtiUserDataActivity.this,EditUserNameSignActivity.class);
                    intent.putExtra("firstContent",tv_userName.getText().toString());
                    intent.putExtra("headTitle","编辑昵称");
                    intent.putExtra("inputHint","请输入昵称");
                    startActivityForResult(intent,10);
                break;
//                点击编辑用户结婚时间
                case R.id.ll_edt_userMarryTime:
                    pvTime.show();
                break;
//                点击选择用户性别
                case R.id.ll_edt_userSex:
                    pvOptions.show();
                break;
                //点击填写用户地址
                case R.id.ll_edt_userAddress:
                    ToastUtils.shortToast(EdtiUserDataActivity.this,"您点击了编辑用户地址");
                    intent = new Intent(EdtiUserDataActivity.this,EditUserNameSignActivity.class);
                    intent.putExtra("firstContent",tv_userAddress.getText().toString());
                    intent.putExtra("headTitle","编辑家乡");
                    intent.putExtra("inputHint","请输入地址");
                    startActivityForResult(intent,30);
                break;
//                点击填写用户签名
                case R.id.ll_edt_userSign:
                    intent = new Intent(EdtiUserDataActivity.this,EditUserNameSignActivity.class);
                    intent.putExtra("firstContent",tv_userSign.getText().toString());
                    intent.putExtra("headTitle","编辑签名");
                    intent.putExtra("inputHint","请输入内容");
                    startActivityForResult(intent,20);
                break;
                //点击保存按钮时候要做的操作
                case R.id.tv_saveUserInfo:
                    //点击的时候出现一个Dialog
                    progressDialog=null;
                    progressDialog = new SweetAlertDialog(EdtiUserDataActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                    progressDialog.setTitleText("正在更新资料");
                    progressDialog.show();
                    //设置按返回键不可以退出
                    progressDialog.setCancelable(false);
                    //设置圆圈的颜色
                    progressDialog.getProgressHelper().setBarColor(R.color.zhuColor);
                    updateUserData();
                break;
            }
        }
    }

    //上传用户的信息
    private void updateUserData() {
        //1.获取到用户昵称
        String userName = tv_userName.getText().toString();
        //2.获取用户结婚时间
        String userMarryTime=tv_userMarryTime.getText().toString();
        //3.获取用户的性别
        String userSex=tv_userSex.getText().toString();//男（man）,女（woman）
        //4.获取用户的地址
        String userAddress = tv_userAddress.getText().toString();
        //5.获取用户的签名
        String userSign=tv_userSign.getText().toString();
        //6.获取用户的文件路径
        //7.获取用户自己的ID
       String userid = user.getU_id()+"";
        //如果文件的大小大于0，说明用户选取了照片，就进行上传图片
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","updateuser");
        params.addBodyParameter("username",userName);
        params.addBodyParameter("usermarrytime",userMarryTime);
        if ("男".equals(userSex)){
            params.addBodyParameter("usersex","man");
        }else{
            params.addBodyParameter("usersex","woman");
        }

        params.addBodyParameter("useraddress",userAddress);
        params.addBodyParameter("usersign",userSign);
        if (mResults!=null&&!mResults.isEmpty()){
            params.addBodyParameter("haveImage","yes");
            params.addBodyParameter("userpic",new File(mResults.get(0)),"multipart/form-data");
        }
        params.addBodyParameter("userid",userid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.setTitleText("更新成功");
                progressDialog.setConfirmText("确定");
                progressDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //更新本地缓存
                        updataLocalCache(user.getU_id()+"");
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressDialog.setTitleText("错误");
                progressDialog.setContentText("网络出现问题");
                progressDialog.setConfirmText("确定");
                progressDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //更新本地缓存
    private void updataLocalCache(String userid) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        //http://10.201.1.9:8080/WeddingJson/UserController?userop=getUserByPhoneNum&phoneNum=12345678910
        params.addQueryStringParameter("userop","detailsuser");
        params.addQueryStringParameter("userid",userid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //存储用户自己的数据
                PrefUtils.setString(EdtiUserDataActivity.this,"user_self_info",result);
                Intent intent = new Intent();
                intent.putExtra("data_return","HelloAndroid");
                setResult(222,intent);
                finish();
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
    private String getYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }
}
