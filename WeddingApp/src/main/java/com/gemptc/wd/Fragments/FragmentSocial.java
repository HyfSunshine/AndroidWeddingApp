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
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.social.SearchPostActivity;
import com.gemptc.wd.activities.social.SocialFannaojiActivity;
import com.gemptc.wd.activities.social.SocialFannaojiDetailActivity;
import com.gemptc.wd.activities.social.SocialHuiyiluActivity;
import com.gemptc.wd.activities.social.SocialHuiyiluDetailActivity;
import com.gemptc.wd.activities.social.SocialJinxingceActivity;
import com.gemptc.wd.activities.social.SocialJinxingceDetailActivity;
import com.gemptc.wd.activities.social.SocialShenghuojiActivity;
import com.gemptc.wd.activities.social.SocialShenghuojiDetailActivity;
import com.gemptc.wd.bean.PostBean;
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
public class FragmentSocial extends Fragment {
    public ViewPager viewPager;
    private PageIndicator indicator;
    public List<String> imagesUrlList;

   PostBean mpostBean;

    private View view;

    private ImageButton social_search;

    private TextView tv_huiyilu_tuijian,tv_jinxingce_tuijian,tv_shenghuoji_tuijian,tv_fannaoji_tuijian;

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
        //初始化图片地址，轮播图
        initImagesURL();

        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.setViewPager(viewPager);
        //选择
        choosepostop();
        return view;
    }

    private void choosepostop() {
        gettuijianDatas("gettuijian1");
        gettuijianDatas("gettuijian2");
        gettuijianDatas("gettuijian3");
        gettuijianDatas("gettuijian4");
    }


    //获取推荐模块的内容
    private void gettuijianDatas(final String postop) {
        RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
        params.addBodyParameter("postop",postop);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PrefUtils.setString(getActivity(),"social_"+postop,result);
                parseData(result,postop);
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

    private void parseData(String result,String postop) {
        Gson gson = new Gson();
        mpostBean= gson.fromJson(result, PostBean.class);
       if (postop.equals("gettuijian1")){
            tv_huiyilu_tuijian.setText(mpostBean.getPostTitle());
        }
        if (postop.equals("gettuijian2")){
            tv_jinxingce_tuijian.setText(mpostBean.getPostTitle());
        }
        if (postop.equals("gettuijian3")){
            tv_shenghuoji_tuijian.setText(mpostBean.getPostTitle());
        }
        if (postop.equals("gettuijian4")){
            tv_fannaoji_tuijian.setText(mpostBean.getPostTitle());
        }
    }

    private void initListeners() {
        SocialListener listener = new SocialListener();
        social_search.setOnClickListener(listener);
        //四大推荐板块的监听事件
        tv_huiyilu_tuijian.setOnClickListener(listener);
        tv_jinxingce_tuijian.setOnClickListener(listener);
        tv_shenghuoji_tuijian.setOnClickListener(listener);
        tv_fannaoji_tuijian.setOnClickListener(listener);
        //四大板块的监听事件
        social_huiyilu.setOnClickListener(listener);
        social_jinxingce.setOnClickListener(listener);
        social_shenghuoji.setOnClickListener(listener);
        social_fannaoji.setOnClickListener(listener);

    }

    private void initView() {
        //婚礼说界面的搜索
        social_search = (ImageButton) view.findViewById(R.id.social_search);
        //四大推荐板块
        tv_huiyilu_tuijian= (TextView) view.findViewById(R.id.huiyilu_tuijian);
        tv_jinxingce_tuijian= (TextView) view.findViewById(R.id.jinxingce_tuijian);
        tv_shenghuoji_tuijian= (TextView) view.findViewById(R.id.shenghuoji_tuijian);
        tv_fannaoji_tuijian= (TextView) view.findViewById(R.id.fannaoji_tuijian);
        //四大板块
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
                //四大推荐
                case R.id.huiyilu_tuijian:
                    Intent intent5=new Intent(getActivity(), SocialHuiyiluDetailActivity.class);
                    intent5.putExtra("posthuiyilu",parseCache(PrefUtils.getString(getActivity(),"social_gettuijian1",null)));
                    getActivity().startActivity(intent5);
                    break;
                case R.id.jinxingce_tuijian:
                    Intent intent6=new Intent(getActivity(), SocialJinxingceDetailActivity.class);
                    intent6.putExtra("postjinxingce",parseCache(PrefUtils.getString(getActivity(),"social_gettuijian2",null)));
                    getActivity().startActivity(intent6);
                    break;
                case R.id.shenghuoji_tuijian:
                    Intent intent7=new Intent(getActivity(), SocialShenghuojiDetailActivity.class);
                    intent7.putExtra("postshenghuoji",parseCache(PrefUtils.getString(getActivity(),"social_gettuijian3",null)));
                    getActivity().startActivity(intent7);
                    break;
                case R.id.fannaoji_tuijian:
                    Intent intent8=new Intent(getActivity(), SocialFannaojiDetailActivity.class);
                    intent8.putExtra("postfannaoji",parseCache(PrefUtils.getString(getActivity(),"social_gettuijian4",null)));
                    getActivity().startActivity(intent8);
                    break;
                //四大板块
                case R.id.social_huiyilu:
                    Intent intent1=new Intent(getContext(), SocialHuiyiluActivity.class);
                    intent1.putExtra("postselection",1);
                    startActivity(intent1);
                    break;
                case R.id.social_jinxingce:
                    Intent intent2=new Intent(getContext(), SocialJinxingceActivity.class);
                    intent2.putExtra("postselection",2);
                    startActivity(intent2);
                    break;

                case R.id.social_shenghuoji:
                    Intent intent3=new Intent(getContext(), SocialShenghuojiActivity.class);
                    intent3.putExtra("postselection",3);
                    startActivity(intent3);
                    break;

                case R.id.social_fannaoji:
                    Intent intent4=new Intent(getContext(), SocialFannaojiActivity.class);
                    intent4.putExtra("postselection",4);
                    startActivity(intent4);
                    break;
                default:
                    break;
            }
        }
    }


    private PostBean parseCache(String s){
        Gson gson = new Gson();
        return gson.fromJson(s,PostBean.class);
    }
}



