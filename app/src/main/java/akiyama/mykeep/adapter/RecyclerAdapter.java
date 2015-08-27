package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.util.DateUtil;
import akiyama.mykeep.vo.ChildRocommendVo;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

   private List<RecordModel> mDataset;
   private static OnItemClick mOnItemClick;
   public RecyclerAdapter(List<RecordModel> mDataset){
       this.mDataset=mDataset;
   }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mDataset!=null && mDataset.size()>0){
            RecordModel recordModel=mDataset.get(position);
            if(recordModel!=null){
                holder.mTitleTv.setText(recordModel.getTitle());
                holder.mSubTitleTv.setText(recordModel.getContent());
                holder.mUpdateView.setText(DateUtil.getDate(recordModel.getUpdateTime()));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTv;
        public TextView mSubTitleTv;
        public TextView mUpdateView;
        public ViewHolder(View v) {
            super(v);
            mTitleTv =(TextView) v.findViewById(R.id.title);
            mSubTitleTv =(TextView) v.findViewById(R.id.subtitle);
            mUpdateView=(TextView) v.findViewById(R.id.update_time_tv);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClick != null) {
                        mOnItemClick.onItemClick(v, getPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    public void refreshDate(List<RecordModel> dataset){
        this.mDataset = dataset;
        notifyDataSetChanged();
    }
    /**
     * item接口点击回调
     */
    public interface OnItemClick{
        public void onItemClick(View v,int position);
    }
}


