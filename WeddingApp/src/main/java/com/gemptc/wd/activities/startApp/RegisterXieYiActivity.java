package com.gemptc.wd.activities.startApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.wedding.R;

public class RegisterXieYiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_xieyi);
    }

    public void fanhui(View view) {
        finish();
    }
}
