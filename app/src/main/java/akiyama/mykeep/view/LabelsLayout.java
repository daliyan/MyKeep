package akiyama.mykeep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.util.DimUtil;
import akiyama.mykeep.util.ResUtil;

/**
 * 标签组
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-25  16:08
 */
public class LabelsLayout extends LinearLayout {

    private Context mContext;
    private StringBuffer mLabelStr= new StringBuffer("");
    private int mLabelPadding = (int) DimUtil.dipToPx(4);
    public LabelsLayout(Context context) {
        super(context);
        init(context);
    }

    public LabelsLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public LabelsLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(HORIZONTAL);
    }

    public void addLabel(String labelName){
        if(labelName!=null) {
            TextView labelTv = new TextView(mContext);
            labelTv.setText(labelName);
            labelTv.setTextSize(13);
            labelTv.setTextColor(getResources().getColor(R.color.white));
            labelTv.setBackgroundColor(getResources().getColor(R.color.grey));
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(mLabelPadding,mLabelPadding,mLabelPadding,mLabelPadding);
            labelTv.setLayoutParams(params);
            labelTv.setPadding(mLabelPadding, mLabelPadding, mLabelPadding, mLabelPadding);
            labelTv.setBackgroundResource(R.drawable.corners_bg);
            labelTv.setTypeface(AppContext.getRobotoSlabLight());
            this.addView(labelTv);
            mLabelStr.append(labelName + DbConfig.LABEL_SPLIT_SYMBOL) ;
        }
    }

    public String getLabelTextStr(){
        return mLabelStr.toString();
    }

    public void initLabelText(){
        mLabelStr= new StringBuffer("");
    }

    /**
     * 循环添加Label标签
     * @param labels
     */
    public void setLabels(String[] labels){
        if(labels!=null){
            removeAllViews();
            initLabelText();
            for(String label:labels){
                addLabel(label);
            }
        }
    }
}


