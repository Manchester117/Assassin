package com.highpin.core.utitlity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peng.Zhao on 2017/8/1.
 */
public class CommonTools {
    public static String getNowTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return sdf.format(date);
    }
}
