package akiyama.mykeep.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-10  15:54
 */
public class LabelVo  {

    private String labelId;
    private String labelName;

    public LabelVo(String labelId,String labelName){
        this.labelId=labelId;
        this.labelName=labelName;
    }
    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }
}
