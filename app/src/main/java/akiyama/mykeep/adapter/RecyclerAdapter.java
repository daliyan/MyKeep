package akiyama.mykeep.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.util.DateUtil;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.ResUtil;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
   private static final String TAG="RecyclerAdapter";
   private List<RecordModel> mDataset;
   private OnItemClick mOnItemClick;
   private OnLongItemClick mOnLongItemClick;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
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

                holder.mTitleTv.setTypeface(AppContext.getRobotoSlabBold());
                holder.mSubTitleTv.setTypeface(AppContext.getRobotoSlabLight());
                holder.mUpdateTv.setTypeface(AppContext.getRobotoSlabThin());
                //不要使用setBackgroundColor,该方法会覆盖掉设置的弧度等参数
                holder.mCardView.setCardBackgroundColor(Color.parseColor(recordModel.getLevel()));
            }
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClick(v, holder.getPosition());
                }
            }
        });

        holder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongItemClick != null) {
                    mOnLongItemClick.onLongItemClick(v, holder.getPosition());
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
    }

    /**
     * 针对某些高版本系统出现设置cardElevation无效果的BUG
     */
    public void setElevation(){

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
        public void onItemClick(View v,int position);
    }

    /**
     * 长按事件
     */
    public interface OnLongItemClick{
        public void onLongItemClick(View v,int position);
    }
}


