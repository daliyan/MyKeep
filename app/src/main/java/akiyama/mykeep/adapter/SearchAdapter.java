package akiyama.mykeep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import akiyama.mykeep.R;
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
    private Context mContext;
    public SearchAdapter(Context context,List<SearchVo> searchVoList){
        this.mContext=context;
        this.mSearchVoList=searchVoList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
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
        return convertView;
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
