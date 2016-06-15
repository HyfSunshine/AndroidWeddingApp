package com.gemptc.wd.activities.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.adapter.ProductAdapter;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.view.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductDetailActivity extends AppCompatActivity {
    private ProductBean mProduct;
    private Seller mSeller;
    private TextView mProductNameTxt;
    private TextView mProductDesTxt;
    private CircleImageView mImageView;
    private ImageView mProductImageView;
   // private PullToRefreshListView mListView;
    private ListView mListView;
    private List<String> mImgUrl=new ArrayList<>();
    private ProductAdapter mAdapter;
    private TextView mLocationTxt;
    private ImageButton mPhoneImageBtn,mConnectImageBtn;
    private LinearLayout mRLbottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent=getIntent();
        mProduct= (ProductBean) intent.getSerializableExtra("productDetail");
        mSeller= (Seller) intent.getSerializableExtra("seller");
        initView();
        mAdapter=new ProductAdapter(ProductDetailActivity.this,mImgUrl);
        mListView.setAdapter(mAdapter);
        getImgUrl();
        checkedUserProduct();
        initListener();
    }
    private void initListener() {
        phoneListener();
        //收藏
        mConnectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCollectProduct();
            }
        });
    }

    private void checkedUserProduct() {
        RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
        params.addBodyParameter("productop", "checkUserProduct");
        params.addBodyParameter("userid", 4 + "");
        params.addBodyParameter("productid", mProduct.getProductId()+"");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.startsWith("用户已收藏")) {
                    mConnectImageBtn.setClickable(false);
                    mConnectImageBtn.setBackgroundResource(R.mipmap.icon_collect_r2);
                } else {
                    mConnectImageBtn.setBackgroundResource(R.mipmap.icon_collect_w);
                    mConnectImageBtn.setClickable(true);
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

    private void uploadCollectProduct() {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"ProductController");
        params.addBodyParameter("productop","collection");
        params.addBodyParameter("userid",4+"");
        params.addBodyParameter("productid",mProduct.getProductId()+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("collectproduct","success");
                mConnectImageBtn.setClickable(false);
                mConnectImageBtn.setBackgroundResource(R.mipmap.icon_collect_r2);
                new SweetAlertDialog(ProductDetailActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("收藏案例成功!")
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
        LinearLayout  hearderViewLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.product_listview_header,null);
        mListView= (ListView) findViewById(R.id.productListview);
        mImageView= (CircleImageView)hearderViewLayout. findViewById(R.id.cv_imageview);
        mProductImageView= (ImageView) hearderViewLayout. findViewById(R.id.productPic);
        mProductNameTxt= (TextView) hearderViewLayout.findViewById(R.id.product_name);
        mProductDesTxt= (TextView) hearderViewLayout.findViewById(R.id.descriptionTxt);
        mLocationTxt= (TextView) findViewById(R.id.locationTxt);
        mPhoneImageBtn= (ImageButton) findViewById(R.id.phoneImage);
        mRLbottom= (LinearLayout) findViewById(R.id.bottom);
        mRLbottom.getBackground().setAlpha(150);
        mConnectImageBtn= (ImageButton) findViewById(R.id.collect);
        mLocationTxt.setText(mSeller.getSellerAddress());
        mProductNameTxt.setText(mProduct.getProductName());
        mProductDesTxt.setText(mProduct.getPrDescription());
        /*//pulltorefresh没有addHeaderView方法 ，但内部有listview
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        hearderViewLayout.setLayoutParams(layoutParams);
        ListView lv=mListView.getRefreshableView();*/
        mListView.addHeaderView(hearderViewLayout);
        Glide.with(ProductDetailActivity.this)
                .load(UrlAddress.SELLER_IMAGE_ADDRESS+mSeller.getSellerPicName())
                .thumbnail(0.5f)
                .into(mImageView);
    }

    private void getImgUrl() {
        String cacheResult= PrefUtils.getString(this,mProduct.getProductName(),null);
        if (cacheResult!=null){
            parseData(cacheResult);
        }
        getDataFromInternet();

    }
    //解析数据的方法
    private void parseData(String result){
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> imageUrl= gson.fromJson(result, type);
        mImgUrl.clear();
        for (int i = 0; i < imageUrl.size(); i++) {
            mImgUrl.add(imageUrl.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }
    //从网络上获取数据
    public void getDataFromInternet() {
        String url = UrlAddress.PRODUCT_Controller;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("productop","getproductpic");
        params.addQueryStringParameter("productid",""+mProduct.getProductId());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //开始存储数据
                PrefUtils.setString(ProductDetailActivity.this,mProduct.getProductName(),result);
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


    public void backActivity(View view) {
        finish();
    }
}
