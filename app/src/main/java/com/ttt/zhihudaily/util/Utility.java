package com.ttt.zhihudaily.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utility {

    /**
     * 获得前i天的日期
     * @param i 今天往前的天数。i=0为今天
     * @return
     */
    public static String getDate(int i, Boolean isFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -i);
        SimpleDateFormat simpleDateFormat;
        if (isFormat) {
            simpleDateFormat = new SimpleDateFormat("yyyy年M月d日");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        return simpleDateFormat.format(calendar.getTime());
    }
}
