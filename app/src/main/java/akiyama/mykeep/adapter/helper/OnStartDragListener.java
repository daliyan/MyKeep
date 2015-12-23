package akiyama.mykeep.adapter.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2015/12/20.
 */
public interface OnStartDragListener {
    /**
     * 开始拖动
     * @param viewHolder
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder,int position);
}
