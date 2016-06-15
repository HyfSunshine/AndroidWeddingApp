package com.gemptc.wd.activities.kind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.activities.kind.adapter.AllProductKindAdapter;
import com.gemptc.wd.bean.ProductBean;
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

public class AllProductKindActivity extends AppCompatActivity {

    private Intent intent;
    private TextView tv_kind_headTitle;
    private ListView lv_product_kind;
    private ImageView iv_kind_back;

    private AllProductKindAdapter allProductKindAdapter;
    private List<ProductBean> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product_kind);
        intent = getIntent();
        String productop = intent.getStringExtra("productop");
        initView();
        initData();
        initListener();

        //获取缓存
        String cache_product_kind = PrefUtils.getString(this,"product_kind_"+productop,null);
        if (cache_product_kind!=null){
            parseData(cache_product_kind);
        }
        getProductData(productop);
    }

    private void initListener() {
        iv_kind_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        tv_kind_headTitle.setText(intent.getStringExtra("headTitle"));

        productList=new ArrayList<>();
        allProductKindAdapter=new AllProductKindAdapter(this,productList);
        lv_product_kind.setAdapter(allProductKindAdapter);
    }

    private void initView() {
        tv_kind_headTitle= (TextView) findViewById(R.id.tv_kind_headTitle);
        lv_product_kind= (ListView) findViewById(R.id.lv_product_kind);
        iv_kind_back= (ImageView) findViewById(R.id.iv_kind_back);
    }

    private void getProductData(final String productop) {
        RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
        if ("searchproduct".equals(productop)){
            params.addBodyParameter("searchcontent",intent.getStringExtra("searchcontent"));
        }
        params.addBodyParameter("productop",productop);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //将分类的信息存储起来
                PrefUtils.setString(AllProductKindActivity.this,"product_kind_"+productop,result);
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

    //解析从网络上获取的案例数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductBean>>(){}.getType();
        List<ProductBean> productBeanList = gson.fromJson(result,type);
        productList.clear();
        for (int i = 0; i < productBeanList.size(); i++) {
            productList.add(productBeanList.get(i));
        }

        //通知adapter更新
        allProductKindAdapter.notifyDataSetChanged();
    }
}
