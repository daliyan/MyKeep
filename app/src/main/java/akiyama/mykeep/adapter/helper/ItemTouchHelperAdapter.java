package akiyama.mykeep.adapter.helper;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2015/12/20.
 */
public interface ItemTouchHelperAdapter {

    /**
     * 拖动item
     * @param fromPosition
     * @param toPosition
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     *
     * @param position
     */
    void onItemDismiss(int position);

}
