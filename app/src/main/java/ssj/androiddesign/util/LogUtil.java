package ssj.androiddesign.util;

import android.text.TextUtils;
import android.util.Log;

import ssj.androiddesign.AppContext;

/**
 * 信息调试类，用于记录一些调试信息和错误日志信息
 * @author zhiwu_yan
 * @since 2015年05月26日
 * @version 1.0
 */
public class LogUtil {

    private final static String TAG="LogUtil";
    private final static boolean LOGGABLE = AppContext.DEBUG;

    /**
     * 打印DEBUG信息
     * @param tag
     * @param str
     */
    public static void d(String tag, String str){
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.d(tag, str);
        }
    }

    /**
     * 打印debug级别的log
     * @param str 内容
     */
    public static void d(String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.d(TAG, str);
        }
    }

    /**
     * 打印warning的log
     * @param tag tag标签
     * @param str 内容
     */
    public static void w(String tag, String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.w(tag, str);
        }
    }


    /**
     * 打印warning级别的log
     *
     * @param str 内容
     */
    public static void w(String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.w(TAG, str);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void e(String tag, String str, Throwable tr) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.e(tag, str);
            tr.printStackTrace();
        }
    }

    /**
     * 打印error级别的log
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void e(String tag, String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.e(tag, str);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param str 内容
     */
    public static void e(String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.e(TAG, str);
        }
    }

    /**
     * 打印info级别的log
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void i(String tag, String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.i(tag, str);
        }
    }

    /**
     * 打印info级别的log
     *
     * @param str 内容
     */
    public static void i(String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.i(TAG, str);
        }
    }

    /**
     * 打印verbose级别的log
     * @param tag tag标签
     * @param str 内容
     */
    public static void v(String tag, String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.v(tag, str);
        }
    }

    /**
     * 打印verbose级别的log
     * @param str 内容
     */
    public static void v(String str) {
        if (LOGGABLE && !TextUtils.isEmpty(str)) {
            Log.v(TAG, str);
        }
    }

}
