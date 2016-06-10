package com.gemptc.wd.activities.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.android.wedding.R;
import com.gemptc.wd.utils.Bimp;
import com.gemptc.wd.utils.FileUtils;
import com.gemptc.wd.utils.ImageItem;
import com.gemptc.wd.utils.PublicWay;
import com.gemptc.wd.utils.Res;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditPostActivity extends AppCompatActivity  {
    private static final int TAKE_PICTURE=0x000001;
    InputMethodManager imm;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap ;

    private EditText  et_postTitle,et_postContent;
    private Button sendButton;

    private SweetAlertDialog sweetDialogError;

    //获取要发帖的照片路径
    private List<String> mImageUrl;
    private SweetAlertDialog sweetDialogLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Res.init(this);
        bimap= BitmapFactory.decodeResource(getResources(),R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        parentView=getLayoutInflater().inflate(R.layout.activity_edit_post,null);
        setContentView(parentView);
        init();
        initView();
        initListeners();
        initImageUrl();

    }



    private void UploadData() {
        RequestParams  params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addBodyParameter("postop","addpost");
        params.addBodyParameter("postselection","1");
        params.addBodyParameter("userid","4");
        params.addBodyParameter("username","赵志飞");
//        if (!Bimp.tempSelectBitmap.isEmpty()){
//            for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//                //params.addBodyParameter("postpic",new File(Bimp.tempSelectBitmap.get(i).imagePath));
                 //params.addBodyParameter("postpic",new File(FileUtils.SDPATH+"1465463391026.JPEG"),"multipart/form-data");
//            }
//        }
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

            }
        });
    }

    private void initImageUrl() {



    }

    private void initListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetDialogLoad = new SweetAlertDialog(EditPostActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                sweetDialogLoad.setTitleText("正在发布帖子").show();
                UploadData();
            }
        });
    }

    private void initView() {
        sendButton= (Button) findViewById(R.id.postSend);
        et_postTitle= (EditText) findViewById(R.id.postTitle);
        et_postContent= (EditText) findViewById(R.id.postContent);
    }

    public void init() {
        pop=new PopupWindow(EditPostActivity.this);
        View view=getLayoutInflater().inflate(R.layout.item_popupwindows,null);
        ll_popup= (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent= (RelativeLayout) view.findViewById(R.id.parent);
        Button btn1= (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button btn2= (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button btn3= (Button) view.findViewById(R.id.item_popupwindows_cancel);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        //拍照
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        //从相册里选择照片
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditPostActivity.this,AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in,R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        //取消
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollgridview= (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter=new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position== Bimp.tempSelectBitmap.size()){
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(EditPostActivity.this,R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM,0,0);
                }else {
                    Intent intent=new Intent(EditPostActivity.this,GalleryActivity.class);
                    intent.putExtra("position","1");
                    intent.putExtra("ID",position);
                    startActivity(intent);
                }
            }
        });
    }



    public class GridAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        @Override
        public int getCount() {
            if (Bimp.tempSelectBitmap.size()==9){
                return 9;
            }
            return (Bimp.tempSelectBitmap.size()+1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelectedPosition(int position) {
            selectedPosition=position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=inflater.inflate(R.layout.item_published_grida,parent,false);
                holder.image= (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            if (position==Bimp.tempSelectBitmap.size()){
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_addpic_unfocused));
                if (position==9){
                    holder.image.setVisibility(View.GONE);
                }
            }else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }
    }
    public class ViewHolder {
        public ImageView image;
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true){
                   if (Bimp.max==Bimp.tempSelectBitmap.size()){
                       Message message=new Message();
                       message.what=1;
                       handler.sendMessage(message);
                       break;
                   }else {
                       Bimp.max+=1;
                       Message message=new Message();
                       message.what=1;
                       handler.sendMessage(message);
                   }
               }
            }
        }).start();
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    @Override
    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PICTURE:
            if(Bimp.tempSelectBitmap.size()<9&&resultCode==RESULT_OK){
                String fileName=String.valueOf(System.currentTimeMillis());
                Bitmap bm= (Bitmap) data.getExtras().get("data");
                FileUtils.saveBitmap(bm,fileName);

                ImageItem takePhoto=new ImageItem();
                takePhoto.setBitmap(bm);
                Bimp.tempSelectBitmap.add(takePhoto);
            }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null!=PublicWay.activityList.get(i)){
                    PublicWay.activityList.get(i).finish();
                }
            }
            //退出
            System.exit(0);
        }
       return true;
    }

    //调用系统相机拍照
    public void photo() {
        Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    //返回
    public void back(View view) {
        //隐藏键盘
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.hideSoftInputFromWindow(et_postTitle.getWindowToken(),0);
//        imm.hideSoftInputFromWindow(et_postContent.getWindowToken(),0);
        finish();
    }
}
