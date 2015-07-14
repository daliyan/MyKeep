package akiyama.mykeep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.vo.LabelVo;

/**
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-10  15:37
 */
public class SpinnerAdapter extends BaseAdapter{

    private static final String TAG="SpinnerAdapter";
    private Context mContext;
    private List<LabelVo> mLabelVos;
    public SpinnerAdapter(Context context,List<LabelVo> labelVos){
        this.mLabelVos=labelVos;
        this.mContext=context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mLabelVos.size();
    }

    @Override
    public Object getItem(int position) {
        return mLabelVos.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_label, parent, false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if(mLabelVos!=null){
            viewHolder.mTitleTv.setText(mLabelVos.get(position).getLabelName());
        }
        return convertView;
    }

    public static class ViewHolder  {
        public TextView mTitleTv;
        public ViewHolder(View v) {
            mTitleTv =(TextView) v.findViewById(R.id.label_name_tv);
        }
    }
}
