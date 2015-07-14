package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.vo.ChildRocommendVo;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

   private List<ChildRocommendVo> mDataset;
   public RecyclerAdapter(List<ChildRocommendVo> mDataset){
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
            ChildRocommendVo childRocommend=mDataset.get(position);
            if(childRocommend!=null){
                holder.mTitleTv.setText(childRocommend.title);
                holder.mSubTitleTv.setText(childRocommend.subtitle);
                holder.mPhoneIv.setImageResource(childRocommend.image);
                holder.mUpdateView.setText(childRocommend.updateTime);
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
        public ImageView mPhoneIv;
        public TextView mUpdateView;
        public ViewHolder(View v) {
            super(v);
            mTitleTv =(TextView) v.findViewById(R.id.title);
            mSubTitleTv =(TextView) v.findViewById(R.id.subtitle);
            mPhoneIv =(ImageView) v.findViewById(R.id.phone_iv);
            mUpdateView=(TextView) v.findViewById(R.id.update_time_tv);
        }
    }
}


