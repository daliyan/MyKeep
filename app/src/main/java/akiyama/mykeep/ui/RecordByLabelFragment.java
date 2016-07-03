package akiyama.mykeep.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.helper.OnStartDragListener;
import akiyama.mykeep.adapter.RecyclerAdapter;
import akiyama.mykeep.adapter.helper.RecyclerViewTouchCallback;
import akiyama.mykeep.base.BaseObserverFragment;
import com.akiyama.data.dbservice.RecordController;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.preferences.KeepPreferenceUtil;
import akiyama.mykeep.task.DeleteSingleDbTask;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.task.QueryRecordByLabelTask;
import akiyama.mykeep.ui.main.MainActivity;
import com.akiyama.base.utils.LogUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.SvgHelper;

/**
 * 通过标签分类显示记录信息
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:01
 */
public class RecordByLabelFragment extends BaseObserverFragment implements OnStartDragListener {

    public static final String TAG="RecordByLabelFragment";
    public static final String KEY_LABEL_NAME="key_label_name";//标签名称KEY值

    private static final int DELETE_RECORD = 0;
    private static final int EDIT_RECORD = 1;
    private static final int ALARM_RECORD = 2;
    private static final int LABEL_RECORD = 3;
    private static final int PRIORITY_RECORD = 4;

    private View mEmptyView;
    private ImageView mEmptyIv;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private List<RecordModel> mRecordModels;
    private StaggeredGridLayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private int mSpanCount = 1;
    private String mLabelName="";
    private Context mContext;
    private RecordController rc=new RecordController();
    private int mCurrentSelectItem;
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
        mAdapter.setOnStartDragListener(this);
        ItemTouchHelper.Callback callback = new RecyclerViewTouchCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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
                goEditRecord(v, position);
            }
        });

        mAdapter.setOnLongItemClick(new RecyclerAdapter.OnLongItemClick() {
            @Override
            public void onLongItemClick(View v, int position) {
                ((MainActivity)getActivity()).refreshLongToolBar();
            }
        });

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
                EventType.EVENT_SWITCH_VIEW,
                EventType.EVENT_DELETE_RECORD
        };
    }

    private void goEditRecord(View v, int position){
        if (mRecordModels != null && mRecordModels.size() > position) {
            ((MainActivity)getActivity()).goEditRecordFragment(mRecordModels.get(position), v);
        }else {
            LogUtil.e(TAG,"setOnItemClick position is NV");
        }
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
     * 根据id删除记录数据
     */
    private void deleteSingleRecord(boolean isShowProgress,final int position){
        String id = mRecordModels.get(position).getId();
        new DeleteSingleDbTask(mContext, rc,isShowProgress) {
            @Override
            public void deletePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    mRecordModels.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    KeepNotifyCenterHelper.getInstance().notifyRefreshRecord();
                    Snackbar.make(getView(), R.string.delete_snackbar_msg_success, Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(getView(), R.string.delete_snackbar_msg_fail, Snackbar.LENGTH_SHORT).show();
                }
            }
        }.execute(id);
    }

    /**
     * 删除当前页面的长按选定的记录
     */
    public void deleteRecord(){
        deleteSingleRecord(false,mCurrentSelectItem);
    }


    /**
     * 切换记事本视图显示
     */
    private void refreshViewSpanCount(){
        mLayoutManager.setSpanCount(KeepPreferenceUtil.getInstance(mContext).getShowViewCount());
        mAdapter.refreshDate(mRecordModels);
    }


    private void showEditMenu(final View v, final int position){
        AlertDialog.Builder editMenuBuilder = new AlertDialog.Builder(getActivity());
        editMenuBuilder.setItems(R.array.edit_record, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DELETE_RECORD:
                        deleteSingleRecord(false,position);
                        break;
                    case EDIT_RECORD:
                        goEditRecord(v, position);
                        break;
                    case ALARM_RECORD:
                        break;
                    case LABEL_RECORD:
                        break;
                    case PRIORITY_RECORD:
                        break;
                }
            }
        });
        editMenuBuilder.show();
    }

    /**
     * 开始拖动
     *
     * @param viewHolder
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder,int position) {
        mItemTouchHelper.startDrag(viewHolder);
        mCurrentSelectItem = position;
    }
}
