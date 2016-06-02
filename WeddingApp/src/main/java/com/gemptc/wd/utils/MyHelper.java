package com.gemptc.wd.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class MyHelper extends SQLiteOpenHelper {
    public static final String NAME =  "history_list.db";
    public static final int VERSION=1;
    public static final String CREATE_TABLE = "create table " + HistoryTable.Field.TABLE_NAME +
            "(" + HistoryTable.Field._ID +
            " integer primary key autoincrement,"  + HistoryTable.Field.HISTORY_NAME +
            " text)";


    public MyHelper(Context context) {
        super(context,  NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
