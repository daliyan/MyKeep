package akiyama.mykeep.db.model;

import android.content.ContentResolver;
import android.net.Uri;

import akiyama.mykeep.common.Constants;


/**
 * 用户表
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  15:46
 */
public class UserColumns extends BaseColumns{

    public final static String TABLE_NAME="user";
    public final static String USERNAME="username";
    public final static String PASSWORD="password";
    public final static String EMAIL="email";
    public final static String PHONE="phone";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";
    public final static Uri CONTENT_URI=Uri.parse("content://"+ Constants.AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + USERNAME + " text not null, "
            + PASSWORD + " text not null, "
            + EMAIL + " text not null, "
            + PHONE + " text, "
            + PROFILE_IMAGE_URL + "text,"
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null, "
            + "unique ( "
            + USERNAME
            + " ) on conflict ignore );";

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.mykeep.user";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.mykeep.user";
}
