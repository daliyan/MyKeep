package akiyama.mykeep.db.model;

import android.content.ContentResolver;
import android.net.Uri;

import akiyama.mykeep.common.Constants;

/**
 * “记录”表项
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  16:39
 */
public class RecordColumns extends BaseColumns{

    public final static String TABLE_NAME="record";
    public final static String TITLE="title";
    public final static String CONTENT="content";
    public final static String ALARMTIME="alarm_time";
    public final static String LEVEL="level";
    public final static String LABELNAMES ="label_names";
    public final static String USERID="user_id";
    public final static String RECORDTYPE = "record_type";//记事类型，例如 列表型、普通计事型

    public final static Uri CONTENT_URI=Uri.parse("content://"+ Constants.AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + TITLE + " text, "
            + CONTENT + " text not null, "
            + ALARMTIME + " text not null, "
            + LEVEL + " INTEGER not null, "
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null, "
            + USERID + " text not null, "
            + RECORDTYPE + " INTEGER not null,"
            + LABELNAMES + " text)";

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.mykeep.record";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.mykeep.record";
}
