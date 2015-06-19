package ssj.androiddesign.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用于对时间进行操作
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  16:34
 */
public class DateUtil {

    /**
     * 功能: 将插入的字符串按格式转换成对应的日期对象
     *
     * @param str 字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date StringToDate(String str, String pattern) {
        Date dateTime = null;
        try {
            if (str != null && !str.equals("")) {
                SimpleDateFormat formater = new SimpleDateFormat(pattern);
                dateTime = formater.parse(str);
            }
        } catch (Exception ex) {
        }
        return dateTime;
    }

    /**
     * 功能：返回传入日期对象（date）之后afterDays天数的日期对象
     *
     * @param date 日期对象
     * @param afterDays 往前天数
     * @return java.util.Date 返回值
     */
    public static Date getBeferDay(Date date, int afterDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -afterDays);
        return cal.getTime();
    }

    /**
     * 得到后几天 天月的字符串
     * @return java.util.Date 返回值
     */
    public static String getNowDayMothString(int beferDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeferDay(Calendar.getInstance().getTime(),beferDays));
        if(beferDays==0){
            return "今天";
        }else if(beferDays==1){
            return "昨天";
        }else{
            int month = (cal.get(Calendar.MONTH))+1;
            int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
            return  month+"."+day_of_month;
        }
    }
}
