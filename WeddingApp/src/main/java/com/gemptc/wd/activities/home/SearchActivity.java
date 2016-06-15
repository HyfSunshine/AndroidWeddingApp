package com.gemptc.wd.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.SellerListAdapter;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private String searchText;
    private PullToRefreshListView mListView;
    private ImageButton mImageButtonBack;
    private SellerListAdapter mAdapter;
    private List<Seller> mList=new ArrayList<>();
    private TextView mEmptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent=getIntent();
        searchText=intent.getStringExtra("searchtext");
        initView();
        mAdapter=new SellerListAdapter(this,mList);
        mListView.setAdapter(mAdapter);
        initData();
        initListener();

    }

    private void initListener() {
        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        String cacheResult= PrefUtils.getString(this,"searchseller",null);
        if (cacheResult!=null){
            parseData(cacheResult);
        }
        getDataFromInternet();

    }

    private void getDataFromInternet() {
        RequestParams params=new RequestParams(UrlAddress.SELLER_Controller);
        params.addBodyParameter("sellerop","searchseller");
        params.addBodyParameter("searchcontent",searchText);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("search","success");
                //开始存储数据
                PrefUtils.setString(SearchActivity.this,"searchseller",result);
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

    private void parseData(String cacheResult) {
        Gson gson=new Gson();
        Type type=new TypeToken<List<Seller>>(){}.getType();
        List<Seller> sellerList=gson.fromJson(cacheResult,type);
        mList.clear();
        if(sellerList.size()>0){
            mEmptyTextView.setVisibility(View.GONE);
            for(int i=0;i<sellerList.size();i++){
                mList.add(sellerList.get(i));
            }
        }
        if (sellerList.size() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mImageButtonBack= (ImageButton) findViewById(R.id.btn_back);
        mListView= (PullToRefreshListView) findViewById(R.id.listView);
        mEmptyTextView= (TextView) findViewById(R.id.Textempty);
    }


}
