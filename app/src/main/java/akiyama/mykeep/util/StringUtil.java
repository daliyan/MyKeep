package akiyama.mykeep.util;

import android.text.TextUtils;

/**
 * 字符串操作类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-25  16:59
 */
public class StringUtil {

    /**
     * 根据符号分段截取字符串
     * @param str
     * @param symbol
     * @return
     */
    public static String[] subStringBySymbol(String str,String symbol){
        if(!TextUtils.isEmpty(str) && !TextUtils.isEmpty(symbol)){
            String[] strArray = str.split(symbol);
            return strArray;
        }
        return null;
    }
}
