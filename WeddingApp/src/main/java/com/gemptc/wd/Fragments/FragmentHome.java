package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.wedding.R;
import com.bumptech.glide.Glide;
import com.gemptc.wd.activities.home.HomeFindMerchantActivity;
import com.gemptc.wd.activities.home.HomeWeddingTaskActivity;
import com.gemptc.wd.activities.home.HomeWeixinCadActivity;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.home.ProductDetailActivity;
import com.gemptc.wd.activities.home.SearchActivity;
import com.gemptc.wd.bean.ProductBean;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.StringUtils;
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
    private LinearLayout mLL_merchant,mLL_wedding_task,mLL_weixin_cad;
    private View view;
    private AMapLocationClient mLocationClient;
    private TextView mLocationTextView;
    private ImageButton mColthesImgBtn,mRingImgBtn,mHotelImgBtn,mImageButtonSearch;
    private Seller mSeller;
    private ProductBean mProduct;
    private ImageView mImgViewTravel1,mImgViewTravel2,mImgViewTravel3;
    private EditText mEditTextSearch;

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
        mLocationTextView= (TextView) view.findViewById(R.id.locationTxt);
        pagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
        initLocation();
        String result = PrefUtils.getString(getContext(), "home_product_lunbo", null);
        if (result != null) {
            parseData(result);
            Toast.makeText(getContext(), "轮播Json从缓存中获取", Toast.LENGTH_SHORT).show();
        }
        getDatas();
        //获取旅拍图片并显示
        getLvPaiImg();
        return view;
    }

    private void initLocation() {
        Log.e("location","jinru");
        mLocationClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.e("location",aMapLocation.toString());
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String location = StringUtils.extractLocation(city, district);
                        mLocationTextView.setText(location);
                        Log.e("location",city);
                        Log.e("location",location);
                    }
                }
            }
        });
        mLocationClient.startLocation();

    }


    private void initView() {
        mLL_merchant= (LinearLayout) view.findViewById(R.id.LL_merchant);
        mLL_wedding_task= (LinearLayout) view.findViewById(R.id.LL_wedding_task);
        mLL_weixin_cad= (LinearLayout) view.findViewById(R.id.LL_weixin_cad);
        mColthesImgBtn= (ImageButton) view.findViewById(R.id.ImgBtnClothes);
        mRingImgBtn= (ImageButton) view.findViewById(R.id.ImgBtnRing);
        mHotelImgBtn= (ImageButton) view.findViewById(R.id.ImgBtnHotel);
        mImgViewTravel1= (ImageView) view.findViewById(R.id.travltuijian1);
        mImgViewTravel2= (ImageView) view.findViewById(R.id.travltuijian2);
        mImgViewTravel3= (ImageView) view.findViewById(R.id.travltuijian3);
        mEditTextSearch= (EditText) view.findViewById(R.id.search_et_input);
        mImageButtonSearch= (ImageButton) view.findViewById(R.id.ImgBtnSearch);
    }

    private void initListeners() {
        HomeListener listener=new HomeListener();
        mLL_merchant.setOnClickListener(listener);
        mLL_wedding_task.setOnClickListener(listener);
        mLL_weixin_cad.setOnClickListener(listener);
        mColthesImgBtn.setOnClickListener(listener);
        mRingImgBtn.setOnClickListener(listener);
        mHotelImgBtn.setOnClickListener(listener);
        mImgViewTravel1.setOnClickListener(listener);
        mImgViewTravel2.setOnClickListener(listener);
        mImgViewTravel3.setOnClickListener(listener);
        mImageButtonSearch.setOnClickListener(listener);
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
                case R.id.ImgBtnClothes:
                    getTuiJianDatas("homeweddingdress");
                    break;
                case R.id.ImgBtnRing:
                    getTuiJianDatas("homeweddingring");
                    break;
                case R.id.ImgBtnHotel:
                    getTuiJianDatas("homehotel");
                    break;
                case R.id.travltuijian1:
                    getTuiJianDatas("hometravel1");
                    break;
                case R.id.travltuijian2:
                    getTuiJianDatas("hometravel2");
                    break;
                case R.id.travltuijian3:
                    getTuiJianDatas("hometravel3");
                    break;
                case R.id.ImgBtnSearch:
                    //跳到搜索页面
                    Intent intent=new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("searchtext",mEditTextSearch.getText().toString());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
    private void getLvPaiImg() {
        String result = PrefUtils.getString(getContext(), "hometravel", null);
        if (result != null) {
            parseLvPaiImgData(result);
        }
        requestLvPaiImgDatas();
    }

    private void requestLvPaiImgDatas() {
        RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
        params.addBodyParameter("productop", "hometravel");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                PrefUtils.setString(getContext(), "hometravel", result);
                parseLvPaiImgData(result);
                Log.e("travel",result);
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

    private void parseLvPaiImgData(String result) {
        Gson gson=new Gson();
        Type type=new TypeToken<List<ProductBean>>(){}.getType();
        List<ProductBean> productList=gson.fromJson(result,type);
        Glide.with(getContext())
                .load(UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(0).getPrListPicName())
                .thumbnail(0.5f)
                .into(mImgViewTravel1);
        Glide.with(getContext())
                .load(UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(1).getPrListPicName())
                .thumbnail(0.5f)
                .into(mImgViewTravel2);
        Glide.with(getContext())
                .load(UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(2).getPrListPicName())
                .thumbnail(0.5f)
                .into(mImgViewTravel3);

       /* x.image().bind(mImgViewTravel1,UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(0).getPrListPicName());
        x.image().bind(mImgViewTravel2,UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(1).getPrListPicName());
        x.image().bind(mImgViewTravel3,UrlAddress.PRODUCT_IMAGE_ADDRESS+productList.get(2).getPrListPicName());*/
    }
    private void getTuiJianDatas(String tuiJianName) {
        String result = PrefUtils.getString(getContext(), tuiJianName, null);
        if (result != null) {
            parseTuiJianData(result,tuiJianName);
        }
        requestTuiJianDatas(tuiJianName);
    }

    private void requestTuiJianDatas(final String tuiJianName) {
        if(tuiJianName.equals("hometravel1")||tuiJianName.equals("hometravel2")||tuiJianName.equals("hometravel3")){
            RequestParams params = new RequestParams(UrlAddress.PRODUCT_Controller);
            params.addBodyParameter("productop", "hometravel");
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    PrefUtils.setString(getContext(), tuiJianName, result);
                    parseTuiJianData(result,tuiJianName);
                    Log.e("travel",result);
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
        }else{

            RequestParams params = new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"ProductController");
            params.addBodyParameter("productop", tuiJianName);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    PrefUtils.setString(getContext(), tuiJianName, result);
                    parseTuiJianData(result,tuiJianName);
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

    }
    private void parseTuiJianData(String result,String tuiJianName) {
        if(tuiJianName.equals("hometravel1")||tuiJianName.equals("hometravel2")||tuiJianName.equals("hometravel3")){
            Gson gson=new Gson();
            Type type=new TypeToken<List<ProductBean>>(){}.getType();
            List<ProductBean> productList=gson.fromJson(result,type);
            Log.e("travel",productList.toString());
            if(tuiJianName.equals("hometravel1")){
                mProduct=productList.get(0);
                getSeller(mProduct.getSellerId());
                /*Intent intent=new Intent(getContext(),ProductDetailActivity.class);
                intent.putExtra("productDetail",mProduct);
                intent.putExtra("seller",mSeller);
                startActivity(intent);*/
            }else if(tuiJianName.equals("hometravel2")){
                mProduct=productList.get(1);
                getSeller(mProduct.getSellerId());
            }else if(tuiJianName.equals("hometravel3")){
                mProduct=productList.get(2);
                getSeller(mProduct.getSellerId());
            }
        }else{
            Gson gson = new Gson();
            mProduct=gson.fromJson(result,ProductBean.class);
            Log.e("seller",mProduct.getSellerId()+"");
            getSeller(mProduct.getSellerId());
            Log.e("seller",mSeller.toString());

        }
    }

    //获取推荐里的商家信息
    private void getSeller(int sellerId){
        String result = PrefUtils.getString(getContext(), sellerId+"hometuijian", null);
        if (result != null) {
            parseSellerData(result);
            Toast.makeText(getContext(), "tuijian从缓存中获取", Toast.LENGTH_SHORT).show();
        }
        requestSellerDatas(sellerId);
    }

    private void requestSellerDatas(final int sellerId) {
        Log.e("seller","jinru");
        RequestParams params = new RequestParams(UrlAddress.SELLER_Controller);
        params.addBodyParameter("sellerop", "detailsseller");
        params.addBodyParameter("sellerid",sellerId+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("seller","requestseller");
                PrefUtils.setString(getContext(),sellerId+"hometuijian", result);
                parseSellerData(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("seller",ex.toString());
            }
            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("seller","cancelled");
            }
            @Override
            public void onFinished() {
                Log.e("seller","finshed");
            }
        });

    }

    private void parseSellerData(String result) {
            Gson gson=new Gson();
            mSeller=gson.fromJson(result,Seller.class);
            Intent intent=new Intent(getContext(),ProductDetailActivity.class);
            intent.putExtra("productDetail",mProduct);
            intent.putExtra("seller",mSeller);
            startActivity(intent);
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

