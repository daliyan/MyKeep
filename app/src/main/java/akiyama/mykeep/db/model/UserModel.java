package akiyama.mykeep.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;

import akiyama.mykeep.db.DataProviderHelper;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  17:39
 */
public class UserModel extends BaseModel{
    private String username;
    private String password;
    private String email;
    private String phone;
    public String profileImageUrl;

    public UserModel(){

    }

    public UserModel(Parcel in){
        readBase(in);
        username = in.readString();
        password = in.readString();
        email = in.readString();
        phone = in.readString();
        profileImageUrl = in.readString();
    }

    /**
     * 通过Cursor获取对应的数据，出现错误的时候返回null
     * @param cursor
     * @return
     */
    public static UserModel getUserByCursor(Cursor cursor){
        if(cursor==null){
            return null;
        }
        UserModel user=new UserModel();
        user.id= DataProviderHelper.parseString(cursor,BaseColumns._ID);
        user.objectId=DataProviderHelper.parseString(cursor,BaseColumns.ID);
        user.username=DataProviderHelper.parseString(cursor,UserColumns.USERNAME);
        user.password=DataProviderHelper.parseString(cursor,UserColumns.PASSWORD);
        user.email=DataProviderHelper.parseString(cursor,UserColumns.EMAIL);
        user.password=DataProviderHelper.parseString(cursor,UserColumns.PHONE);
        user.creatTime=DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        user.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        return user;
    }

    @Override
    public ContentValues values() {
        return null;
    }

    @Override
    public Uri getContentUri() {
        return null;
    }

    @Override
    public String getTable() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
