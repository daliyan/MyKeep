package akiyama.mykeep.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import akiyama.mykeep.db.model.BaseModel;

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

    public static Uri withAppendedId(Uri baseUri, String id) {
        return Uri.withAppendedPath(baseUri, "id/" + id);
    }

    /**
     * 返回user_id的ContentUri
     * @param baseUri
     * @param userId
     * @return
     */
    public static Uri withAppendedRecordUserId(Uri baseUri, String userId) {
        return Uri.withAppendedPath(baseUri, "user_id/" + userId);
    }

    public static ContentValues[] getContentValuesByModels(List<? extends BaseModel> models) {
        if (models == null || models.size() == 0) {
            return null;
        }
        int size = models.size();
        ContentValues[] values = new ContentValues[size];

        for (int i = 0; i < size; i++) {
            values[i] = models.get(i).values();
        }
        return values;
    }

}
