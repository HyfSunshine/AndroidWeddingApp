package com.gemptc.wd.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by C5-0 on 2016/6/12.
 */
public class DateUtil {
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
