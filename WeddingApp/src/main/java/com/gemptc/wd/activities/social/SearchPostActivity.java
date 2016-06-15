package com.gemptc.wd.activities.social;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.adapter.GridAdapter;
import com.gemptc.wd.adapter.ListAdapter;
import com.gemptc.wd.adapter.SearchRecyclerViewAdapter;
import com.gemptc.wd.bean.GridBean;
import com.gemptc.wd.bean.PostBean;
import com.gemptc.wd.bean.SearchBean;
import com.gemptc.wd.bean.SearchHistoryBean;
import com.gemptc.wd.utils.DividerItemDecoration;
import com.gemptc.wd.utils.HistoryTable;
import com.gemptc.wd.utils.MyHelper;
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

public class SearchPostActivity extends AppCompatActivity {
    //显示历史数据的条数
    public static final int HISTORY_NUM=10;
    //public static final int mWhat=911;
    //public static final int mFinish = 922;
    //创建数据库辅助类对象
    MyHelper myHelper;
    //声明一个数据库操作类
    SQLiteDatabase mDatabase;
    //声明一个游标对象
    Cursor mCursor;
    //清除历史记录按钮
    Button history_clear;
    String text;
    Toast mToast;
    //上下文对象
    private Context mContext;
    //数据库总量
    private List<SearchBean> mList;
    //搜索结果
    private List<SearchBean> resultList;
    //历史记录
    private List<SearchHistoryBean> historyList;
    //适配器
    SearchRecyclerViewAdapter mSearchRecyclerViewAdapter=null;
    ListAdapter mListAdapter;
//    GridAdapter mGridAdapter;
    //gridbean
//    List<GridBean> mGridList;
//    GridView mGridView;


    private ImageView search_back;
    private Button search_go;
    private EditText mEditText;
    ImageView search_clear;
    private boolean flag;
    private boolean notify;
    RecyclerView resultRecyclerView;
    ListView listView;
    LinearLayout result_Layout, now_Layout;
    InputMethodManager imm;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ((String)msg.obj){
                case "finish":
                    Toast.makeText(SearchPostActivity.this, "finish", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);
        initViews();
        initListeners();

    }

