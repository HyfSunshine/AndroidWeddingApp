package com.gemptc.wd.activities.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wedding.R;
import com.gemptc.wd.utils.ToastUtils;

public class EditUserNameSignActivity extends AppCompatActivity{

    private ImageView iv_update_back;
    private TextView headTitle,save;
    private EditText inputContent;
    private String hendContent;
    private int USER_NAME_OK=10;
    private int USER_SIGN_OK=20;
    private int USER_ADDRESS_OK=20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name_sign);
        initViews();
        initListener();
        Intent intent = getIntent();
        hendContent = intent.getStringExtra("headTitle");
        String firstName = intent.getStringExtra("firstContent");
        String inputHint = intent.getStringExtra("inputHint");
        headTitle.setText(hendContent);
        inputContent.setHint(inputHint);
        inputContent.setText(firstName);
    }

    private void initListener() {
        save.setOnClickListener(new View.OnClickListener() {
            Intent intent=new Intent();
            @Override
            public void onClick(View v) {
                hendContent=headTitle.getText().toString();
                intent.putExtra("data_return",inputContent.getText().toString());
                if ("".equals(inputContent.getText().toString())){
                    ToastUtils.shortToast(EditUserNameSignActivity.this,"不能为空哟");
                }else{
                    if ("编辑昵称".equals(hendContent)){
                        setResult(USER_NAME_OK,intent);
                    }else if ("编辑签名".equals(hendContent)){
                        setResult(USER_SIGN_OK,intent);
                    }else{
                        setResult(USER_ADDRESS_OK,intent);
                    }
                    finish();
                }
            }
        });

        iv_update_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        headTitle= (TextView) findViewById(R.id.tv_what);
        save= (TextView) findViewById(R.id.tv_save);
        inputContent= (EditText) findViewById(R.id.edt_inputContent);
        iv_update_back= (ImageView) findViewById(R.id.iv_update_back);
    }
}
