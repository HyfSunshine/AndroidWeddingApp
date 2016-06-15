package com.gemptc.wd.activities.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.RVHeaderBottomAdapter;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SellerDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<ProductBean> mProBeanlist;
    RVHeaderBottomAdapter mAdapter;
    private Seller mSeller;
    private TextView mLocationTxt, mfansnumTxtView;
    private ImageButton mPhoneImageBtn, mImageBtnCollect;
    private LinearLayout mLLbottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Intent intent = getIntent();
        //获取找商家页面传来商家的信息
        mSeller = (Seller) intent.getSerializableExtra("sellerdata");
        mProBeanlist = new ArrayList<>();
        initView();
        //设置layoutManager
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new RVHeaderBottomAdapter(mProBeanlist, mSeller, SellerDetailActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        String result = PrefUtils.getString(SellerDetailActivity.this, "" + mSeller.getId(), null);
        if (result != null) {
            parseData(result);
        }
        getDatas();
        mAdapter.setmOnItemClickListener(new RVHeaderBottomAdapter.OnMoreItemClickListener() {
            @Override
            public void onItemClick(View view, ProductBean product) {
                Intent intent = new Intent(SellerDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("productDetail", product);
                intent.putExtra("seller", mSeller);
                startActivity(intent);
            }
        });
        checkedUserSeller();
        initListener();


    }

    private void initListener() {
        phoneListener();
        //收藏
        mImageBtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断该用户是否关注了该商家
                uploadCollectSeller();

             /*   if(mfansnumTxtView.getText().toString()!=null){
                    int num=Integer.parseInt(mfansnumTxtView.getText().toString())+1;
                    mfansnumTxtView.setText(""+num);
                }*/

            }
        });
    }

    private void checkedUserSeller() {
        RequestParams params = new RequestParams(UrlAddress.SELLER_Controller);
        params.addBodyParameter("sellerop", "checkUserSeller");
        params.addBodyParameter("userid", 4 + "");
        params.addBodyParameter("sellerid", mSeller.getId() + "");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.startsWith("用户已关注")) {
                    mImageBtnCollect.setClickable(false);
                    mImageBtnCollect.setBackgroundResource(R.mipmap.icon_collect_r2);
                } else {
                    mImageBtnCollect.setBackgroundResource(R.drawable.icon_collect_r2);
                    mImageBtnCollect.setClickable(true);
                }
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



    private void uploadCollectSeller() {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"SellerController");
        params.addBodyParameter("sellerop","collection");
        params.addBodyParameter("userid",4+"");
        params.addBodyParameter("sellerid",mSeller.getId()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mImageBtnCollect.setClickable(false);
                mImageBtnCollect.setBackgroundResource(R.mipmap.icon_collect_r2);
                Log.e("collect","success");
                new SweetAlertDialog(SellerDetailActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("收藏商家成功!")
                        .show();
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
    private void phoneListener() {
        mPhoneImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mSeller.getSellerPhone();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + number));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mLocationTxt= (TextView) findViewById(R.id.locationTxt);
        mLocationTxt.setText(mSeller.getSellerAddress());
        mPhoneImageBtn= (ImageButton) findViewById(R.id.phoneImg);
        mImageBtnCollect= (ImageButton) findViewById(R.id.collect);
        mfansnumTxtView= (TextView) findViewById(R.id.fansnum);
    }
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"ProductController");
        params.addBodyParameter("productop", "sellerAllPro");
        params.addBodyParameter("sellerid",""+mSeller.getId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PrefUtils.setString(SellerDetailActivity.this,""+mSeller.getId(), result);
                parseData(result);
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
    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductBean>>() {
        }.getType();
        List<ProductBean> proBeanList= gson.fromJson(result, type);
        mProBeanlist.clear();
        for (int i = 0; i < proBeanList.size(); i++) {
            mProBeanlist.add(proBeanList.get(i));
            /*Log.e("数据456",sellerList.get(i).toString());*/
        }
        mAdapter.notifyDataSetChanged();
    }

    public void backActivity(View view) {
        finish();
    }
}
