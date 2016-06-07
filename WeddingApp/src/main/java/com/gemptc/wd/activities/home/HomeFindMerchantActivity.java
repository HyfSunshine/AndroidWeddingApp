package com.gemptc.wd.activities.home;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.adapter.SellerGridAdapter;
import com.gemptc.wd.adapter.SellerListAdapter;
import com.gemptc.wd.bean.Seller;
import com.gemptc.wd.bean.SellerData;
import com.gemptc.wd.utils.PrefUtils;
import com.gemptc.wd.utils.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFindMerchantActivity extends FragmentActivity {
    GridView mGridView;
    List<String> mlist=new ArrayList<>();
    SellerGridAdapter mSellerGridAdapter;
    PullToRefreshListView mListView;
    List<Seller> mSellerlist=new ArrayList<>();
    List<SellerData> mSellerData;
    SellerListAdapter mSellerListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_find_merchant);
        //初始化GridView
        initGridData();
        initGridview();
        initGridlistener();
        //初始化下拉刷新
        initListview();
        //商家分类和Params
        initSellerData();

        mSellerListAdapter=new SellerListAdapter(HomeFindMerchantActivity.this,mSellerlist);
        mListView.setAdapter(mSellerListAdapter);
        //初始化从网络上获取的数据
        clickGetClassData(0);
        //initListData();
        //initListlistener();

       /* Intent intent = getIntent();
        initView();*/
    }
    public void initRefreshListView() {
        ILoadingLayout startLabels = mListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新");
        startLabels.setRefreshingLabel("正在拉");
        startLabels.setReleaseLabel("放开刷新");
        ILoadingLayout endLabels = mListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载");
        endLabels.setRefreshingLabel("正在载入...");
        endLabels.setReleaseLabel("放开加载...");
    }

    //点击分类开始获取网络数据
    private void clickGetClassData(int selectClass){
        String cacheResult=PrefUtils.getString(this,mSellerData.get(0).getPreUtils(),null);
        if (cacheResult!=null){
            parseData(cacheResult);
        }
        getDataFromInternet(selectClass);
    }

    //解析数据的方法
    private void parseData(String result){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Seller>>() {
        }.getType();
        List<Seller> sellerList= gson.fromJson(result, type);
        mSellerlist.clear();
        for (int i = 0; i < sellerList.size(); i++) {
            mSellerlist.add(sellerList.get(i));
            Log.e("数据456",sellerList.get(i).toString());
        }
        mSellerListAdapter.notifyDataSetChanged();
    }
    //从网络上获取数据
    public void getDataFromInternet(final int selectClass) {
        String url = UrlAddress.SELLER_Controller;
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("sellerop",mSellerData.get(selectClass).getSellerOp());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //开始存储数据
                PrefUtils.setString(HomeFindMerchantActivity.this,mSellerData.get(selectClass).getPreUtils(),result);
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

    static class LoadDataAsyncTask extends AsyncTask<Void, Void, String> {//定义返回值的类型
        //后台处理
        private HomeFindMerchantActivity activity;

        public LoadDataAsyncTask(HomeFindMerchantActivity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(Void... params) {
            //用一个线程来模拟刷新
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //加载数据
            //activity.loadData();
            return "success";
        }

        //  onPostExecute（）是对返回的值进行操作
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ("success".equals(s)) {
                activity.mSellerListAdapter.notifyDataSetChanged();//通知数据集改变,界面刷新
                activity.mListView.onRefreshComplete();//表示刷新完成
            }
        }
    }
    private void initSellerData() {
        SellerData selDat1=new SellerData("seller1","showtype1");
        SellerData selDat2=new SellerData("seller2","showtype2");
        SellerData selDat3=new SellerData("seller3","showtype3");
        SellerData selDat4=new SellerData("seller4","showtype4");
        SellerData selDat6=new SellerData("seller6","showtype6");
        SellerData selDat5=new SellerData("seller5","showtype5");
        mSellerData=new ArrayList<>();
        mSellerData.add(selDat1);
        mSellerData.add(selDat2);
        mSellerData.add(selDat3);
        mSellerData.add(selDat4);
        mSellerData.add(selDat5);
        mSellerData.add(selDat6);

    }

    private void initListview() {
       mListView= (PullToRefreshListView) findViewById(R.id.sellerlist);

        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新时头部的状态
        initRefreshListView();
        //设置上拉和下拉时候的监听器
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //下拉时
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTask(HomeFindMerchantActivity.this).execute();//执行下载数据
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadDataAsyncTask(HomeFindMerchantActivity.this).execute();
            }
        });
    }



    private void initGridview() {
        mGridView= (GridView)findViewById(R.id.look_classfiy);
        int size = mlist.size();
        int length = 120;
        //使用DisplayMetrics获取屏幕参数
        DisplayMetrics dm = new DisplayMetrics();
       getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * length * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mGridView.setLayoutParams(layoutParams); // 设置GirdView布局参数,横向布局的关键
        mGridView.setColumnWidth(itemWidth); // 设置列表项宽
        mGridView.setHorizontalSpacing(5); // 设置列表项水平间距
        mGridView.setStretchMode(GridView.NO_STRETCH);//不拉伸
        mGridView.setNumColumns(size); // 设置列数量=列表集合数
        mGridView.setVerticalScrollBarEnabled(false);//设置滚动条不显示
        mSellerGridAdapter=new SellerGridAdapter(HomeFindMerchantActivity.this,mlist);
        mGridView.setAdapter(mSellerGridAdapter);
    }
    private void initGridData(){
        mlist.add("婚纱摄影");
        mlist.add("婚纱礼服");
        mlist.add("婚礼戒指");
        mlist.add("婚宴酒店");
        mlist.add("婚车租赁");
        mlist.add("蜜月旅行");

    }
    private void initGridlistener() {
       mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               mSellerlist.clear();
               switch (position){
                   case 0:
                       clickGetClassData(0);
                       break;
                   case 1:
                       clickGetClassData(1);
                       break;
                   case 2:
                       clickGetClassData(2);
                       break;
                   case 3:
                       clickGetClassData(3);
                       break;
                   case 4:
                       clickGetClassData(4);
                       break;
                   case 5:
                       clickGetClassData(5);
                       break;

               }
              /* mListView.notify();
               mSellerListAdapter.notifyDataSetChanged();*/
               mSellerListAdapter=new SellerListAdapter(HomeFindMerchantActivity.this,mSellerlist);
               mListView.setAdapter(mSellerListAdapter);
               mSellerListAdapter.notifyDataSetChanged();

           }
       });
    }

}
