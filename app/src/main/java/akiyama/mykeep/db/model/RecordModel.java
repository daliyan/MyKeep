package akiyama.mykeep.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;

import akiyama.mykeep.util.DataProviderHelper;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-07  15:30
 */
public class RecordModel extends BaseModel{

    private String title;
    private String content;
    private String alarmTime;
    private String level;
    private String userId;

    public RecordModel(){

    }

    public RecordModel(Parcel in){
        readBase(in);
        title=in.readString();
        content=in.readString();
        alarmTime=in.readString();
        level=in.readString();
        userId=in.readString();
    }
    
    public static RecordModel getRecordModel(Cursor cursor){
        if(cursor==null){
            return null;
        }
        RecordModel recordModel=new RecordModel();
        recordModel.id= DataProviderHelper.parseString(cursor,BaseColumns._ID);
        recordModel.title=DataProviderHelper.parseString(cursor,RecordColumns.TITLE);
        recordModel.content=DataProviderHelper.parseString(cursor,RecordColumns.CONTENT);
        recordModel.alarmTime=DataProviderHelper.parseString(cursor,RecordColumns.ALARMTIME);
        recordModel.level=DataProviderHelper.parseString(cursor,RecordColumns.LEVEL);
        recordModel.creatTime=DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        recordModel.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        recordModel.userId=DataProviderHelper.parseString(cursor,RecordColumns.USERID);
        return recordModel;
    }
    @Override
    public ContentValues values() {
        ContentValues cv=convert();
        cv.put(RecordColumns.TITLE,title);
        cv.put(RecordColumns.CONTENT,content);
        cv.put(RecordColumns.ALARMTIME,alarmTime);
        cv.put(RecordColumns.LEVEL,level);
        cv.put(RecordColumns.USERID,userId);
        return cv;
    }

    @Override
    public Uri getContentUri() {
        return RecordColumns.CONTENT_URI;
    }

    @Override
    public String getTable() {
        return RecordColumns.TABLE_NAME;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeBase(dest,flags);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(alarmTime);
        dest.writeString(level);
        dest.writeString(userId);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
