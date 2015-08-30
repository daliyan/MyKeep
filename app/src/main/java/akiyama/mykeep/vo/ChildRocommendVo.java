package akiyama.mykeep.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 记事的子项目
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  17:39
 */
public class ChildRocommendVo implements Parcelable {

    public String title;
    public String subtitle;
    public int image;
    public String updateTime;
    public ChildRocommendVo(String title, String subtitle, String updateTime, int image){
        this.title=title;
        this.subtitle=subtitle;
        this.updateTime=updateTime;
        this.image=image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(updateTime);
        dest.writeInt(image);
    }

}
