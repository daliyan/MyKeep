package akiyama.mykeep.db.model;

import android.content.ContentValues;
import android.os.Parcel;

/**
 * 序列化BaseModel
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  17:29
 */
public abstract class BaseModel implements IModel{

    protected  String id;
    protected  String creatTime;
    protected  String updateTime;

    protected void readBase(Parcel in) {
        id = in.readString();
        creatTime = in.readString();
        updateTime = in.readString();
    }

    protected void writeBase(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(creatTime);
        dest.writeString(updateTime);
    }

    protected ContentValues convert() {
        ContentValues cv = new ContentValues();
        cv.put(BaseColumns._ID, id);
        cv.put(BaseColumns.CREATAT, creatTime);
        cv.put(BaseColumns.UPDATEAT, updateTime);
        return cv;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
