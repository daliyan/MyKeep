package akiyama.mykeep.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.helper.ItemTouchHelperAdapter;
import akiyama.mykeep.adapter.helper.ItemTouchHelperViewHolder;
import akiyama.mykeep.adapter.helper.OnStartDragListener;

import com.akiyama.base.AppContext;
import com.akiyama.base.common.DbConfig;
import com.akiyama.data.db.model.RecordModel;
import akiyama.mykeep.util.DateUtil;
import com.akiyama.base.utils.LogUtil;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {
   private static final String TAG="RecyclerAdapter";
   private List<RecordModel> mDataset;
   private OnItemClick mOnItemClick;
   private OnLongItemClick mOnLongItemClick;
    private OnStartDragListener mOnStartDragListener;
   public RecyclerAdapter(List<RecordModel> mDataset){
       this.mDataset=mDataset;
   }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mDataset!=null && mDataset.size()>0){
            RecordModel recordModel=mDataset.get(position);
            if(recordModel!=null){
                holder.mTitleTv.setText(recordModel.getTitle());
                LogUtil.e(TAG,""+holder.mTitleTv.getText()+" "+position);
                String content = recordModel.getContent();
                //移除内容中特殊的标记
                if(recordModel.getRecordType() ==RecordModel.RECORD_TYPE_LIST){
                    content = content.replace(DbConfig.BIG_SPLIT_SYMBOL,"");
                    content = content.replace(DbConfig.SMAIL_SPLIT_SYMBOL,"");
                }
                holder.mSubTitleTv.setText(content);
                holder.mUpdateTv.setText(DateUtil.getDate(recordModel.getUpdateTime()));

                holder.mTitleTv.setTypeface(AppContext.getRobotoSlabLight());
                holder.mSubTitleTv.setTypeface(AppContext.getRobotoSlabLight());
                holder.mUpdateTv.setTypeface(AppContext.getRobotoSlabLight());
                //不要使用setBackgroundColor,该方法会覆盖掉设置的弧度等参数
                holder.mCardView.setCardBackgroundColor(Color.parseColor(recordModel.getLevel()));
            }
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });

        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClick != null) {
                    mOnLongItemClick.onLongItemClick(v, holder.getAdapterPosition());
                }

                if(mOnStartDragListener!=null){
                    mOnStartDragListener.onStartDrag(holder,position);
                }
                
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * 拖动item
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDataset, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * @param position
     */
    @Override
    public void onItemDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnStartDragListener(OnStartDragListener mOnStartDragListener) {
        this.mOnStartDragListener = mOnStartDragListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView mTitleTv;
        public TextView mSubTitleTv;
        public TextView mUpdateTv;
        public CardView mCardView;
        public ViewHolder(View v) {
            super(v);
            mTitleTv =(TextView) v.findViewById(R.id.title);
            mSubTitleTv =(TextView) v.findViewById(R.id.subtitle);
            mUpdateTv =(TextView) v.findViewById(R.id.update_time_tv);
            mCardView = (CardView) v.findViewById(R.id.cardView);
        }

        /**
         * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
         * Implementations should update the item view to indicate it's active state.
         */
        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.5f);
        }

        /**
         * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
         * state should be cleared.
         */
        @Override
        public void onItemClear() {
            itemView.setAlpha(1.0f);
        }
    }

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public void refreshDate(List<RecordModel> dataset){
        this.mDataset = dataset;
        notifyDataSetChanged();
    }


    public void setOnLongItemClick(OnLongItemClick mOnLongItemClick) {
        this.mOnLongItemClick = mOnLongItemClick;
    }

    /**
     * item接口点击回调
     */
    public interface OnItemClick{
        void onItemClick(View v,int position);
    }

    /**
     * 长按事件
     */
    public interface OnLongItemClick{
        void onLongItemClick(View v,int position);
    }
}


