package akiyama.mykeep.db.model;

import android.net.Uri;


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

    private final static Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ( "
            + _ID + " integer primary key autoincrement, "
            + ID + " text not null, "
            + USERNAME + " text not null, "
            + PASSWORD + " text not null, "
            + EMAIL + " text not null, "
            + PHONE + " text, "
            + PROFILE_IMAGE_URL + "text,"
            + CREATAT + " text not null, "
            + UPDATEAT + " text not null, "
            + "unique ( "
            + USERNAME + ","
            + ID
            + " ) on conflict ignore );";
}
