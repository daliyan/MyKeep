package akiyama.mykeep.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import akiyama.mykeep.AppContext;

/**
 * 资源帮助类
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-06
 */
public class ResUtil {

    public static final String ROBOTOSLAB_BOLD="bold";
    public static final String ROBOTOSLAB_LIGHR="light";
    public static final String ROBOTOSLAB_REGULAR="regular";
    public static final String ROBOTOSLAB_THIN="thin";
    /**
     * 设置某个VIEW成RobotoSlab的字体
     * @param view 需要设置字体的TextView
     * @param type 设置字体的类型
     */
    public static void setRobotoSlabTypeface(TextView view,String type){
        AssetManager assetManager = AppContext.getInstance().getAssets();
        if(type.equals(ROBOTOSLAB_BOLD)){
            view.setTypeface(Typeface.createFromAsset(assetManager, "fonts/RobotoSlab/RobotoSlab-Bold.ttf"));
        }else if(type.equals(ROBOTOSLAB_LIGHR)){
            view.setTypeface(Typeface.createFromAsset(assetManager, "fonts/RobotoSlab/RobotoSlab-Light.ttf"));
        }else if(type.equals(ROBOTOSLAB_REGULAR)){
            view.setTypeface(Typeface.createFromAsset(assetManager, "fonts/RobotoSlab/RobotoSlab-Regular.ttf"));
        }else if(type.equals(ROBOTOSLAB_THIN)){
            view.setTypeface(Typeface.createFromAsset(assetManager, "fonts/RobotoSlab/RobotoSlab-Thin.ttf"));
        }
    }


}
