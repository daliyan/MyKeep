package akiyama.mykeep.db.model;

import android.net.Uri;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  16:39
 */
public class RecordColumns extends BaseColumns{

    public final static String TABLE_NAME="record";
    private final static String TITLE="title";
    private final static String CONTENT="content";
    private final static String ALARMTIME="alarm_time";
    private final static String LEVEL="level";
    private final static String USERID="user_id";

    private final static Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + ID + " text not null, "
            + TITLE + " text, "
            + CONTENT + " text not null, "
            + ALARMTIME + " text not null, "
            + LEVEL + " INTEGER not null, "
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null, "
            + USERID + " text not null, "
            + "unique ( "
            + ID
            + " ) on conflict ignore );";
}
