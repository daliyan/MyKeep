package ssj.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ssj.androiddesign.R;
import ssj.androiddesign.View.TimeLineView;
import ssj.androiddesign.bean.ChildRocommend;
import ssj.androiddesign.bean.Recommend;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

   private List<Recommend> mDataset;
   public RecyclerAdapter(List<Recommend> mDataset){
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
            ChildRocommend childRocommend=mDataset.get(position).childRocommend;
            if(childRocommend!=null){
                holder.mTitleTv.setText(childRocommend.title);
                holder.mSubTitleTv.setText(childRocommend.subtitle);
                holder.phoneIv.setImageResource(childRocommend.image);
                if(mDataset.get(position).timeTitle!=null){
                    holder.timeLineView.setText(mDataset.get(position).timeTitle,true);
                }else{
                    holder.timeLineView.setText("",false);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitleTv;
        public TextView mSubTitleTv;
        public CircleImageView phoneIv;
        public TimeLineView timeLineView;
        public ViewHolder(View v) {
            super(v);
            mTitleTv =(TextView) v.findViewById(R.id.title);
            mSubTitleTv =(TextView) v.findViewById(R.id.subtitle);
            phoneIv =(CircleImageView) v.findViewById(R.id.phone_iv);
            timeLineView=(TimeLineView) v.findViewById(R.id.title_timeLine_Tlv);
        }
    }
}


