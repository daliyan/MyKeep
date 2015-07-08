package akiyama.mykeep.db.model;

import android.content.ContentResolver;
import android.net.Uri;

import akiyama.mykeep.common.Constants;

/**
 * 图片表
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  16:55
 */
public class ImageColumns extends BaseColumns{

    public final static String TABLE_NAME="image";
    public final static String URL="url";
    public final static String RECORD_ID="record_id";
    public final static Uri CONTENT_URI=Uri.parse("content://"+ Constants.AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + URL + " text not null, "
            + RECORD_ID + " text not null, "
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null)";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.mykeep.image";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.mykeep.image";
}
