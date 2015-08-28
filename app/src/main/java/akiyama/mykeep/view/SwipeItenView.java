package akiyama.mykeep.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import akiyama.mykeep.R;
import akiyama.mykeep.util.LogUtil;

/**
 * 侧滑View
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-13  10:37
 */
public class SwipeItenView extends LinearLayout {

    private View mView;
    private LinearLayout mContentLl;
    private LinearLayout mMenuLl;
    /**
     * 需要附加的到这个SwipeItenView的View
     */
    private View mSwipItemLayout;

    private Scroller mScroller;
    private int mLastX = 0;
    private int mLastY = 0;
    private int mHolderWidth = 120;
    public SwipeItenView(Context context) {
        super(context);
        init(context);
    }

    public SwipeItenView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }

    public SwipeItenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        setOrientation(LinearLayout.HORIZONTAL);

        mView = View.inflate(context, R.layout.layout_custem_swipe, this);
        mContentLl = (LinearLayout) mView.findViewById(R.id.item_content_ll);
        mMenuLl = (LinearLayout) mView.findViewById(R.id.item_menu_ll);
        mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources().getDisplayMetrics()));
        setSwipeItemView(context);
    }

    public void setSwipeItemView(Context context){
        mSwipItemLayout = LayoutInflater.from(context).inflate(R.layout.item_search_resultl,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
        mSwipItemLayout.setLayoutParams(params);
        if(mContentLl!=null){
            mContentLl.addView(mSwipItemLayout);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.d("test"+event.getAction());
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * 2) {
                    // 滑动不满足条件，不做横向滑动
                    break;
                }

                // 计算滑动终点是否合法，防止滑动越界
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                int newScrollX = 0;
                // 这里做了下判断，当松开手的时候，会自动向两边滑动，具体向哪边滑，要看当前所处的位置
                if (scrollX - mHolderWidth * 0.75 > 0) {
                    newScrollX = mHolderWidth;
                }
                // 慢慢滑向终点
                smoothScrollTo(newScrollX, 0);
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;//自己来处理这个事件
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    public void setSwipItemLayout(View view){
        if(view!=null){
            mContentLl.addView(view);
        }
    }

}
