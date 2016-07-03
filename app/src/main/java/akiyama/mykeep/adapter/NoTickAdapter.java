package akiyama.mykeep.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.akiyama.base.AppContext;

import java.util.Collections;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.helper.ItemTouchHelperAdapter;
import akiyama.mykeep.adapter.helper.ItemTouchHelperViewHolder;
import akiyama.mykeep.adapter.helper.OnStartDragListener;
import akiyama.mykeep.util.SvgHelper;

public class NoTickAdapter extends RecyclerView.Adapter<NoTickAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "NoTickAdapter";
    private Context mContext;
    private List<String> mDataset;
    private NoTickCallback mNoTickCallback;
    /**
     * 允许存在的最大空项数量
     */
    public static final int MAX_EMPTY_CONTENT = 2;
    private OnStartDragListener mOnStartDragListener;
    public NoTickAdapter(List<String> mDataset,Context context) {
        this.mDataset = mDataset;
        this.mContext = context;
    }

    @Override
    public NoTickAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordlist_no_tick_view, parent, false);
        ViewHolder vh = new ViewHolder(view,new ContentTextWatch());
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mDataset!=null && mDataset.size()>0){
            holder.mContentEt.setTypeface(AppContext.getRobotoSlabLight());
            holder.mContentTextWatch.updatePosition(position);//必须在setText之前updatePosition，否则会触发onTextChanged方法让位置错乱
            holder.mContentEt.setText(mDataset.get(position));
            if (holder.mContentEt.requestFocus()) {
                holder.mCancelIv.setVisibility(View.VISIBLE);
            } else {
                holder.mCancelIv.setVisibility(View.GONE);
            }

           /* if(holder.mContentEt.requestFocus()){
                mImm.toggleSoftInputFromWindow(mParentView.getWindowToken(),0,0);
            }
            *//**
            *//* * 没有绘制完成，不能显示键盘，所以这里做一个延迟
             * FIXME 后面看是否有什么方式解决
             *//**//*
            holder.mContentEt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(holder.mContentEt.requestFocus()){
                        mImm.showSoftInput(holder.mContentEt,InputMethodManager.SHOW_FORCED);
                    }
                }
            },50);*/

            holder.mCancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mNoTickCallback!=null){

                        //如果使用notifyItemxxx来删除位置，注意一定要使用getAdapterPosition()。否则因为视图和Adapter数据不一致导致出现错误的位置
                        //如果使用notifyDataSetChanged()来更新位置，则直接使用position否则会返回NO_POSITION而报错
                        mNoTickCallback.onNoTickRemoveItem(holder.getAdapterPosition());
                    }
                }
            });

            holder.mSelectCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = ((CheckBox)v);
                    checkBox.setChecked(!checkBox.isChecked());
                    if(mNoTickCallback!=null){
                        mNoTickCallback.onNoTickCheckItem(holder.getAdapterPosition());
                    }
                }
            });

            holder.mContentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        holder.mCancelIv.setVisibility(View.VISIBLE);
                    }else{
                        holder.mCancelIv.setVisibility(View.GONE);
                    }
                }
            });
            /**
             * Touch移动列表项目
             */
            holder.mMoveIv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(mOnStartDragListener!=null){
                        mOnStartDragListener.onStartDrag(holder,position);
                    }
                    return true;
                }
            });
        }
    }

    public void addItem(String content){
        mDataset.add(content);
        notifyItemInserted(mDataset.size());
    }

    public void removeItem(int position){
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void setNoTickCallback(NoTickCallback mNoTickCallback) {
        this.mNoTickCallback = mNoTickCallback;
    }

    public void setOnStartDragListener(OnStartDragListener mOnStartDragListener) {
        this.mOnStartDragListener = mOnStartDragListener;
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public ImageView mMoveIv;
        public CheckBox mSelectCb;
        public EditText mContentEt;
        public ImageView mCancelIv;
        public ContentTextWatch mContentTextWatch;
        public ViewHolder(View v,ContentTextWatch mContentTextWatch) {
            super(v);
            if(v!=null){
                mMoveIv = (ImageView) v.findViewById(R.id.touch_move_iv);
                mSelectCb = (CheckBox) v.findViewById(R.id.no_tick_select_cb);
                mContentEt = (EditText) v.findViewById(R.id.no_tick_content_et);
                mCancelIv = (ImageView) v.findViewById(R.id.no_tick_cancel_iv);
                mSelectCb.setChecked(false);
                SvgHelper.setImageDrawable(mMoveIv, R.raw.ic_apps_24px);
                this.mContentTextWatch = mContentTextWatch;
                mContentEt.addTextChangedListener(mContentTextWatch);
            }
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

    /**
     * 获取空内容列表数量
     * @return
     */
    public int getNullContentSize(){
        int count = 0;
        for(String content:mDataset){
            if(content.equals("")){
                count++;
            }
        }
        return count;
    }

    private class ContentTextWatch implements TextWatcher{

        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataset.set(position,s.toString());
            if(mNoTickCallback!=null){
                mNoTickCallback.onNoTickTextChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public interface NoTickCallback{
        public void onNoTickRemoveItem(int position);
        public void onNoTickCheckItem(int position);

        /**
         * 获取当前项目的Text发生改变
         */
        public void onNoTickTextChanged();
    }
}


