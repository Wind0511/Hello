package com.wind.server.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return sdf.format(cal.getTime()).toString();
    }
}
