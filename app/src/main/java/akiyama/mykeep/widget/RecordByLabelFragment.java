package akiyama.mykeep.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.RecyclerAdapter;
import akiyama.mykeep.base.BaseObserverFragment;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.preferences.KeepPreferenceUtil;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.task.QueryRecordByLabelTask;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.SvgHelper;

/**
 * 通过标签分类显示记录信息
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:01
 */
public class RecordByLabelFragment extends BaseObserverFragment {

    public static final String TAG="RecordByLabelFragment";
    public static final String KEY_LABEL_NAME="key_label_name";//标签名称KEY值
    public static final String KEY_CHANGE_MENU="key_change_menu";//修改菜单标记
    private View mEmptyView;
    private ImageView mEmptyIv;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<RecordModel> mRecordModels;
    private StaggeredGridLayoutManager mLayoutManager;
    private int mSpanCount = 1;
    private String mLabelName="";
    private Context mContext;
    private RecordController rc=new RecordController();
    @Override
    public int onSetLayoutId() {
        return R.layout.fragemnt_record_label_list;
    }

    @Override
    public void findView(View view) {
        mContext = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.record_rv);
        mEmptyView = view.findViewById(R.id.empty_include);
        mEmptyIv = (ImageView) mEmptyView.findViewById(R.id.empty_iv);
        mLayoutManager = new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    /**
     * 初始化SVG资源
     */
    @Override
    public void initSvgView() {
        SvgHelper.setImageDrawable(mEmptyIv,R.raw.ic_mode_edit_48px);
    }

    @Override
    public void initView() {
        mSpanCount = KeepPreferenceUtil.getInstance(mContext).getShowViewCount();
        mRecordModels =new ArrayList<>();
        mLabelName = getArguments().getString(KEY_LABEL_NAME);//获取需要加载的标签分类名称
        mAdapter = new RecyclerAdapter(mRecordModels);

        mLayoutManager = new StaggeredGridLayoutManager(mSpanCount,StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mRecordModels);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void initDate() {
        queryRecordByLabel(false);
    }


    @Override
    public void setOnClick() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                //如果View被选定，点击就取消选定状态
                if(v.isSelected()){
                    LogUtil.d(TAG, "onItem:" + v.hashCode());
                    switchActionBarMenu(StatusMode.MENU_NORMAL);
                }else{
                    if (mRecordModels != null && mRecordModels.size() > position) {
                        ((MainActivity)getActivity()).goEditRecordFragment(mRecordModels.get(position), v);
                        //goEditRecordActivity(mRecordModels.get(position), v);
                    }else {
                        LogUtil.e(TAG,"setOnItemClick position is NV");
                    }
                }
            }
        });

        mAdapter.setOnLongItemClick(new RecyclerAdapter.OnLongItemClick() {
            @Override
            public void onLongItemClick(View v, int position) {
                switchActionBarMenu(StatusMode.MENU_EDIT);
               // mAdapter.notifyItemRemoved(position);
                LogUtil.e(TAG,"LongItem: "+v.hashCode()+" "+position);
                v.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        });

    }

    private void switchActionBarMenu(String actionbarMode){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CHANGE_MENU,actionbarMode);
        KeepNotifyCenterHelper.getInstance().notifySwitchMenu(bundle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        String eventType = notifyInfo.getEventType();
        if(eventType.equals(EventType.EVENT_LOGIN)){
            queryRecordByLabel(false);
        }else if(eventType.equals(EventType.EVENT_LOGINOUT)){
            mRecordModels.clear();
            mAdapter.refreshDate(mRecordModels);
        }else if(eventType.equals(EventType.EVENT_REFRESH_RECORD)){
            //不针对具体的标签刷新，直接全部刷新，一来可以避免针对不同标签做刷新可能引起的问题（在移除标签的时候并不好操作），二来全部刷新并也不会影响APP的性能，因为当前存活TAB页面很少（一到2个）
            queryRecordByLabel(false);
        }else if(eventType.equals(EventType.EVENT_SWITCH_VIEW)){
            refreshViewSpanCount();
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_LOGIN,
                EventType.EVENT_LOGINOUT,
                EventType.EVENT_REFRESH_RECORD,
                EventType.EVENT_SWITCH_VIEW
        };
    }


    /**
     * 通过标签查询记录数据
     */
    private void queryRecordByLabel(boolean isShowProgress){
        if(mLabelName.equals(mContext.getString(R.string.all_label))){
            queryAllRecord(isShowProgress);
        }else {
            queryLabelRecord(isShowProgress);
        }
    }

    /**
     * 查询对应标签的记录
     */
    private void queryLabelRecord(boolean isShowProgress){
        new QueryRecordByLabelTask(mContext, rc,isShowProgress) {
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null){
                    mRecordModels =(List<RecordModel>) models;
                    mAdapter.refreshDate(mRecordModels);
                    //设置空状态下的视图
                    if(models.size() >0){
                        mEmptyView.setVisibility(View.GONE);
                    }else{
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }.execute(LoginHelper.getCurrentUserId(),mLabelName);
    }

    /**
     * 查询所有的记录数据
     */
    private void queryAllRecord(boolean isShowProgress){
        new QueryByUserDbTask(mContext, rc,isShowProgress) {
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null){
                    mRecordModels.clear();
                    mRecordModels.addAll((List<RecordModel>) models);
                    mAdapter.refreshDate(mRecordModels);
                    //设置空状态下的视图
                    if(models.size() >0){
                        mEmptyView.setVisibility(View.GONE);
                    }else{
                        mEmptyView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }.execute(LoginHelper.getCurrentUserId());
    }


    /**
     * 切换记事本视图显示
     */
    private void refreshViewSpanCount(){
        mLayoutManager.setSpanCount(KeepPreferenceUtil.getInstance(mContext).getShowViewCount());
        mAdapter.refreshDate(mRecordModels);
    }
}
