package ssj.androiddesign.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *判断网络是否有效工具类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-02  09:47
 */
public class NetworkHelper {

    private final static String TAG="NetworkHelper";

    /**
     * 判断网络是否链接成功
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context){
        if (context == null)
            return false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     * 是否是WIFI连接
     * @param context
     * @return
     */
    public static boolean isWifi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取网络信息
     * @param context
     * @return
     */
    public static String getNetworkInfo(Context context) {
        if (context == null)
            return "";
        StringBuilder builder = new StringBuilder();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        builder.append(info == null ? "" : info.toString());
        return builder.toString();
    }

}
