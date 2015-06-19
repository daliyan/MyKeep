package ssj.androiddesign.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ssj.androiddesign.R;
import ssj.androiddesign.util.DimUtil;

/**
 * 自定义时间线条
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-19  11:24
 */
public class TimeLineView extends View {

    private Context mContext;
    private Paint mTimeLinePaint;
    private Paint mTitlePaint;
    private Paint mRoundPaint;

    private CharSequence mText="TimeLineView";
    private ColorStateList mTextColor=null;
    private int mTextSize = (int)DimUtil.dipToPx(14);

    private int mHeight;
    private int mWidth;

    private int mTitlePadding=(int)DimUtil.dipToPx(3);
    private float mRoundRadius=DimUtil.dipToPx(5);

    private boolean isShowLine=true;
    public TimeLineView(Context context) {
        this(context,null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    public void init(Context context,AttributeSet attrs,int defStyleAttr){
        this.mContext=context;
        initAttrs(attrs);

        mTimeLinePaint=new Paint();
        mTimeLinePaint.setColor(getResources().getColor(R.color.main_bg));
        mTimeLinePaint.setStrokeWidth(DimUtil.dipToPx(2));
        mTimeLinePaint.setStyle(Paint.Style.FILL);
        mTimeLinePaint.setAntiAlias(true);

        mTitlePaint=new Paint();
        mTitlePaint.setColor(getResources().getColor(R.color.main_bg));
        mTitlePaint.setStrokeWidth(DimUtil.dipToPx(2));
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setColor((mTextColor != null ? mTextColor : ColorStateList.valueOf(0xFF000000)).getColorForState(getDrawableState(),0));
        mTitlePaint.setTextSize(mTextSize);
        mTitlePaint.setAntiAlias(true);

        mRoundPaint=new Paint();
        mRoundPaint.setColor(getResources().getColor(R.color.main_bg));
        mRoundPaint.setStrokeWidth(DimUtil.dipToPx(1));
        mRoundPaint.setStyle(Paint.Style.FILL);
        mRoundPaint.setAntiAlias(true);
    }

    private void initAttrs(AttributeSet attrs){
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,R.styleable.timeLineView,0, 0);
        try {
            mText = a.getText(R.styleable.timeLineView_text);
            mTextColor = a.getColorStateList(R.styleable.timeLineView_textColor);
            mTextSize=a.getDimensionPixelSize(R.styleable.timeLineView_textSize,(int)DimUtil.dipToPx(14));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight=getMeasuredHeight();
        mWidth=getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lineX=DimUtil.spToPx(mTitlePaint.getTextSize());
        if(isShowLine){
            if(mText!=null){
                canvas.drawText(mText.toString(),0,mHeight/2+mRoundRadius,mTitlePaint);
            }else{
                canvas.drawText("TimeLineView",0,mHeight/2+mRoundRadius,mTitlePaint);
            }
            canvas.drawCircle(lineX, mHeight/2, mRoundRadius, mRoundPaint);
        }
        canvas.drawLine(lineX,0,lineX,mHeight,mTimeLinePaint);
    }

    public void setText(CharSequence mText,boolean isShowLine) {
        this.mText = mText;
        this.isShowLine=isShowLine;
        requestLayout();
        invalidate();
    }
}
