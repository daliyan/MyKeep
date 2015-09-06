package akiyama.mykeep.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.RecyclerAdapter;
import akiyama.mykeep.base.BaseFragment;
import akiyama.mykeep.base.BaseObserverActivity;
import akiyama.mykeep.base.BaseObserverFragment;
import akiyama.mykeep.common.Constants;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.task.QueryRecordByLabelTask;
import akiyama.mykeep.util.DateUtil;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.StringUtil;

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
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<RecordModel> mRecordModels;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBarCircularIndeterminate mProgressBar;
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
        mProgressBar=(ProgressBarCircularIndeterminate) view.findViewById(R.id.progressBarCircularIndeterminate);
        mLayoutManager = new LinearLayoutManager(mContext);
    }

    @Override
    public void initView() {
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecordModels =new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mRecordModels);
        mRecyclerView.setAdapter(mAdapter);
        mLabelName = getArguments().getString(KEY_LABEL_NAME);//获取需要加载的标签分类名称
    }

    @Override
    public void initDate() {
        final long startTime=System.currentTimeMillis();   //获取开始时间
        queryRecordByLabel(false);
        long endTime1=System.currentTimeMillis(); //获取结束时间
        LogUtil.e(TAG,mLabelName +" 执行耗时："+ (endTime1 - startTime)+"ms");
    }


    @Override
    public void setOnClick() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                if (mRecordModels != null && mRecordModels.size() > position) {
                    goEditLabelActivity(mRecordModels.get(position));
                }else {
                    LogUtil.e(TAG,"setOnItemClick position is NV");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void goEditLabelActivity(RecordModel recordModel){
        Intent goEditRecord = new Intent(mContext,AddRecordActivity.class);
        goEditRecord.putExtra(AddRecordActivity.KEY_RECORD_MODE, StatusMode.RECORD_EDIT_MODE);
        goEditRecord.putExtra(AddRecordActivity.KEY_EDIT_RECORD_LIST, recordModel);
        mContext.startActivity(goEditRecord);
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
        final long startTime=System.currentTimeMillis();   //获取开始时间
        new QueryRecordByLabelTask(mContext, rc,isShowProgress) {
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null){
                    long endTime=System.currentTimeMillis(); //获取结束时间
                    //LogUtil.e(TAG,this.hashCode()+ mLabelName +" queryLabelRecord() "+models.size());
                    mRecordModels.clear();
                    mRecordModels.addAll((List<RecordModel>) models);
                    mAdapter.refreshDate(mRecordModels);
                    mProgressBar.setVisibility(View.GONE);
                    LogUtil.e(TAG,mLabelName +" 查询耗时："+ (endTime - startTime)+"ms");
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
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }.execute(LoginHelper.getCurrentUserId());
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
            String labels = notifyInfo.getBundleString(Constants.KEY_LABEL_NAMES);
            if(labels!=null){
               String[] labelNames = StringUtil.subStringBySymbol(labels, DbConfig.LABEL_SPLIT_SYMBOL);
                //“全部”标签组或者需要刷新的标签组刷新数据
               if(StringUtil.isContains(labelNames,mLabelName) || mLabelName.equals(getString(R.string.all_label))){
                   queryRecordByLabel(false);
               }
            }
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_LOGIN,
                EventType.EVENT_LOGINOUT,
                EventType.EVENT_REFRESH_RECORD
        };
    }

    public String getLabelName() {
        return mLabelName;
    }
}
