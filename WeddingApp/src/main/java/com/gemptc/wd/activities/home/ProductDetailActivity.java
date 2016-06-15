package com.gemptc.wd.activities.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

public class ProductDetailActivity extends AppCompatActivity {
private ProductBean mProduct;
    private Seller mSeller;
    private TextView mProductNameTxt;
    private TextView mProductDesTxt;
    private CircleImageView mImageView;
    private PullToRefreshListView mListView;
    //private ListView mListView;
    private List<String> mImgUrl=new ArrayList<>();
    private ProductAdapter mAdapter;
    private TextView mLocationTxt;
    private ImageButton mPhoneImageBtn;
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
        phoneListener();
    }

    private void phoneListener() {
        mPhoneImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mSeller.getSellerPhone();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(ProductDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
    }

    private void initView() {
        LinearLayout  hearderViewLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.product_listview_header,null);
        mListView= (PullToRefreshListView) findViewById(R.id.productListview);
        mImageView= (CircleImageView)hearderViewLayout. findViewById(R.id.cv_imageview);
        mProductNameTxt= (TextView) hearderViewLayout.findViewById(R.id.product_name);
        mProductDesTxt= (TextView) hearderViewLayout.findViewById(R.id.descriptionTxt);
        mLocationTxt= (TextView) findViewById(R.id.locationTxt);
        mPhoneImageBtn= (ImageButton) findViewById(R.id.phoneImage);
        mLocationTxt.setText(mSeller.getSellerAddress());
        mProductNameTxt.setText(mProduct.getProductName());
        mProductDesTxt.setText(mProduct.getPrDescription());
        //pulltorefresh没有addHeaderView方法 ，但内部有listview
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
        hearderViewLayout.setLayoutParams(layoutParams);
        ListView lv=mListView.getRefreshableView();
        lv.addHeaderView(hearderViewLayout);
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
