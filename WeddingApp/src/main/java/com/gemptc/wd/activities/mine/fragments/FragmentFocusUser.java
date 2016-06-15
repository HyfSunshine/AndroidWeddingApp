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
import com.gemptc.wd.activities.mine.adapter.FocusUserAdapter;
import com.gemptc.wd.bean.UserBean;
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
public class FragmentFocusUser extends Fragment {

    private View view;
    private ListView lv_focusUser;
    private List<UserBean> userList;
    private FocusUserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getContext(), R.layout.mine_focus_coll_listview,null);
        initView();
        initData();
        String user_focus_user = PrefUtils.getString(getActivity(),"user_focus_user",null);
        if (user_focus_user!=null){
            parseData(user_focus_user);
        }
        getFocusUserData();
        return view;
    }

    //从网络上获取数据的方法
    private void getFocusUserData() {
        //获取到用户的ID
        String userid = PrefUtils.getString(getActivity(),"user_self_id",null);

        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"UserFourCollServlet");
        params.addBodyParameter("userop","CollectUserList");
        params.addBodyParameter("userid",userid);
        x.http().post(params,new MyCommonCallBack());
    }

    //解析数据的方法
    private void parseData(String result){
        Gson gson = new Gson();
        Type type = new TypeToken<List<UserBean>>(){}.getType();
        List<UserBean>userBeanList = gson.fromJson(result,type);
        userList.clear();
        for (int i = 0; i < userBeanList.size(); i++) {
            userList.add(userBeanList.get(i));
        }
        //通知适配器更新数据(在adapter中提醒适配器更新)
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        userList=new ArrayList<>();
        adapter = new FocusUserAdapter(getActivity(),userList);
        lv_focusUser.setAdapter(adapter);
    }

    private void initView() {
        lv_focusUser= (ListView) view.findViewById(R.id.lv_focus_coll);
    }

    class MyCommonCallBack implements Callback.CommonCallback<String>{

        @Override
        public void onSuccess(String result) {
            Log.e("获取用户关注用户信息",result);

            //存储数据
            PrefUtils.setString(getActivity(),"user_focus_user",result);
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
    }


}

