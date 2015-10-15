package akiyama.mykeep.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wnafee.vector.compat.ResourcesCompat;


/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-10-15  17:10
 */
public class SvgHelper {

    /**
     * 设置SVG Drawable到Image类控件中
     * @param context
     * @param viewId 需要设置的控件ID
     * @param drawableId 需要设置的SVG Drawable的ID
     * @param parentView 可能需要通过该parentView来查找viewId对应的控件
     */
    public static void setImageDrawable(Context context,int viewId,int drawableId,@Nullable View parentView){
        ImageView imageView = null;
        if(parentView!=null){
            imageView =(ImageView) parentView.findViewById(viewId);
        }else{
            imageView =(ImageView) ((Activity)context).findViewById(viewId);
        }

        if(imageView!=null){
            Drawable remainDr = ResourcesCompat.getDrawable(context, drawableId);
            imageView.setImageDrawable(remainDr);
        }
    }

    /**
     * 设置SVG Drawable到Image类控件中
     * @param context
     * @param imageView 需要设置的控件
     * @param drawableId 需要设置的SVG Drawable的ID
     */
    public static void setImageDrawable(Context context,ImageView imageView,int drawableId){
        Drawable remainDr = ResourcesCompat.getDrawable(context, drawableId);
        imageView.setImageDrawable(remainDr);
    }
}
