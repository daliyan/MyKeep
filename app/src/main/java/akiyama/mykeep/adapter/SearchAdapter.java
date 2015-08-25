package akiyama.mykeep.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.controller.LabelController;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.Notify;
import akiyama.mykeep.util.DimUtil;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.vo.SearchVo;
import akiyama.swipe.adapter.RecyclerViewAdapter;
import akiyama.swipe.swipe.SwipeItemLayout;
import akiyama.swipe.swipe.SwipeMenuItem;

/**
 * 标签查询、添加、删除、编辑适配器
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  16:21
 */
public class SearchAdapter extends RecyclerViewAdapter<SearchAdapter.ViewHolder> {

    private static final int EDIT_LABEL=0;
    private static final int DELETE_LABEL=1;
    private List<SearchVo> mSearchVoList;
    private List<SearchVo> mSearchFilterList=new ArrayList<>();
    private Context mContext;
    private int mPosition;

    public SearchAdapter(Context context,List<SearchVo> searchVoList){
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
        }
        mPosition=position;
        holder.mSelectLabelCb.setClickable(false);

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
                Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_LABEL_LIST);
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
                            mSearchVoList.remove(mSearchVoList.get(position));
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(mContext,"删除失败，请重试！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case EDIT_LABEL:
                        Toast.makeText(mContext,position+"取消事件执行",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

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

   public static class ViewHolder extends RecyclerViewAdapter.ViewHolder {
       public TextView mTitleTv;
       public CheckBox mSelectLabelCb;
       public View mView;
       public ViewHolder(View v, List<? extends View> menuItems, SwipeItemLayout swipeItemLayout, int layoutId) {
           super(v, menuItems, swipeItemLayout, layoutId);
           this.mView=v;
           mTitleTv =(TextView) v.findViewById(R.id.label_name_tv);
           mSelectLabelCb=(CheckBox) v.findViewById(R.id.select_label_cb);
       }

       public View getView() {
           return mView;
       }
   }
}
