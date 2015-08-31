package akiyama.mykeep.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.RecyclerAdapter;
import akiyama.mykeep.base.BaseFragment;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.util.LoginHelper;

/**
 * 通过标签分类显示记录信息
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:01
 */
public class RecordByLabelFragment extends BaseFragment{

    private static final String KEY_LABEL_NAME="key_label_name";//标签名称KEY值
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
        mRecordModels =new ArrayList<RecordModel>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mRecordModels);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initDate() {
        //mLabelName = getArguments().getString(KEY_LABEL_NAME);//获取需要加载的标签分类名称
        queryRecord();
    }


    @Override
    public void setOnClick() {
        mAdapter.setOnItemClick(new RecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                if (mRecordModels != null && mRecordModels.size() > position) {
                    goEditLabelActivity(mRecordModels.get(position));
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
        startActivity(goEditRecord);
    }

    /**
     * 查询记录数据
     */
    private void queryRecord(){
        new QueryByUserDbTask(mContext, rc) {
            @Override
            protected void queryPreExecute() {
                //super.queryPreExecute();
            }

            /**
             * 查询数据成功后执行的操作
             *
             * @param models
             */
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null){
                    mRecordModels =(List<RecordModel>) models;
                    mAdapter.refreshDate(mRecordModels);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }.execute(LoginHelper.getCurrentUserId());
    }

}
