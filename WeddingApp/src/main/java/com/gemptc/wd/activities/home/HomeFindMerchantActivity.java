package com.gemptc.wd.activities.home;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.Window;

import com.android.wedding.R;

public class HomeFindMerchantActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_find_merchant);
        Intent intent = getIntent();
        initView();
    }

    private void initView() {

    }

}
