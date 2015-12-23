package akiyama.mykeep.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.controller.LabelController;
import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.util.DimUtil;
import akiyama.mykeep.util.SvgHelper;
import akiyama.mykeep.vo.SearchVo;
import akiyama.swipe.adapter.RecyclerViewAdapter;
import akiyama.swipe.swipe.SwipeItemLayout;

/**
 * 标签查询、添加、删除、编辑适配器
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  16:21
 */
public class SearchAdapter extends RecyclerViewAdapter<SearchAdapter.ViewHolder> {

    private static final int EDIT_LABEL=0;
    private static final int DELETE_LABEL=1;
    private ArrayList<SearchVo> mSearchVoList;
    private ArrayList<SearchVo> mSearchFilterList = new ArrayList<>();
    private Context mContext;

    public SearchAdapter(Context context,ArrayList<SearchVo> searchVoList){
        this.mContext=context;
        this.mSearchVoList=searchVoList;
        this.mSearchFilterList=searchVoList;
    }

    @Override
    public ViewHolder onCreatViewHodler(View view,  List<? extends View> menuViews, SwipeItemLayout swipeItemLayout, int i) {
        return new ViewHolder(view,menuViews,swipeItemLayout,i);
    }

    @Override
    public View onCreateItemLayoutId(ViewGroup viewGroup, int i) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_result_layout, viewGroup, false);
    }

    @Override
    public List<? extends View> creatMenuView() {
        List<Button> menuViews = new ArrayList<>();

        Button menu1=new Button(mContext);
        menu1.setBackgroundResource(R.drawable.label_edit_bg);
        menu1.setLayoutParams(new ViewGroup.LayoutParams((int) DimUtil.dipToPx(60), ViewGroup.LayoutParams.MATCH_PARENT));
        menu1.setText("编辑");
        menu1.setTextColor(Color.parseColor("#ffffff"));
        menu1.setTextSize(14);
        menuViews.add(menu1);

        Button menu2=new Button(mContext);
        menu2.setBackgroundResource(R.drawable.label_delete_bg);
        menu2.setLayoutParams(new ViewGroup.LayoutParams((int) DimUtil.dipToPx(60), ViewGroup.LayoutParams.MATCH_PARENT));
        menu2.setText("删除");
        menu2.setTextColor(Color.parseColor("#ffffff"));
        menu2.setTextSize(14);
        menuViews.add(menu2);
        return menuViews;
    }

    @Override
    public int onCreatLayout() {
        return R.layout.item_search_resultl;
    }

    @Override
    public SwipeItemLayout onCreatSwipeLayout(View view) {
        return (SwipeItemLayout)view.findViewById(R.id.search_sil) ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(mSearchVoList!=null){
            holder.mTitleTv.setText(mSearchVoList.get(position).getName());
            holder.mSelectLabelCb.setChecked(mSearchVoList.get(position).getIsCheck());
            SvgHelper.setImageDrawable( holder.mNameIconIv,R.raw.ic_label_outline_24px);
        }
        holder.mSelectLabelCb.setOnClickListener(null);
        /**
         * 设置单击事件，需要用自定义的单击事件来实现，否则由于滑动冲突会导致单击失效
         */
        holder.getSwipeItemLayout().setSwipeClick(new SwipeItemLayout.SwipeClick() {
            @Override
            public void swipeLayoutClickEvent() {
                holder.mSelectLabelCb.setChecked(!holder.mSelectLabelCb.isChecked());
                if (holder.mSelectLabelCb.isChecked()) {
                    mSearchVoList.get(position).setIsCheck(true);
                    mSearchFilterList.get(position).setIsCheck(true);
                } else {
                    mSearchVoList.get(position).setIsCheck(false);
                    mSearchFilterList.get(position).setIsCheck(false);
                }
                KeepNotifyCenterHelper.getInstance().notifyLabelStatusChange();
            }
        });

        holder.getSwipeItemLayout().setmSwipeMenuClick(new SwipeItemLayout.SwipeMenuClick() {
            @Override
            public void swipeMenuClickEvent(int swipePosition) {
                switch (swipePosition){
                    case DELETE_LABEL:
                        final LabelController labelController=new LabelController();
                        //直接在主线程中删除
                        boolean isDelete=labelController.deleteLabelByName(mContext,mSearchVoList.get(position).getName());
                        if(isDelete){
                            notifyItemRemoved(position);
                            mSearchVoList.remove(mSearchVoList.get(position));
                            KeepNotifyCenterHelper.getInstance().notifyLabelChange();
                        }else {
                            Toast.makeText(mContext,"删除失败，请重试！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case EDIT_LABEL:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchVoList.size();
    }

    public void refreshDate(ArrayList<SearchVo> searchVoList){
        this.mSearchVoList=searchVoList;
        notifyDataSetChanged();
    }

    public ArrayList<SearchVo> getFinalSearchDate(){
        return mSearchFilterList;
    }

    public ArrayList<SearchVo> getSearchVoList() {
        return mSearchVoList;
    }

   public static class ViewHolder extends RecyclerViewAdapter.ViewHolder {
       public TextView mTitleTv;
       public CheckBox mSelectLabelCb;
       public ImageView mNameIconIv;
       public View mView;
       public ViewHolder(View v, List<? extends View> menuItems, SwipeItemLayout swipeItemLayout, int layoutId) {
           super(v, menuItems, swipeItemLayout, layoutId);
           this.mView=v;
           mTitleTv =(TextView) v.findViewById(R.id.label_name_tv);
           mNameIconIv = (ImageView) v.findViewById(R.id.label_icon_iv);
           mSelectLabelCb=(CheckBox) v.findViewById(R.id.select_label_cb);
       }

       public View getView() {
           return mView;
       }
   }
}
