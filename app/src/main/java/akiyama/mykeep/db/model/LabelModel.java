package akiyama.mykeep.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.text.Layout;

import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.db.DataProvider;
import akiyama.mykeep.util.DataProviderHelper;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-10  15:13
 */
public class LabelModel extends BaseModel{
    private String name;
    private String userId;

    public LabelModel(){

    }

    public LabelModel(String name,String userId){
        this.name = name;
        this.userId =userId;
    }

    public LabelModel(Parcel in){
        readBase(in);
        name=in.readString();
        userId=in.readString();
    }

    @Override
    public ContentValues values() {
        ContentValues cv=convert();
        cv.put(LabelCoumnls.NAME,name);
        cv.put(RecordColumns.USERID,userId);
        return cv;
    }

    @Override
    public Uri getContentUri() {
        return LabelCoumnls.CONTENT_URI;
    }

    @Override
    public String getTable() {
        return LabelCoumnls.TABLE_NAME;
    }

    @Override
    public LabelModel getModel(Cursor cursor) {
        if(cursor==null){
            return null;
        }
        LabelModel lm=new LabelModel();
        lm.id= DataProviderHelper.parseString(cursor, BaseColumns._ID);
        lm.updateTime=DataProviderHelper.parseString(cursor,BaseColumns.UPDATEAT);
        lm.creatTime=DataProviderHelper.parseString(cursor,BaseColumns.CREATAT);
        lm.name= DataProviderHelper.parseString(cursor,LabelCoumnls.NAME);
        lm.userId=DataProviderHelper.parseString(cursor,LabelCoumnls.USERID);
        return lm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeBase(dest,flags);
        dest.writeString(name);
        dest.writeString(userId);
    }

    public void setName(String name) {
        //不能包含DbConfig.LABEL_SPLIT_SYMBOL 字符，需要进行转义处理
        if(name.contains(DbConfig.LABEL_SPLIT_SYMBOL)){
            name = name.replace(DbConfig.LABEL_SPLIT_SYMBOL,DbConfig.LABEL_REPLACE_SYMBOL);
        }
        this.name = name;
    }

    public String getName() {
        //反转义
        if(name.contains(DbConfig.LABEL_REPLACE_SYMBOL)){
            name = name.replace(DbConfig.LABEL_REPLACE_SYMBOL,DbConfig.LABEL_SPLIT_SYMBOL);
        }
        return name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }



}
