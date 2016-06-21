package choosetime.com.example.chen.mycalendar.calendar.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    private static final long INTERVAL_IN_MILLISECONDS = 30000L;

    public static final String FORMAT_DATETIME_MS = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_DATE = "yyyy-MM-dd";

    public static final String FORMAT_DATEHR = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_DATE_YEAR = "yyyy-MM-dd";

    public static final String FORMAT_TIME = "HH:mm";

    public static final String FORMAT_MIN_SCE = "mm:ss";





    public static boolean isCloseEnough(long paramLong1, long paramLong2) {
        long l = paramLong1 - paramLong2;
        if (l < 0L)
            l = -l;
        return (l < 5 * 60 * 1000);
    }

    public static boolean isThisWeek(Date date) {
        Calendar monday = new GregorianCalendar();
        monday.setTimeInMillis(System.currentTimeMillis());
        int day_of_week = monday.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        monday.add(Calendar.DATE, -day_of_week + 1);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int cha = calendar.get(Calendar.DAY_OF_YEAR) - monday.get(Calendar.DAY_OF_YEAR);
        if (cha > 0 && cha < 7) {
            return true;
        }
        return false;
    }

    public static boolean isSameDay(long start, long end) {
        Date startDay = new Date(start);
        Date endDay = new Date(end);
        return startDay.getYear() == endDay.getYear()
                && startDay.getMonth() == endDay.getMonth()
                && startDay.getDate() == endDay.getDate();
    }

    /**
     * 得到calendar的日期：xxxx-xx-xx
     */
    public static String getTagTimeStr(Calendar calendar) {
        String ss = "";
        if (calendar != null) {
            ss = DateUtils.longToStr(calendar.getTimeInMillis(), DateUtils.FORMAT_DATE);
        }
        return ss;
    }

    /**
     * 得到calendar的日期：xx月xx日
     */
    public static String getTagTimeStrByMouthandDay(Calendar calendar) {
        String ss = "";
        if (calendar != null) {
            ss = DateUtils.longToStr(calendar.getTimeInMillis(), "MM月dd日");
        }
        return ss;
    }

    /**
     * 取得两个时间相差的毫秒数
     */
    public static long diff(String dateStr1, String dateStr2) {
        long diff = 0;
        Date date1 = stringToDate(dateStr1);
        Date date2 = stringToDate(dateStr2);

        if (date1 != null && date2 != null) {
            diff = Math.abs(date1.getTime() - date2.getTime());
        }

        return diff;
    }

    /**
     * 取得特定时间与当前时间之间相差的毫秒数
     */
    public static long diffCurrentTime(String dateStr) {
        long diff = 0;

        Date date = stringToDate(dateStr);
        if (date != null) {
            diff = Math.abs(date.getTime() - System.currentTimeMillis());
        }

        return diff;
    }

    public static Date stringToDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        int len = dateStr.length();
        if (len == 10) {
            return StringToDate(dateStr, FORMAT_DATE);
        } else if (len == 16) {
            return StringToDate(dateStr, DateUtils.FORMAT_DATEHR);
        } else if (len == 19) {
            return StringToDate(dateStr, DateUtils.FORMAT_DATETIME);
        } else if (len == 23) {
            return StringToDate(dateStr, DateUtils.FORMAT_DATETIME_MS);
        }
        return null;
    }

    public static Date StringToDate(String dateStr, String format) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                format);
        Date localDate = null;
        try {
            localDate = localSimpleDateFormat.parse(dateStr);
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return localDate;
    }

    public static String longToStr(long time, String format) {
        SimpleDateFormat dataFormat = new SimpleDateFormat(
                format);
        Date date = new Date();
        date.setTime(time);
        return dataFormat.format(date);
    }

    public static String getCurrentTime(String format) {
        if (TextUtils.isEmpty(format)) {
            format = FORMAT_DATETIME;
        }
        return longToStr(System.currentTimeMillis(), format);
    }

    public static String toTime(int paramInt) {
        paramInt /= 1000;
        int i = paramInt / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = paramInt % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i),
                Integer.valueOf(k)});
    }

    public static String toTimeBySecond(int paramInt) {
        int i = paramInt / 60;
        int j = 0;
        if (i >= 60) {
            j = i / 60;
            i %= 60;
        }
        int k = paramInt % 60;
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(i),
                Integer.valueOf(k)});
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        // 再转换为时间
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekStr(Date date) {
        String str = "";
        str = getWeek(date);
        if ("1".equals(str)) {
            str = "星期日";
        } else if ("2".equals(str)) {
            str = "星期一";
        } else if ("3".equals(str)) {
            str = "星期二";
        } else if ("4".equals(str)) {
            str = "星期三";
        } else if ("5".equals(str)) {
            str = "星期四";
        } else if ("6".equals(str)) {
            str = "星期五";
        } else if ("7".equals(str)) {
            str = "星期六";
        }
        return str;
    }

    public static String getTimestampStr() {
        return Long.toString(System.currentTimeMillis());
    }



    public static boolean isCanBack(long millis) {
        long diff = (new Date().getTime()) - millis;
        double seconds = Math.abs(diff) / 1000;
        double minutes = seconds / 60;
        if (minutes < 2) {
            return true;
        } else {
            return false;
        }
    }


}
