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
import android.view.View;
import android.widget.ImageButton;
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

public class SellerDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<ProductBean> mProBeenlist;
    RVHeaderBottomAdapter mAdapter;
    private Seller mSeller;
    private TextView mLocationTxt;
    private ImageButton mPhoneImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);
        Intent intent = getIntent();
        //获取找商家页面传来商家的信息
        mSeller = (Seller) intent.getSerializableExtra("sellerdata");
        mProBeenlist = new ArrayList<>();
        initView();
        //设置layoutManager
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new RVHeaderBottomAdapter(mProBeenlist, mSeller, SellerDetailActivity.this);
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
        phoneListener();

    }

    private void phoneListener() {
        mPhoneImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mSeller.getSellerPhone();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(SellerDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mLocationTxt= (TextView) findViewById(R.id.locationTxt);
        mLocationTxt.setText(mSeller.getSellerAddress());
        mPhoneImageBtn= (ImageButton) findViewById(R.id.phoneImg);
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
        mProBeenlist.clear();
        for (int i = 0; i < proBeanList.size(); i++) {
            mProBeenlist.add(proBeanList.get(i));
            /*Log.e("数据456",sellerList.get(i).toString());*/
        }
        mAdapter.notifyDataSetChanged();
    }

    public void backActivity(View view) {
        finish();
    }
}
