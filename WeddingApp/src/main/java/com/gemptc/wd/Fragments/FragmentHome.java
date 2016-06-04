package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.activities.HomeFindMerchantActivity;
import com.gemptc.wd.activities.HomeWeddingTaskActivity;
import com.gemptc.wd.activities.HomeWeixinCadActivity;
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
    private View view;
//    private Handler handler;
private ImageButton mImgbtnMerchant,mImgbtnWeddingTask,mImgbtnWeixinCard;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initListeners();
        imagesUrlList = new ArrayList<>();
        viewPager = (ViewPager) view.findViewById(R.id.home_lunbo_viewpager);
        indicator = (PageIndicator) view.findViewById(R.id.indicator);
        pagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        String result = PrefUtils.getString(getContext(), "home_product_lunbo", null);
        if (result != null) {
            parseData(result);
            Toast.makeText(getContext(), "轮播Json从缓存中获取", Toast.LENGTH_SHORT).show();
        }
        getDatas();
        return view;
    }

    private void initView() {
        mImgbtnMerchant= (ImageButton) view.findViewById(R.id.imgbtn_merchant);
        mImgbtnWeddingTask= (ImageButton) view.findViewById(R.id.imgbtn_wedding_task);
        mImgbtnWeixinCard= (ImageButton) view.findViewById(R.id.imgbtn_weixin_card);

    }

    private void initListeners() {
        HomeListener listener=new HomeListener();
        mImgbtnMerchant.setOnClickListener(listener);
        mImgbtnWeddingTask.setOnClickListener(listener);
        mImgbtnWeixinCard.setOnClickListener(listener);
    }
    class HomeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgbtn_merchant:
                    startActivity(new Intent(getContext(),HomeFindMerchantActivity.class));
                    break;
                case R.id.imgbtn_wedding_task:
                    startActivity(new Intent(getContext(),HomeWeddingTaskActivity.class));
                    break;
                case R.id.imgbtn_weixin_card:
                    startActivity(new Intent(getContext(), HomeWeixinCadActivity.class));
                    break;
                default:
                    break;
            }
        }
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
        RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
        params.addBodyParameter("productop", "homesheying");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
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

