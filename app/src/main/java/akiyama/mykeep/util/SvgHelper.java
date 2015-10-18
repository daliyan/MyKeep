package akiyama.mykeep.util;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;

import akiyama.mykeep.AppContext;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-10-15  17:10
 */
public class SvgHelper {

    /**
     * 获取Image图片
     * @param svgId
     * @return
     */
    public static void setImageDrawable(ImageView imageView,int svgId){
        SVG svg = new SVGBuilder().readFromResource(AppContext.getInstance().getResources(), svgId).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        imageView.setImageDrawable(svg.getDrawable());
    }

    /**
     * 设置Image图片
     * @param context
     * @param viewId
     * @param svgId
     */
    public static void setImageDrawable(Context context,int viewId,int svgId){
        SVG svg = new SVGBuilder().readFromResource(context.getResources(), svgId).build();
        ImageView imageView = (ImageView) ((Activity)context).findViewById(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        imageView.setImageDrawable(svg.getDrawable());
    }
}
