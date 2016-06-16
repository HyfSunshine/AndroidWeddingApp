package com.gemptc.wd.activities.mine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.activities.mine.adapter.FocusSellerAdapter;
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
 * Created by Administrator on 2016/5/5.
 */
public class FragmentFocusSeller extends Fragment {

    private View view;
    private ListView sellerListview;
    private List<Seller> sellerList;
    private FocusSellerAdapter sellerAdapter;

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.mine_focus_coll_listview,null);
        //初始化界面
        initView();
        initData();

        //判断是否有缓存
        String cache_User_Seller = PrefUtils.getString(getActivity(),"cache_User_Seller",null);
        if (cache_User_Seller!=null){
            //解析数据
            parseData(cache_User_Seller);
        }
        //获取网络数据
        getUserFocusSellerData();
        return view;
        }

    private void getUserFocusSellerData() {
        //获取到用户的ID
        String userid = PrefUtils.getString(getActivity(),"user_self_id",null);

        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"UserFourCollServlet");
        params.addBodyParameter("userop","CollectSellerList");
        params.addBodyParameter("userid",userid);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("获取用户关注商家信息",result);

                PrefUtils.setString(getActivity(),"cache_User_Seller",result);
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
        Type type = new TypeToken<List<Seller>>(){}.getType();
        List<Seller> sellerBeanList = gson.fromJson(result,type);
        sellerList.clear();
        for (int i = 0; i < sellerBeanList.size(); i++) {
            sellerList.add(sellerBeanList.get(i));
        }
        sellerAdapter.notifyDataSetChanged();
    }

    private void initData() {
        sellerList=new ArrayList<>();
        sellerAdapter = new FocusSellerAdapter(getActivity(),sellerList);
        sellerListview.setAdapter(sellerAdapter);
    }

    private void initView() {
        sellerListview= (ListView) view.findViewById(R.id.lv_focus_coll);
    }


}

