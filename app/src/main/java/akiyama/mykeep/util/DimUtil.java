package akiyama.mykeep.util;

import android.content.res.Resources;

/**
 * px 和dip之间转换方法
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  11:29
 */
public class DimUtil {

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int pxToDip(int px){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }


    public static float dipToPx(float dip){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dip * scale + 0.5f);
    }


    public static int pxToSp(float px) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    public static int spToPx( float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
