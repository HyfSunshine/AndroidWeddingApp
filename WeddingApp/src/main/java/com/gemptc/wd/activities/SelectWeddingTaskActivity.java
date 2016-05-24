package com.gemptc.wd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.FragmentApplication.R;

public class SelectWeddingTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wedding_task);
        Intent intent=getIntent();
    }

    public void setAddTask(View view) {
        //调回
        Intent intent=new Intent(SelectWeddingTaskActivity.this,HomeWeddingTaskActivity.class);
        startActivity(intent);
    }
}
