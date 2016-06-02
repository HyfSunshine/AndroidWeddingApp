package com.gemptc.wd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.wedding.R;
import com.gemptc.wd.bean.Seller;

import java.util.List;

/**
 * Created by Gemptc on 2016/5/19.
 */
public class LiFuFragment extends Fragment {
    private ListView mListView;
    //找数据
    private List<Seller> mSellerList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.tab_lifu, container, false);
        mListView= (ListView) view.findViewById(R.id.listView);
        //找数据，并解析出数据存到一个bean类里
        return view;

    }
}
