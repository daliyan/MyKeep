package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    private final Object mLock = new Object();
    public NoTickAdapter(List<String> mDataset) {
        this.mDataset = mDataset;
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
            String recordList = mDataset.get(position);
            holder.mSelectCb.setChecked(true);
            holder.mContentEt.setText(recordList);
            holder.mContentEt.setTypeface(AppContext.getRobotoSlabThin());
            holder.mContentTextWatch.updatePosition(holder.getPosition());
            if(holder.mContentEt.hasFocus()){
                holder.mCancelIv.setVisibility(View.VISIBLE);
            }else{
                holder.mCancelIv.setVisibility(View.GONE);
            }
            holder.mCancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });

            holder.mSelectCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
        }
    }

    public void addItem(String content){
        mDataset.add(content);
       // notifyItemInserted(mDataset.size());//android BUG,存在并发的问题
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        mDataset.remove(position);
       //s notifyItemRemoved(position);
        notifyDataSetChanged();
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
        public ContentTextWatch mContentTextWatch;
        public ViewHolder(View v,ContentTextWatch mContentTextWatch) {
            super(v);
            if(v!=null){
                mMoveIv = (ImageView) v.findViewById(R.id.tick_cancel_iv);
                mSelectCb = (CheckBox) v.findViewById(R.id.no_tick_select_cb);
                mContentEt = (EditText) v.findViewById(R.id.no_tick_content_et);
                mCancelIv = (ImageView) v.findViewById(R.id.no_tick_cancel_iv);
                this.mContentTextWatch = mContentTextWatch;
                mContentEt.addTextChangedListener(mContentTextWatch);
            }

        }
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
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}


