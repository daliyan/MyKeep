package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;

public class NoTickAdapter extends RecyclerView.Adapter<NoTickAdapter.ViewHolder> {
    private static final String TAG = "NoTickAdapter";
    private List<String> mDataset;

    public NoTickAdapter(List<String> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public NoTickAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recordlist_no_tick_view, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(mDataset!=null && mDataset.size()>0){
            String recordList = mDataset.get(position);
            holder.mSelectCb.setChecked(true);
            holder.mContentEt.setText(recordList);
            holder.mContentEt.setTypeface(AppContext.getRobotoSlabBold());
            holder.mCancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        public ImageView mMoveIv;
        public CheckBox mSelectCb;
        public EditText mContentEt;
        public ImageView mCancelIv;

        public ViewHolder(View v) {
            super(v);
            if(v!=null){
                mMoveIv = (ImageView) v.findViewById(R.id.tick_cancel_iv);
                mSelectCb = (CheckBox) v.findViewById(R.id.no_tick_select_cb);
                mContentEt = (EditText) v.findViewById(R.id.no_tick_content_et);
                mCancelIv = (ImageView) v.findViewById(R.id.no_tick_cancel_iv);
            }

        }
    }
}


