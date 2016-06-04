package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.wedding.R;
import com.gemptc.wd.activities.social.EditPostActivity;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.social.SearchPostActivity;
import com.gemptc.wd.activities.social.SocialFannaojiActivity;
import com.gemptc.wd.activities.social.SocialHuiyiluActivity;
import com.gemptc.wd.activities.social.SocialJinxingceActivity;
import com.gemptc.wd.activities.social.SocialShenghuojiActivity;



import com.gemptc.wd.utils.UrlAddress;
import com.viewpagerindicator.PageIndicator;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentSocial extends Fragment {
    public ViewPager viewPager;
    private PageIndicator indicator;
    public List<String> imagesUrlList;

    private View view;

    private ImageButton social_search, social_editpost;

    private LinearLayout social_huiyilu, social_jinxingce, social_shenghuoji, social_fannaoji;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.handler.sendEmptyMessageDelayed(1, 3000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, null);
        initView();
        initListeners();
        viewPager = (ViewPager) view.findViewById(R.id.social_lunbo_viewpager);
        indicator = (PageIndicator) view.findViewById(R.id.indicator);
        //初始化图片地址
        initImagesURL();

        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.setViewPager(viewPager);

        return view;
    }

    private void initListeners() {
        SocialListener listener = new SocialListener();
        social_search.setOnClickListener(listener);
        social_editpost.setOnClickListener(listener);

        social_huiyilu.setOnClickListener(listener);
        social_jinxingce.setOnClickListener(listener);
        social_shenghuoji.setOnClickListener(listener);
        social_fannaoji.setOnClickListener(listener);

    }

    private void initView() {
        social_search = (ImageButton) view.findViewById(R.id.social_search);
        social_editpost = (ImageButton) view.findViewById(R.id.social_editpost);

        social_huiyilu = (LinearLayout) view.findViewById(R.id.social_huiyilu);
        social_jinxingce = (LinearLayout) view.findViewById(R.id.social_jinxingce);
        social_shenghuoji = (LinearLayout) view.findViewById(R.id.social_shenghuoji);
        social_fannaoji = (LinearLayout) view.findViewById(R.id.social_fannaoji);
    }


    private void initImagesURL() {
        imagesUrlList = new ArrayList<>();
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS + "photo1.jpg");
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS + "photo2.png");
        imagesUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS + "photo3.jpg");
    }

    private class MyViewPagerAdapter extends PagerAdapter {
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


    class SocialListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.social_search:
                    startActivity(new Intent(getContext(), SearchPostActivity.class));
                    break;
                case R.id.social_editpost:
                    startActivity(new Intent(getContext(), EditPostActivity.class));
                    break;
                case R.id.social_huiyilu:
                    startActivity(new Intent(getContext(), SocialHuiyiluActivity.class));
                    break;
                case R.id.social_jinxingce:
                    startActivity(new Intent(getContext(), SocialJinxingceActivity.class));
                    break;

                case R.id.social_shenghuoji:
                    startActivity(new Intent(getContext(), SocialShenghuojiActivity.class));
                    break;

                case R.id.social_fannaoji:
                    startActivity(new Intent(getContext(), SocialFannaojiActivity.class));
                    break;
                default:
                    break;
            }
        }
    }
}



