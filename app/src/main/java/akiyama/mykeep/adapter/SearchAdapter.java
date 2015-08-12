package akiyama.mykeep.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.Notify;
import akiyama.mykeep.util.DimUtil;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.vo.SearchVo;

/**
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  16:21
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private List<SearchVo> mSearchVoList;
    private List<SearchVo> mSearchFilterList=new ArrayList<SearchVo>();
    private Context mContext;
    private int mPosition;
    public SearchAdapter(Context context,List<SearchVo> searchVoList){
        this.mContext=context;
        this.mSearchVoList=searchVoList;
        this.mSearchFilterList=searchVoList;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_resultl, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.ViewHolder holder, final int position) {
        if(mSearchVoList!=null){
            holder.mTitleTv.setText(mSearchVoList.get(position).getName());
            holder.mSelectLabelCb.setChecked(mSearchVoList.get(position).getIsCheck());
        }
        mPosition=position;
        holder.mSelectLabelCb.setClickable(false);

     /*   holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("SearchAdapter->OnClick", "" + v.getScaleY());
                holder.mSelectLabelCb.setChecked(!holder.mSelectLabelCb.isChecked());
                if (holder.mSelectLabelCb.isChecked()) {
                    mSearchVoList.get(position).setIsCheck(true);
                    mSearchFilterList.get(position).setIsCheck(true);
                } else {
                    mSearchVoList.get(position).setIsCheck(false);
                    mSearchFilterList.get(position).setIsCheck(false);
                }
                Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_LABEL_LIST);
            }
        });*/


    }

    public int getmPosition() {
        return mPosition;
    }

    @Override
    public int getItemCount() {
        return mSearchVoList.size();
    }

    public void refreshDate(List<SearchVo> searchVoList){
        this.mSearchVoList=searchVoList;
        notifyDataSetChanged();
    }

    public List<SearchVo> getFinalSearchDate(){
        return mSearchFilterList;
    }

    public List<SearchVo> getSearchVoList() {
        return mSearchVoList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTv;
        public CheckBox mSelectLabelCb;
        public View mView;
        public ViewHolder(View v) {
            super(v);
            this.mView=v;
            mTitleTv =(TextView) v.findViewById(R.id.label_name_tv);
            mSelectLabelCb=(CheckBox) v.findViewById(R.id.select_label_cb);
        }

        public View getView() {
            return mView;
        }
    }
}
