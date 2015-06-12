package ssj.androiddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ssj.androiddesign.R;
import ssj.androiddesign.bean.Recommend;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

   private List<Recommend> mDataset;
   public RecyclerAdapter(List<Recommend> mDataset){
       this.mDataset=mDataset;
       //this.mProgressBarCircularIndeterminate=progressBarCircularIndeterminate;
   }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleTv.setText(mDataset.get(position).title);
        holder.mSubTitleTv.setText(mDataset.get(position).subtitle);
        holder.phoneIv.setImageResource(mDataset.get(position).image);
    }

    // Return the size of your dataset (invoked by the layout manager)
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
        public ViewHolder(View v) {
            super(v);
            mTitleTv =(TextView) v.findViewById(R.id.title);
            mSubTitleTv =(TextView) v.findViewById(R.id.subtitle);
            phoneIv =(CircleImageView) v.findViewById(R.id.phone_iv);
        }
    }
}


