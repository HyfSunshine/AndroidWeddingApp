package com.gemptc.wd.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Gemptc on 2016/5/19.
 */
public class MerchantFragmentAdapter extends FragmentPagerAdapter {
List<Fragment> mFragments;
    public static String[] TITLES = new String[]
            { "婚纱礼服", "婚纱摄影", "婚礼戒指","婚宴酒店","婚车租赁","蜜月旅行" };

    public MerchantFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
