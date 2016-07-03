package com.akiyama.data.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;

import com.akiyama.data.db.DataProviderHelper;


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

    @Override
    public ContentValues values() {
        ContentValues cv=convert();
        cv.put(UserColumns.USERNAME,username);
        cv.put(UserColumns.PASSWORD,password);
        cv.put(UserColumns.EMAIL,email);
        cv.put(UserColumns.PHONE,phone);
        cv.put(UserColumns.PROFILE_IMAGE_URL,profileImageUrl);
        return cv;
    }

    @Override
    public Uri getContentUri() {
        return UserColumns.CONTENT_URI;
    }

    @Override
    public String getTable() {
        return UserColumns.TABLE_NAME;
    }

    @Override
    public UserModel getModel(Cursor cursor) {
        if(cursor==null){
            return null;
        }
        UserModel user=new UserModel();
        user.id= DataProviderHelper.parseString(cursor,BaseColumns._ID);
        user.username=DataProviderHelper.parseString(cursor,UserColumns.USERNAME);
        user.password= DataProviderHelper.parseString(cursor,UserColumns.PASSWORD);
        user.email=DataProviderHelper.parseString(cursor,UserColumns.EMAIL);
        user.password=DataProviderHelper.parseString(cursor,UserColumns.PHONE);
        user.createTime =DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        user.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        user.profileImageUrl=DataProviderHelper.parseString(cursor,UserColumns.PROFILE_IMAGE_URL);
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeBase(dest,flags);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(profileImageUrl);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
