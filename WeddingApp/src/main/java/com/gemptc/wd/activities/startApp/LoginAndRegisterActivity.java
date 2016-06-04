package com.gemptc.wd.activities.startApp;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.wedding.R;
import com.gemptc.wd.utils.UrlAddress;
import com.viewpagerindicator.PageIndicator;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class LoginAndRegisterActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PageIndicator indicator;
    private List<String> imageUrlList;

    public static LoginAndRegisterActivity loginAndRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        loginAndRegister=this;

        viewPager= (ViewPager) this.findViewById(R.id.viewPager);
        indicator= (PageIndicator) this.findViewById(R.id.indicator);
        //初始化图片地址集合
        initImageUrl();

        viewPager.setAdapter(new MyViewPagerAdapter());
        indicator.setViewPager(viewPager);
    }

    private void initImageUrl() {
        imageUrlList=new ArrayList<>();
        imageUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo1.jpg");
        imageUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo2.png");
        imageUrlList.add(UrlAddress.LOGIN_IMAGE_ADDRESS+"photo3.jpg");
    }


    class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(LoginAndRegisterActivity.this);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            x.image().bind(image,imageUrlList.get(position));
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    //两个按钮的监听事件
    //1.跳转到登录的界面
    public void login(View view) {
        startActivity(new Intent(LoginAndRegisterActivity.this,LoginActivity.class));
    }
    //2.跳转到注册的界面
    public void register(View view) {
        startActivity(new Intent(LoginAndRegisterActivity.this,RegisterActivity.class));
    }
}
