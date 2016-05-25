package com.gemptc.wd.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.FragmentApplication.R;
import com.gemptc.wd.tools.DateTimePickDialogUtil;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    RelativeLayout mfinishTime;
    RelativeLayout mremindTime;
    RelativeLayout mdescripeText;
    ImageView mDescripeImageView;
    TextView mDescripeTextView;
    Calendar c = Calendar.getInstance();
    String remindTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Intent intent=getIntent();
        initView();
        initListener();
    }

    private void initListener() {
        mfinishTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        Toast.makeText(EditTaskActivity.this,DateFormat.format("yyy-MM-dd", c), Toast.LENGTH_SHORT).show();
                        /*et1.setText(DateFormat.format("yyy-MM-dd", c));*/
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        mremindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        EditTaskActivity.this, "2016年4月23日 17:44");
                dateTimePicKDialog.dateTimePicKDialog();
                remindTime=dateTimePicKDialog.getDateTime();
            }
        });

    }

    private void initView() {
        mfinishTime= (RelativeLayout) findViewById(R.id.finishTimeRl);
        mremindTime= (RelativeLayout) findViewById(R.id.remindTimeRL);
    }

}
