package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;

public class TickAdapter extends RecyclerView.Adapter<TickAdapter.ViewHolder> {
    private static final String TAG = "NoTickAdapter";
    private List<String> mDataset;

    public TickAdapter(List<String> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public TickAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordlist_tick_view, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(mDataset!=null && mDataset.size()>0){
            String recordList = mDataset.get(position);
            holder.mSelectCb.setChecked(true);
            holder.mContentTv.setText(recordList);
            holder.mContentTv.setTypeface(AppContext.getRobotoSlabBold());
            holder.mCancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataset.remove(position);
                    notifyItemRemoved(position);
                }
            });

            holder.mSelectCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mSelectCb;
        public TextView mContentTv;
        public ImageView mCancelIv;

        public ViewHolder(View v) {
            super(v);
            if(v!=null){
                mSelectCb = (CheckBox) v.findViewById(R.id.tick_select_cb);
                mContentTv = (TextView) v.findViewById(R.id.tick_content_tv);
                mCancelIv = (ImageView) v.findViewById(R.id.tick_cancel_iv);
            }

        }
    }
}


