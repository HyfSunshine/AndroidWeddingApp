package com.gemptc.wd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.wedding.R;
import com.gemptc.wd.activities.MainActivity;
import com.gemptc.wd.activities.mine.MineDetalActivity;

/**
 * Created by Administrator on 2016/5/5.
 */
public class FragmentMine extends Fragment{
    private LinearLayout ll_mine_detal;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,null);
        initView();
        initListener();
        return view;
    }

    private void initView(){
        ll_mine_detal= (LinearLayout) view.findViewById(R.id.ll_mine_detal);
    }
    private void initListener() {
        MyListener listener=new MyListener();
        ll_mine_detal.setOnClickListener(listener);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_mine_detal:
                startActivity(new Intent(MainActivity.mainActivity, MineDetalActivity.class));
                break;
            }
        }
    }
}
