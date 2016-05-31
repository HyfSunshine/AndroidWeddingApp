package com.gemptc.wd.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.wedding.R;
import com.gemptc.wd.utils.UrlAddress;
import com.viewpagerindicator.PageIndicator;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentHome extends Fragment{
    private ViewPager viewPager;
    private PageIndicator indicator;
    private List<String> imagesUrlList;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,null);
        viewPager= (ViewPager) view.findViewById(R.id.home_lunbo_viewpager);
        indicator= (PageIndicator) view.findViewById(R.id.indicator);

        //初始化图片地址
        initImagesURL();

        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.setViewPager(viewPager);

        //自动轮播
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    int currentPosition = viewPager.getCurrentItem();
                    if (currentPosition<imagesUrlList.size()-1){
                        currentPosition++;
                    }else{
                        currentPosition=0;
                    }
                    viewPager.setCurrentItem(currentPosition);
                    handler.sendEmptyMessageDelayed(0,3000);
                }
            }
        };

        handler.sendEmptyMessageDelayed(0,3000);
        return view;
    }

    private void initImagesURL() {
        imagesUrlList=new ArrayList<>();
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo1.jpg");
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo2.png");
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo3.jpg");
    }

    class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imagesUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            x.image().bind(image,imagesUrlList.get(position));
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
