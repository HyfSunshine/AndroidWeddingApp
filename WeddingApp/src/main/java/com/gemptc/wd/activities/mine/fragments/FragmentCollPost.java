package com.gemptc.wd.activities.mine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.activities.mine.adapter.CollePostAdapter;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentCollPost extends Fragment {

    private View view;
    private ListView postListView;
    private List<PostBean> postList;
    private CollePostAdapter postAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.mine_focus_coll_listview,null);
        initView();
        initData();
        String user_coll_post=PrefUtils.getString(getActivity(),"user_coll_post",null);
        if (user_coll_post!=null){
            parseData(user_coll_post);
        }
        getUserCollPostData();
        return view;
    }

    private void initView() {
        postListView= (ListView) view.findViewById(R.id.lv_focus_coll);
    }

    private void initData() {
        postList=new ArrayList<>();
        postAdapter=new CollePostAdapter(getActivity(),postList);
        postListView.setAdapter(postAdapter);

    }

    private void getUserCollPostData() {
        RequestParams params  = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"UserFourCollServlet");
        params.addBodyParameter("userop","CollectPostList");
        params.addBodyParameter("userid", PrefUtils.getString(getActivity(),"user_self_id",null));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //存储在本地
                PrefUtils.setString(getActivity(),"user_coll_post",result);
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

    private void parseData(String result){
        Gson gson = new Gson();
        Type type = new TypeToken<List<PostBean>>(){}.getType();
        List<PostBean> postBeanList = gson.fromJson(result,type);
        postList.clear();
        for (int i = 0; i < postBeanList.size(); i++) {
            postList.add(postBeanList.get(i));
        }
    }
}

