package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.wedding.R;
import com.gemptc.wd.activities.kind.AllProductKindActivity;
import com.gemptc.wd.adapter.KindGuessLikeAdapter;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.ToastUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.gemptc.wd.utils.Utility;
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
public class FragmentKinds extends Fragment{
    private View view;
    private LinearLayout ll_sheying,ll_lifu,ll_jiezhi,ll_jiudian,ll_hunche,ll_miyue;
    private ListView lv_home_kind;
    private ScrollView scrollView_kind;
    private ImageView iv_searchProduct;
    private EditText edt_searchProduct;

    private List<ProductBean> productList;
    private KindGuessLikeAdapter kindGuessLikeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kinds,null);
        initView();
        initData();
        initListener();
        //从缓存中读取
        String kind_guess_fivelike = PrefUtils.getString(getActivity(),"kind_guess_fivelike",null);
        if (kind_guess_fivelike!=null){
            parseData(kind_guess_fivelike);
        }
        getGuessLikeData();
        return view;
    }

    private void initView() {
        ll_sheying= (LinearLayout) view.findViewById(R.id.ll_sheying);
        ll_lifu= (LinearLayout) view.findViewById(R.id.ll_lifu);
        ll_jiezhi= (LinearLayout) view.findViewById(R.id.ll_jiezhi);
        ll_jiudian= (LinearLayout) view.findViewById(R.id.ll_jiudian);
        ll_hunche= (LinearLayout) view.findViewById(R.id.ll_hunche);
        ll_miyue= (LinearLayout) view.findViewById(R.id.ll_miyue);

        lv_home_kind= (ListView) view.findViewById(R.id.lv_home_kind);
        scrollView_kind= (ScrollView) view.findViewById(R.id.scrollView_kind);

        //搜索案例
        edt_searchProduct= (EditText) view.findViewById(R.id.edt_searchProduct);
        iv_searchProduct= (ImageView) view.findViewById(R.id.iv_searchProduct);

    }

    private void initData() {
        productList=new ArrayList<>();
        kindGuessLikeAdapter = new KindGuessLikeAdapter(getActivity(),productList);
        lv_home_kind.setAdapter(kindGuessLikeAdapter);
    }

    private void initListener() {
        MyListener listener = new MyListener();
        ll_sheying.setOnClickListener(listener);
        ll_lifu.setOnClickListener(listener);
        ll_jiezhi.setOnClickListener(listener);
        ll_jiudian.setOnClickListener(listener);
        ll_hunche.setOnClickListener(listener);
        ll_miyue.setOnClickListener(listener);
        iv_searchProduct.setOnClickListener(listener);
    }

    private void getGuessLikeData() {
        RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
        params.addBodyParameter("productop","clickclass");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //获取到猜你喜欢的5条数据,进行缓存存储起来
                PrefUtils.setString(getActivity(),"kind_guess_fivelike",result);
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
        kindGuessLikeAdapter.notifyDataSetChanged();
        scrollView_kind.scrollTo(0,0);
        Utility.setListViewHeightBasedOnChildren(lv_home_kind);
    }


    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //分类【婚纱摄影】跳转
                case R.id.ll_sheying:
                    Intent intent1 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent1.putExtra("headTitle","婚纱摄影");
                    intent1.putExtra("productop","showtype1");
                    startActivity(intent1);
                break;
                case R.id.ll_lifu:
                    Intent intent2 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent2.putExtra("headTitle","婚纱礼服");
                    intent2.putExtra("productop","showtype2");
                    startActivity(intent2);
                    break;
                case R.id.ll_jiezhi:
                    Intent intent3 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent3.putExtra("headTitle","婚礼戒指");
                    intent3.putExtra("productop","showtype3");
                    startActivity(intent3);
                    break;
                case R.id.ll_jiudian:
                    Intent intent4 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent4.putExtra("headTitle","婚宴酒店");
                    intent4.putExtra("productop","showtype4");
                    startActivity(intent4);
                    break;
                case R.id.ll_hunche:
                    Intent intent5 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent5.putExtra("headTitle","婚车租赁");
                    intent5.putExtra("productop","showtype5");
                    startActivity(intent5);
                    break;
                case R.id.ll_miyue:
                    Intent intent6 = new Intent(getActivity(),AllProductKindActivity.class);
                    intent6.putExtra("headTitle","蜜月旅行");
                    intent6.putExtra("productop","showtype6");
                    startActivity(intent6);
                    break;
                //点击搜索时候的监听
                case R.id.iv_searchProduct:
                    if (!"".equals(edt_searchProduct.getText().toString())) {
                        Intent intent7 = new Intent(getActivity(), AllProductKindActivity.class);
                        intent7.putExtra("headTitle", "搜索结果");
                        intent7.putExtra("productop", "searchproduct");
                        intent7.putExtra("searchcontent", edt_searchProduct.getText().toString());
                        startActivity(intent7);
                    }else {
                        ToastUtils.shortToast(getActivity(),"搜索内容不能为空");
                    }
                    break;
            }
        }
    }
}
