package akiyama.mykeep.db.model;

import android.net.Uri;

/**
 * 图片类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  16:55
 */
public class ImageColumns extends BaseColumns{

    public final static String TABLE_NAME="image";
    private final static String URL="url";
    private final static String RECORD_ID="record_id";
    private final static Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + ID + " text not null, "
            + URL + " text not null, "
            + RECORD_ID + " text not null, "
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null, "
            + "unique ( "
            + ID
            + " ) on conflict ignore );";
}
