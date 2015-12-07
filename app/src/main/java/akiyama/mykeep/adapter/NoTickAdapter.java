package akiyama.mykeep.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import akiyama.mykeep.util.SvgHelper;

public class NoTickAdapter extends RecyclerView.Adapter<NoTickAdapter.ViewHolder> {
    private static final String TAG = "NoTickAdapter";
    private List<String> mDataset;
    private NoTickCallback mNoTickCallback;
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
            holder.mSelectCb.setChecked(true);
            holder.mContentEt.setTypeface(AppContext.getRobotoSlabLight());
            holder.mContentTextWatch.updatePosition(position);//必须在setText之前updatePosition，否则会触发onTextChanged方法让位置错乱
            holder.mContentEt.setText(mDataset.get(position));
            holder.mContentEt.requestFocus();
            if(holder.mContentEt.hasFocus()){
                holder.mCancelIv.setVisibility(View.VISIBLE);
            }else{
                holder.mCancelIv.setVisibility(View.GONE);
            }

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
                        mNoTickCallback.onNoTickCheckItme(holder.getAdapterPosition());
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
                mMoveIv = (ImageView) v.findViewById(R.id.touch_move_iv);
                mSelectCb = (CheckBox) v.findViewById(R.id.no_tick_select_cb);
                mContentEt = (EditText) v.findViewById(R.id.no_tick_content_et);
                mCancelIv = (ImageView) v.findViewById(R.id.no_tick_cancel_iv);

                SvgHelper.setImageDrawable(mMoveIv, R.raw.ic_apps_24px);
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

    public interface NoTickCallback{
        public void onNoTickRemoveItem(int position);
        public void onNoTickCheckItme(int position);
    }
}


