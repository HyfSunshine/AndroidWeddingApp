package com.gemptc.wd.activities.home.invitation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wedding.R;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.mine.EdtiUserDataActivity;
import com.gemptc.wd.bean.InvitationBean;
import com.gemptc.wd.bean.UserBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddInviteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_invite_addPic, iv_invite_addUserPic, iv_invite_addback;
    private TextView tv_invite_addTime, tv_invite_add;
    private EditText edt_invite_addName01, edt_invite_addName02, edt_invite_addAddress, edt_invite_addContent;
    //存储拍照或者相册图片路径的集合
    private ArrayList<String> mResults = new ArrayList<>();
    private static final int REQUEST_CODE = 123;

    //事件选择器
    private TimePickerView pvTime;
    private String fromWho;
    //Dialog
    private SweetAlertDialog sweetAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invite);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_invite_add = (TextView) findViewById(R.id.tv_invite_add);
        iv_invite_addback = (ImageView) findViewById(R.id.iv_invite_addback);
        iv_invite_addPic = (ImageView) findViewById(R.id.iv_invite_addPic);
        iv_invite_addUserPic = (ImageView) findViewById(R.id.iv_invite_addUserPic);
        tv_invite_addTime = (TextView) findViewById(R.id.tv_invite_addTime);
        edt_invite_addName01 = (EditText) findViewById(R.id.edt_invite_addName01);
        edt_invite_addName02 = (EditText) findViewById(R.id.edt_invite_addName02);
        edt_invite_addAddress = (EditText) findViewById(R.id.edt_invite_addAddress);
        edt_invite_addContent = (EditText) findViewById(R.id.edt_invite_addContent);

        //初始化时间选择器的控件
        initTimePickerView();
    }

    private void initTimePickerView() {
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tv_invite_addTime.setText(getYMD(date));
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        fromWho = intent.getStringExtra("fromWho");
        if (!"添加".equals(fromWho)) {
            InvitationBean invite = (InvitationBean) intent.getSerializableExtra("inviteBean");
            if ("模板".equals(fromWho)) {
                x.image().bind(iv_invite_addPic, invite.getInvate_imageName());
            } else {
                if (invite.getInvate_imageName() != null) {
                    x.image().bind(iv_invite_addPic, UrlAddress.INVITE_IMAGE_ADDRESS + invite.getInvate_imageName());
                } else {
                    x.image().bind(iv_invite_addPic, UrlAddress.MUBAN_IMAGE_ADDRESS + "invite_moren.jpg");
                }
            }
            edt_invite_addName01.setText(invite.getInvite_name1());
            edt_invite_addName02.setText(invite.getInvite_name2());
            tv_invite_addTime.setText(invite.getInvite_time());
            edt_invite_addAddress.setText(invite.getInvate_address());
            edt_invite_addContent.setText(invite.getInvate_content());
        }

        //设置头像
        setUserPic();
    }

    private void setUserPic() {
        String user_self_info = PrefUtils.getString(this,"user_self_info",null);
        if (user_self_info!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<UserBean>() {
            }.getType();
            UserBean user = gson.fromJson(user_self_info, type);
            Glide.with(this).load(UrlAddress.USER_IMAGE_ADDRESS+user.getU_picname()).into(iv_invite_addUserPic);
        }
    }


    private void initListener() {
        iv_invite_addback.setOnClickListener(this);
        tv_invite_add.setOnClickListener(this);
        iv_invite_addPic.setOnClickListener(this);
        tv_invite_addTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.iv_invite_addback:
                finish();
                break;
            //保存请帖内容
            case R.id.tv_invite_add:
                String user_self_id= PrefUtils.getString(this,"user_self_id",null);
                String xinlang = edt_invite_addName01.getText().toString();
                String xinniang = edt_invite_addName02.getText().toString();
                String time = tv_invite_addTime.getText().toString();
                String address = edt_invite_addAddress.getText().toString();
                String content = edt_invite_addContent.getText().toString();

                    //那么就添加请帖
                    if (!"".equals(xinlang)&&!"".equals(xinniang)&&!"".equals(address)&&!"".equals(content)){
                        if (!mResults.isEmpty()) {
                            //判断是修改请帖还是添加一个新的请帖
                            sweetAlertDialog=null;
                            sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在添加...");
                            sweetAlertDialog.show();
                            if ("添加".equals(fromWho)||"模板".equals(fromWho)){
                            addInvite(user_self_id,xinlang,xinniang,time,address,content);
                            }else{
                                //就是修改请帖
                                InvitationBean invitation = (InvitationBean) getIntent().getSerializableExtra("inviteBean");
                                String inviteid=invitation.getInvite_id()+"";
                                updateInvite(inviteid,user_self_id,xinlang,xinniang,time,address,content);
                            }
                        }else{
                            sweetAlertDialog=null;
                            sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("提醒").setContentText("您还需要添加一张图片哟~");
                            sweetAlertDialog.show();
                        }
                    }else{
                        sweetAlertDialog=null;
                        sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("错误").setContentText("喜帖的信息还没有填写完整喔~！");
                        sweetAlertDialog.show();
                    }
                break;
            //添加请帖图片
            case R.id.iv_invite_addPic:
                //开启相册
                // start multiple photos selector
                Intent intent = new Intent(AddInviteActivity.this, ImagesSelectorActivity.class);
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
            //添加时间
            case R.id.tv_invite_addTime:
                pvTime.show();
                break;
        }
    }


    //添加请帖到服务器
    private void addInvite(String user_self_id, String xinlang, String xinniang, String time, String address, String content) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","addInvite");
        params.addBodyParameter("userid",user_self_id);
        params.addBodyParameter("xinlangName",xinlang);
        params.addBodyParameter("xinniangName",xinniang);
        params.addBodyParameter("weddingTime",time);
        params.addBodyParameter("weddingAddress",address);
        params.addBodyParameter("inviteContent",content);
        params.addBodyParameter("haveImage","yes");
        params.addBodyParameter("invateImage",new File(mResults.get(0)),"multipart/form-data");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("添加成功!").setConfirmClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent();
                                intent.putExtra("data_return","helloworld");
                                setResult(101,intent);
                                finish();
                            }
                        }
                );
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("添加请帖出现错误",ex.toString());
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("错误").setContentText("网络出现错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    //修改请帖到服务器
    private void updateInvite(String inviteid,String user_self_id, String xinlang, String xinniang, String time, String address, String content) {
        RequestParams params = new RequestParams(UrlAddress.USER_Controller);
        params.addBodyParameter("userop","updateInvite");
        params.addBodyParameter("userid",user_self_id);
        params.addBodyParameter("inviteid",inviteid);
        params.addBodyParameter("xinlangName",xinlang);
        params.addBodyParameter("xinniangName",xinniang);
        params.addBodyParameter("weddingTime",time);
        params.addBodyParameter("weddingAddress",address);
        params.addBodyParameter("inviteContent",content);
        params.addBodyParameter("invateImage",new File(mResults.get(0)),"multipart/form-data");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("修改成功!").setConfirmClickListener(
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                InvitationListActivity.activity.getInviteData();
                                finish();
                            }
                        }
                );
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("添加请帖出现错误",ex.toString());
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("错误").setContentText("网络出现错误");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            if (requestCode==REQUEST_CODE){
                mResults=data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                Glide.with(this).load(new File(mResults.get(0))).into(iv_invite_addPic);
            }
        }
    }

    private String getYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时");
        return format.format(date);
    }
}
