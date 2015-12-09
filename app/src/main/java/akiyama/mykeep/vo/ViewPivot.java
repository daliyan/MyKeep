package akiyama.mykeep.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 中心坐标，用来携带动画的跳转
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-12-09  16:49
 */
public class ViewPivot implements Parcelable {
    public float pivotX;
    public float pivotY;

    public ViewPivot(float pivotX,float pivotY){
        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }
    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(pivotX);
        dest.writeFloat(pivotY);
    }
}