    //事件监听
    private void initListeners() {
        //监听清除数据按键
        history_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase=myHelper.getReadableDatabase();
                historyList.clear();
                String sql1="delete from historylist";
                String sql2 = "update sqlite_sequence set seq=0 where name='historylist'";
                mDatabase.execSQL(sql1);
                mDatabase.execSQL(sql2);
                mDatabase.close();
                showHistory();
                if (historyList.size()==0){
                    mListAdapter.notifyDataSetChanged();
                    history_clear.setText("没有历史数据可以清理");
                }
            }
        });
        //返回键
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //清除输入内容
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
                search_clear.setVisibility(View.GONE);
                result_Layout.setVisibility(View.GONE);
                //now_Layout.setVisibility(View.GONE);
            }
        });

        //开始搜索
        searchResult();


    }

    private void searchResult() {
        //开始搜索
        search_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    //隐藏软键盘
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                   // now_Layout.setVisibility(View.GONE);
                    result_Layout.setVisibility(View.VISIBLE);
                    search_clear.setVisibility(View.VISIBLE);
                    text=mEditText.getText().toString().trim();
                    //遍历hostory表中的数据
                    showHistoryData();
                    //判断是否需要插入历史记录
                    if (notify==true){
                        insertData();
                    }
                    getHttpMethod();
                }else if (!flag){
                    search_clear.setVisibility(View.GONE);
                    show("请输入话题");
                }
            }

            //请求http获取
            private void getHttpMethod() {
                RequestParams params=new RequestParams(UrlAddress.HOST_ADDRESS_PROJECT+"PostController");
                params.addBodyParameter("postop","searchpost");
                params.addBodyParameter("searchcontent",mEditText.getText().toString());
                x.http().get(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        parseDatas(result);
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




            //Toast内容
            private void show(String text) {
                if (mToast==null){
                    mToast=Toast.makeText(SearchPostActivity.this, text, Toast.LENGTH_SHORT);
                }else {
                    mToast.setText(text);
                }
                mToast.show();
            }

            //历史搜素记录
            private void insertData() {
                mDatabase=myHelper.getReadableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put("name",text);
                mDatabase.insert(HistoryTable.Field.TABLE_NAME,null,contentValues);
                //关闭连接
                mDatabase.close();
                //更新数据
                setAdapter();

            }

            //遍历history表
            private void showHistoryData() {
                mDatabase=myHelper.getReadableDatabase();
                //查询
                historyList.clear();
                mCursor = mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, null);
                while (mCursor.moveToNext()){
                    String name=mCursor.getString(mCursor.getColumnIndex(HistoryTable.Field.HISTORY_NAME));
                    SearchHistoryBean searchHistoryBean=new SearchHistoryBean(name);
                    historyList.add(searchHistoryBean);
                }
                if (historyList.size()==0){
                    notify=true;
                }else {
                    for (int i = 0; i <historyList.size() ; i++) {
                        if (historyList.get(i).getContent().equals(text)||historyList.size()> HISTORY_NUM){
                            notify=false;
                        }else {
                            notify=true;
                        }
                    }
                }
                mDatabase.close();
            }
        });

    }

    //解析数据
    private void parseDatas(String result) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<SearchBean>>() {
        }.getType();
        List<SearchBean> list = gson.fromJson(result, type);
        mList=new ArrayList<>();
        Log.e("result1",list.size()+"");
        for (int i = 0; i < list.size(); i++) {
            Log.e("result",list.get(i).getPostTitle());
            mList.add(list.get(i));
        }
        for (int i = 0; i <mList.size() ; i++) {
            if (mList.get(i).getPostTitle().contains(text.trim())){
                resultList.add(mList.get(i));
                Log.e("rusult",resultList.size()+"");
            }
        }
        //判断resultList的大小
        if (resultList.size()==0){
            //未匹配到结果,切换到之前的界面
            result_Layout.setVisibility(View.GONE);
            resultList.clear();
            //now_Layout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            Toast.makeText(SearchPostActivity.this, "抱歉，没有找到相关话题", Toast.LENGTH_SHORT).show();
        }
        if (mSearchRecyclerViewAdapter==null){
            mSearchRecyclerViewAdapter=new SearchRecyclerViewAdapter(resultList,SearchPostActivity.this);
            resultRecyclerView.setAdapter(mSearchRecyclerViewAdapter);
        }else {
            mSearchRecyclerViewAdapter.notifyDataSetChanged();
        }
        //接着判断editText内容
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.getText().toString().equals("")){
                    flag=false;
                    search_clear.setVisibility(View.GONE);
                    result_Layout.setVisibility(View.GONE);
                    //now_Layout.setVisibility(View.GONE);
                }else {
                    resultList.clear();
                    mList.clear();
                    //隐藏软键盘
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    searchResult();
                }

            }
        });
        Message message=new Message();
        message.obj="finish";
        mHandler.sendMessage(message);
        mSearchRecyclerViewAdapter.notifyDataSetChanged();
    }



    private void initViews() {
        //mGridView= (GridView) findViewById(R.id.hotGridView);
        //绑定gridView
        //gridViewSearch();
        history_clear= (Button) findViewById(R.id.clear_history);
        //创建数据库对象
        myHelper=new MyHelper(SearchPostActivity.this);
        //now_Layout= (LinearLayout) findViewById(R.id.now_linear);
        result_Layout= (LinearLayout) findViewById(R.id.result_linear);
        search_back= (ImageView) findViewById(R.id.search_back);
        search_go = (Button) findViewById(R.id.search_go);
        search_clear = (ImageView) findViewById(R.id.search_clear);
        mEditText = (EditText) findViewById(R.id.search_edit);
        listView = (ListView) findViewById(R.id.historyList);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycle);
        mList = new ArrayList<>();
        resultList = new ArrayList<>();
        historyList = new ArrayList<>();
        setAdapter();

        //将历史数据上的放在文本框上
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEditText.setText(historyList.get(position).getContent());
            }
        });
        //隐藏软键盘
        imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //强制隐藏键盘
        imm.hideSoftInputFromInputMethod(mEditText.getWindowToken(),0);

        //设置RecyclerView的布局管理
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SearchPostActivity.this);
        resultRecyclerView.setLayoutManager(linearLayoutManager);
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(SearchPostActivity.this, 1));

        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setFocusableInTouchMode(true);
                //将软键盘弹出
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
            }
        });
        //再判断内容是否被编辑
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.getText().toString().equals("")){
                    flag=false;
                }else {
                    search_clear.setVisibility(View.VISIBLE);
                    flag=true;
                }

            }
        });



    }

    private void setAdapter() {
        showHistory();
        if (mListAdapter==null){
            mListAdapter=new ListAdapter(historyList,SearchPostActivity.this);
            listView.setAdapter(mListAdapter);
        }else {
            mListAdapter.notifyDataSetChanged();
        }

    }

    private void showHistory() {
        mDatabase=myHelper.getReadableDatabase();
        //清空所有数据
        historyList.clear();
        //查询
        mCursor=mDatabase.query(HistoryTable.Field.TABLE_NAME, null, null, null, null, null, null);
        while (mCursor.moveToNext()){
            String name=mCursor.getString(mCursor.getColumnIndex((HistoryTable.Field.HISTORY_NAME)));
            SearchHistoryBean searchHistoryBean=new SearchHistoryBean(name);
            historyList.add(searchHistoryBean);
        }
        if (historyList.size()>0){
            history_clear.setText("清除历史搜索");
        }
        mDatabase.close();

    }

    //绑定gridView
//    private void gridViewSearch() {
//        mGridList=new ArrayList<>();
//        //假数据
//        for (int i = 0; i < 9; i++) {
//            GridBean gridBean=new GridBean();
//            gridBean.setText("热搜"+i);
//            mGridList.add(gridBean);
//        }
//        mGridAdapter=new GridAdapter(SearchPostActivity.this,mGridList);
//        mGridView.setAdapter(mGridAdapter);
//    }

    public void search_back(View view) {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }

}
