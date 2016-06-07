package com.gemptc.wd.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ToastUtils {
    public static void longToast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context,String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
