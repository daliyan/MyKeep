package akiyama.mykeep.db;

import android.database.Cursor;

/**
 * 获取查询数据的帮助类
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  17:48
 */
public class DataProviderHelper {

    public static String parseString(Cursor c, String columnName) {
        return c.getString(c.getColumnIndexOrThrow(columnName));
    }
}
