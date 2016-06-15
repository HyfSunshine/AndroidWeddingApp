package com.gemptc.wd.activities.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.wedding.R;

public class RemindActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);
        //声音
        final MediaPlayer player=MediaPlayer.create(this,R.raw.ten);
        player.start();
        //震动
        //对话框
        Dialog d3=new AlertDialog.Builder(this)
                .setTitle("闹钟")
                .setIcon(R.mipmap.icon_task_remind)
                .setMessage("主人有结婚任务要做啊")
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        player.release();
                        finish();

                    }
                }).show();
        //启动服务


}
}
