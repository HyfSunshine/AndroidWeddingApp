package com.gemptc.wd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.wedding.R;
<<<<<<< HEAD
import com.gemptc.wd.activities.home.HomeFindMerchantActivity;
import com.gemptc.wd.activities.home.HomeWeddingTaskActivity;
import com.gemptc.wd.activities.home.HomeWeixinCadActivity;
=======
>>>>>>> 6e2149986941524f80c5d5115a5e860d19a3a0a0
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.PageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentHome extends Fragment {
    public ViewPager viewPager;
    private PageIndicator indicator;
    public List<String> imagesUrlList;
    private List<ProductBean> listProduct;
    private MyViewPagerAdapter pagerAdapter;
<<<<<<< HEAD
    private View view;
//    private Handler handler;
    private LinearLayout mLL_merchant,mLL_wedding_task,mLL_weixin_cad;
=======
    //private Handler handler;

>>>>>>> 6e2149986941524f80c5d5115a5e860d19a3a0a0
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        imagesUrlList = new ArrayList<>();
        viewPager = (ViewPager) view.findViewById(R.id.home_lunbo_viewpager);
        indicator = (PageIndicator) view.findViewById(R.id.indicator);
        pagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        String result = PrefUtils.getString(getContext(), "home_product_lunbo", null);
        if (result != null) {
            parseData(result);
<<<<<<< HEAD
            Toast.makeText(getContext(), "轮播Json从缓存中获取", Toast.LENGTH_SHORT).show();
        }
        getDatas();
        return view;
    }

    private void initView() {
        mLL_merchant= (LinearLayout) view.findViewById(R.id.LL_merchant);
        mLL_wedding_task= (LinearLayout) view.findViewById(R.id.LL_wedding_task);
        mLL_weixin_cad= (LinearLayout) view.findViewById(R.id.LL_weixin_cad);
    }

    private void initListeners() {
        HomeListener listener=new HomeListener();
        mLL_merchant.setOnClickListener(listener);
        mLL_wedding_task.setOnClickListener(listener);
        mLL_weixin_cad.setOnClickListener(listener);
    }
    class HomeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.LL_merchant:
                    startActivity(new Intent(getContext(),HomeFindMerchantActivity.class));
                    break;
                case R.id.LL_wedding_task:
                    startActivity(new Intent(getContext(),HomeWeddingTaskActivity.class));
                    break;
                case R.id.LL_weixin_cad:
                    startActivity(new Intent(getContext(), HomeWeixinCadActivity.class));
                    break;
                default:
                    break;
            }
        }
=======

            Toast.makeText(getContext(), "轮播Json从缓存中获取", Toast.LENGTH_SHORT).show();

            //Toast.makeText(getContext(), "从缓存中获取", Toast.LENGTH_SHORT).show();

        } else
            getDatas();
        return view;
>>>>>>> 6e2149986941524f80c5d5115a5e860d19a3a0a0
    }

    private void initImagesURL() {
        imagesUrlList.clear();
        imagesUrlList.add(UrlAddress.PRODUCT_IMAGE_ADDRESS + listProduct.get(0).getPrBgroundName());
        imagesUrlList.add(UrlAddress.PRODUCT_IMAGE_ADDRESS + listProduct.get(1).getPrBgroundName());
        imagesUrlList.add(UrlAddress.PRODUCT_IMAGE_ADDRESS + listProduct.get(2).getPrBgroundName());
        pagerAdapter.notifyDataSetChanged();
    }

    //获取网络数据
    private void getDatas() {

        RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"ProductController");

        //RequestParams params = new RequestParams("http://10.201.1.9:8080/WeddingJson/ProductController");

        params.addBodyParameter("productop", "homesheying");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {


                Log.e("数据", result);

                PrefUtils.setString(getContext(), "home_product_lunbo", result);
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

//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo1.jpg");
//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo2.png");
//        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo3.jpg");
    }

    //解析数据
    private void parseData(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProductBean>>() {
        }.getType();
        listProduct = gson.fromJson(result, type);
        //初始化图片地址
        initImagesURL();
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imagesUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            x.image().bind(image, imagesUrlList.get(position));
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

