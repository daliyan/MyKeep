package akiyama.mykeep.adapter;



import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;

import akiyama.mykeep.db.model.LabelModel;
import akiyama.mykeep.widget.RecordByLabelFragment;

/**
 * 主页标签ViewPager适配器
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:39
 */
public class RecordByLabelAdapter extends FragmentStatePagerAdapter {

    private List<LabelModel> mLabelModelList;
    public RecordByLabelAdapter(FragmentManager fm,List<LabelModel> labelModelList) {
        super(fm);
        this.mLabelModelList = labelModelList;
    }

    @Override
    public Fragment getItem(int position) {
        RecordByLabelFragment recordByLabelFragment =new RecordByLabelFragment();
        Bundle bundle=new Bundle();
        bundle.putString(RecordByLabelFragment.KEY_LABEL_NAME,mLabelModelList.get(position).getName());
        recordByLabelFragment.setArguments(bundle);
        return recordByLabelFragment;
    }

    @Override
    public int getCount() {
        return mLabelModelList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLabelModelList.get(position).getName();
    }

    public void refreshList(List<LabelModel> labelModelList){
        this.mLabelModelList = labelModelList;
        notifyDataSetChanged();
    }
}
