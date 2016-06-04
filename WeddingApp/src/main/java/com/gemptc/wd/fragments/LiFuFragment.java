package com.gemptc.wd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.adapter.SellerAdapter;
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

/**
 * Created by Gemptc on 2016/5/19.
 */
public class LiFuFragment extends Fragment {
    private ListView mListView;
    //找数据
    private List<Seller> mList;
    private SellerAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.tab_lifu, container, false);
        mListView= (ListView) view.findViewById(R.id.listView);
        //找数据，并解析出数据存到一个bean类里
        mList=new ArrayList<>();
       //mSellerList=new ArrayList<>();
        myAdapter = new SellerAdapter(getContext(),mList);
        mListView.setAdapter(myAdapter);
        //初始化数据
        String result = PrefUtils.getString(getContext(), "seller_lifu", null);
        if (result != null) {
            parseData(result);
            Toast.makeText(getContext(), "从缓存中获取", Toast.LENGTH_SHORT).show();
        }
        getDatas();
        return view;
    }
    private void getDatas() {
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"SellerController");
        params.addBodyParameter("sellerop", "showtype2");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("数据", result);
                Toast.makeText(getContext(), "到这里了", Toast.LENGTH_SHORT).show();
                PrefUtils.setString(getContext(), "seller_lifu", result);
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
                Log.e("数据","网络访问结束语");
            }
        });
    }
    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Seller>>() {
        }.getType();
        List<Seller> sellerList= gson.fromJson(result, type);
        mList.clear();
        for (int i = 0; i < sellerList.size(); i++) {
            mList.add(sellerList.get(i));
            /*Log.e("数据456",sellerList.get(i).toString());*/
        }
        myAdapter.notifyDataSetChanged();
    }
}
