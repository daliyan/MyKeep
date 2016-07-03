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
 * @since 2015-07-07  15:44
 */
public class ImageModel extends BaseModel{

    private String url;
    private String recordId;

    public ImageModel(){

    }

    public ImageModel(Parcel in){
        readBase(in);
        url=in.readString();
        recordId=in.readString();
    }

    @Override
    public ImageModel getModel(Cursor cursor) {
        if(cursor==null){
            return null;
        }

        ImageModel imageModel=new ImageModel();
        imageModel.id= DataProviderHelper.parseString(cursor, BaseColumns._ID);
        imageModel.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        imageModel.createTime =DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        imageModel.url=DataProviderHelper.parseString(cursor,ImageColumns.URL);
        imageModel.recordId=DataProviderHelper.parseString(cursor,ImageColumns.RECORD_ID);
        return imageModel;
    }
    /*public static ImageModel getImageModel(Cursor cursor){
        if(cursor==null){
            return null;
        }

        ImageModel imageModel=new ImageModel();
        imageModel.id= DataProviderHelper.parseString(cursor, BaseColumns._ID);
        imageModel.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        imageModel.createTime=DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        imageModel.url=DataProviderHelper.parseString(cursor,ImageColumns.URL);
        imageModel.recordId=DataProviderHelper.parseString(cursor,ImageColumns.RECORD_ID);
        return imageModel;
    }*/

    @Override
    public ContentValues values() {
        ContentValues cv=convert();
        cv.put(ImageColumns.URL,url);
        cv.put(ImageColumns.RECORD_ID,recordId);
        return cv;
    }

    @Override
    public Uri getContentUri() {
        return ImageColumns.CONTENT_URI;
    }

    @Override
    public String getTable() {
        return ImageColumns.TABLE_NAME;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeBase(dest, flags);
        dest.writeString(url);
        dest.writeString(recordId);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }
}
