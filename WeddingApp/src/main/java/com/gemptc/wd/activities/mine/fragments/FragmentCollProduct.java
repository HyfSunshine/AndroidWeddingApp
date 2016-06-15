package com.gemptc.wd.activities.mine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.activities.mine.adapter.ColleProductAdapter;
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


/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentCollProduct extends Fragment {

    private View view;
    private ListView productListView;
    private List<ProductBean> productList;
    private ColleProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.mine_focus_coll_listview,null);
        initView();
        initData();

        String cache_user_product = PrefUtils.getString(getActivity(),"cache_user_product",null);
        if (cache_user_product!=null){
            parseData(cache_user_product);
        }
        getUserCollProData();
        return view;
    }

    private void getUserCollProData() {
        //获取到用户的ID
        String userid = PrefUtils.getString(getActivity(),"user_self_id",null);
        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"UserFourCollServlet");
        params.addBodyParameter("userop","CollectProductList");
        params.addBodyParameter("userid",userid);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //获取到数据后将数据存至缓存
                PrefUtils.setString(getActivity(),"cache_user_product",result);
                //解析数据
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

    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductBean>>(){}.getType();
        List<ProductBean> productBeanList = gson.fromJson(result,type);
        productList.clear();
        for (int i = 0; i < productBeanList.size(); i++) {
            productList.add(productBeanList.get(i));
        }
        productAdapter.notifyDataSetChanged();
    }

    private void initData() {
        productList=new ArrayList<>();
        productAdapter = new ColleProductAdapter(getActivity(),productList);
        productListView.setAdapter(productAdapter);
    }

    private void initView() {
        productListView= (ListView) view.findViewById(R.id.lv_focus_coll);
    }
}

