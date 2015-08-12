package akiyama.mykeep.db.model;

import android.content.ContentResolver;
import android.net.Uri;

import akiyama.mykeep.common.Constants;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-10  15:09
 */
public class LabelCoumnls extends BaseColumns{


    public final static String TABLE_NAME="label";
    public final static String NAME="name";
    public final static String USERID="user_id";
    public final static Uri CONTENT_URI=Uri.parse("content://"+ Constants.AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + NAME + " text not null, "
            + USERID + " text not null, "
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null)";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.mykeep.label";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.mykeep.label";
}
