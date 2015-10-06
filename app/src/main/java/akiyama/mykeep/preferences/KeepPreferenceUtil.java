package akiyama.mykeep.preferences;

import android.content.Context;


/**
 *
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-10-04  22:22
 */
public class KeepPreferenceUtil {

    private static KeepPreferenceUtil mKeepPreferenceUtil;
    /**
     * 主页VIew的显示方式,多行或者单行
     */
    private static final String SHOW_VIEW_TYPE="show_view_type";
    private KeepPreference mKeeprefernce;
    private KeepPreferenceUtil(Context context){
        mKeeprefernce = KeepPreference.getInstance(context);
    }

    public static KeepPreferenceUtil getInstance(Context context){
        if(mKeepPreferenceUtil==null){
            return new KeepPreferenceUtil(context);
        }
        return mKeepPreferenceUtil;
    }

    public void setShowViewCount(int count){
        mKeeprefernce.putInt(SHOW_VIEW_TYPE,count);
    }

    public int getShowViewCount(){
        return  mKeeprefernce.getInt(SHOW_VIEW_TYPE,1);
    }
}
