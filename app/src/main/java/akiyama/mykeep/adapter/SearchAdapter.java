package akiyama.mykeep.adapter;

import android.content.Context;
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
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  16:21
 */
public class SearchAdapter extends BaseAdapter {

    private List<SearchVo> mSearchVoList;
    private List<SearchVo> mSearchFilterList=new ArrayList<SearchVo>();
    private Context mContext;
    public SearchAdapter(Context context,List<SearchVo> searchVoList){
        this.mContext=context;
        this.mSearchVoList=searchVoList;
        this.mSearchFilterList=searchVoList;
    }

    @Override
    public int getCount() {
        return mSearchVoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchVoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_resultl, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        if(mSearchVoList!=null){
            viewHolder.mTitleTv.setText(mSearchVoList.get(position).getName());
            viewHolder.mSelectLabelCb.setChecked(mSearchVoList.get(position).getIsCheck());
        }

        viewHolder.mSelectLabelCb.setClickable(false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mSelectLabelCb.setChecked(!viewHolder.mSelectLabelCb.isChecked());
                if (viewHolder.mSelectLabelCb.isChecked()) {
                    mSearchVoList.get(position).setIsCheck(true);
                    mSearchFilterList.get(position).setIsCheck(true);
                } else {
                    mSearchVoList.get(position).setIsCheck(false);
                    mSearchFilterList.get(position).setIsCheck(false);
                }
                Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_LABEL_LIST);
            }
        });
        return convertView;
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

    public static class ViewHolder  {
        public TextView mTitleTv;
        public CheckBox mSelectLabelCb;
        public ViewHolder(View v) {
            mTitleTv =(TextView) v.findViewById(R.id.label_name_tv);
            mSelectLabelCb=(CheckBox) v.findViewById(R.id.select_label_cb);
        }
    }


}
