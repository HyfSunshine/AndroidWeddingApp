package com.gemptc.wd.activities.social;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.utils.UrlAddress;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditPostActivity extends AppCompatActivity  {
    InputMethodManager imm;
    private GridView noScrollgridview;
    private GridAdapter adapter;

    private ImageView iv_addpic;
    private EditText  et_postTitle,et_postContent;
    private Button sendButton;

    private static final int REQUES_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();
    //弹出式对话框
    private SweetAlertDialog sweetDialogLoad;

     int postselection;

    private int mScreenWidth;
    private int mScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Intent intent=getIntent();
        postselection=intent.getIntExtra("postselection",0);
        initView();
        initData();
        initListeners();

        Display display=getWindowManager().getDefaultDisplay();
        mScreenHeight=display.getHeight();
        mScreenWidth=display.getWidth();

    }

    private void initData() {
        adapter=new GridAdapter();
        noScrollgridview.setAdapter(adapter);
    }


    private void UploadData() {
        RequestParams  params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addBodyParameter("postop","addpost");
        params.addBodyParameter("postselection",""+postselection);
        params.addBodyParameter("userid","4");
        params.addBodyParameter("username","赵志飞");
        if (!mResults.isEmpty()){
            params.addBodyParameter("haveImage","yes");
            for (int i = 0; i < mResults.size(); i++) {
                params.addBodyParameter("postpic",new File(mResults.get(i)),"multipart/form-data");
            }
        }

        params.addBodyParameter("title",et_postTitle.getText().toString());
        params.addBodyParameter("postcontent",et_postContent.getText().toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据获取到",result);
                sweetDialogLoad.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sweetDialogLoad.setTitleText("发布成功");
                sweetDialogLoad.show();
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("失败",ex.toString());
                sweetDialogLoad.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetDialogLoad.setTitleText("请检查您的网络");
                sweetDialogLoad.setTitle("发布失败");
                sweetDialogLoad.show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("数据","返回");
                sweetDialogLoad.dismiss();

            }
        });
    }



    private void initListeners() {
        iv_addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启图片选择器
                Intent intent = new Intent(EditPostActivity.this, ImagesSelectorActivity.class);
                // 选择图片的最大数量
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
                // 展示图片的最小的尺寸;
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 1);
                // 显示相机
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                // 将当前选定的图像作为初始值
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                // 启动选择器
                startActivityForResult(intent, REQUES_CODE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断输入框能否为空
                if ("".equals(et_postTitle.getText().toString().trim())||"".equals(et_postContent.getText().toString().trim())){
                    //设置提示
                    Toast.makeText(EditPostActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    sweetDialogLoad = new SweetAlertDialog(EditPostActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                    sweetDialogLoad.setTitleText("正在发布帖子").show();
                    UploadData();
                }
            }
        });
    }


    private void initView() {
        iv_addpic= (ImageView) findViewById(R.id.addPostpic);
        sendButton= (Button) findViewById(R.id.postSend);
        et_postTitle= (EditText) findViewById(R.id.postTitle);
        et_postContent= (EditText) findViewById(R.id.postContent);
        noScrollgridview= (GridView) findViewById(R.id.noScrollgridview);
    }


  public  class GridAdapter extends BaseAdapter{



      @Override
      public int getCount() {
          return mResults.size();
      }

      @Override
      public Object getItem(int position) {
          return mResults.get(position);
      }

      @Override
      public long getItemId(int position) {
          return position;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
          View view=View.inflate(EditPostActivity.this,R.layout.item_grid_editpost,null);
          ImageView imageView= (ImageView) view.findViewById(R.id.iv_postpic);
          Glide.with(EditPostActivity.this).load(mResults.get(position)).into(imageView);

          view.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                  mResults.remove(position);
                  notifyDataSetChanged();
                  return true;
              }
          });
          return view;
      }
  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null&&requestCode==REQUES_CODE){
            mResults=data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
            adapter.notifyDataSetChanged();
        }
    }

    //返回
    public void back(View view) {
        //隐藏键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
