package akiyama.mykeep.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseObserverFragment;
import akiyama.mykeep.common.Constants;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.task.SaveSingleDbTask;
import akiyama.mykeep.task.UpdateSingleDbTask;
import akiyama.mykeep.util.DateUtil;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.StringUtil;
import akiyama.mykeep.util.SvgHelper;
import akiyama.mykeep.view.LabelsLayout;
import akiyama.mykeep.view.RecordRecyclerView;
import akiyama.mykeep.view.calendar.DatePickerController;
import akiyama.mykeep.view.calendar.DayPickerView;
import akiyama.mykeep.view.calendar.SimpleMonthAdapter;
import akiyama.mykeep.vo.SearchVo;

/**
 * 使用Fragment来显示记录列表的详情页面</br>
 * 以前是使用Activity来显示，但是存在以下2个问题：</br>
 *  1.以前在打开记录详情页面的时候使用开始的addView方式来一条条加载对应的列表数据比较耗时，现在修改成{@link akiyama.mykeep.view.RecordRecyclerView}使用空间RecyclerView加载列表数据，打开速度会快3倍左右（以前在华为P7上面为250MS，现在为70MS左右）</br>
 *  2.进入详情页面速度比较慢，特别是在加载大量数据的时候，其中UI的渲染（setContentView(xxx)）占用52%的时间，在华为P7上面测试打开速度大概为70ms左右</br>
 *  3.使用Fragment作为详情页面可以免去重复加载VIEW的耗时</br>
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-11-12  09:42
 */
public class RecordDetailFragment extends BaseObserverFragment{

    private static final String TAG="AddRecordActivity";
    public static final String KEY_RECORD_MODE ="key_record_mode";//编辑状态
    public static final String KEY_EDIT_RECORD_LIST="ket_edit_record_list";//编辑模式下带的参数
    public static final String KEY_ADD_RECORD_TYPE ="key_add_record_type";//添加记录的类型，如列表、普通、音频、视屏 etc
    private Context mContext;
    private String mMode = StatusMode.ADD_RECORD_MODE;//默认是记录添加模式
    private int mAddRecordType;//添加记录
    private EditText mTitleEt;
    private EditText mContentEt;
    /**
     * 正常模式的VIEW
     */
    private View mNormalView = null;
    /**
     * 列表模式VIEW
     */
    private View mListView = null;
    private RecordRecyclerView mContentRlv;
    private TextView mUpdateTimeTv;
    private LabelsLayout mLabelLsl;
    private RecordModel mEditRecordModel;
    private RecordModel mStartRecord = new RecordModel();//刚刚进入添加记录页面的时候的数据，为了比较数据是否发生改变
    private RecordController rc=new RecordController();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int onSetLayoutId() {
        return R.layout.layout_add_record;
    }

    @Override
    public void findView(View view) {
        mTitleEt=(EditText) view.findViewById(R.id.record_title_et);
        mLabelLsl =(LabelsLayout) view.findViewById(R.id.label_lsl);
        mUpdateTimeTv = (TextView) view.findViewById(R.id.record_update_time_tv);
    }


    @Override
    public void initView() {
        mContext = getActivity();
        mTitleEt.setTypeface(AppContext.getRobotoSlabLight());
        mUpdateTimeTv.setTypeface(AppContext.getRobotoSlabLight());
    }

    @Override
    public void initDate() {
        setInitUiByMode();
    }

    @Override
    public void setOnClick() {
        mLabelLsl.setOnClickListener(this);
    }

    @Override
    public void initSvgView() {

    }


    /**
     * 根据当前模式设置不同的UI数据
     */
    private void setInitUiByMode(){
        mMode = getArguments().getString(KEY_RECORD_MODE);
        if(mMode!=null && mMode.equals(StatusMode.EDIT_RECORD_MODE)){
            mEditRecordModel = getArguments().getParcelable(KEY_EDIT_RECORD_LIST);
            mAddRecordType = mEditRecordModel.getRecordType();
            initTypeView(mAddRecordType);
            mStartRecord = mEditRecordModel;
            if(mEditRecordModel!=null){
                mTitleEt.setText(mEditRecordModel.getTitle());
                if(mAddRecordType == RecordModel.RECORD_TYPE_NORMAL){
                    mContentEt.setText(mEditRecordModel.getContent());
                }else if(mAddRecordType == RecordModel.RECORD_TYPE_LIST){
                    mContentRlv.setFormatText(mEditRecordModel.getContent());
                }
                mLabelLsl.setLabels(StringUtil.subStringBySymbol(mEditRecordModel.getLabelNames(), DbConfig.SPLIT_SYMBOL));
                mUpdateTimeTv.setText("修改时间："+DateUtil.getDate(mEditRecordModel.getUpdateTime()));
            }
        }else if(mMode!=null && mMode.equals(StatusMode.ADD_RECORD_MODE)){
            mAddRecordType = getArguments().getInt(KEY_ADD_RECORD_TYPE,RecordModel.RECORD_TYPE_NORMAL);
            initTypeView(mAddRecordType);
            mStartRecord.setContent("");
            mStartRecord.setTitle("");
            mStartRecord.setLabelNames("");
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.label_lsl:
                goAddLabelActivity();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        if(notifyInfo!=null){
            String eventType = notifyInfo.getEventType();
            if(eventType.equals(EventType.EVENT_SELECTED_LABEL_LIST)){
                Bundle bundle = notifyInfo.getBundle();
                List<SearchVo> searchSelectedVos = new ArrayList<SearchVo>();
                if(bundle!=null){
                    searchSelectedVos =(ArrayList<SearchVo>) bundle.get(AddLabelActivity.KEY_EXTRA_SELECTED_LABEL);
                    setSelectedLabel(searchSelectedVos);
                }
            }
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_SELECTED_LABEL_LIST
        };
    }

    /**
     * 保存或者更新数据
     */
    public void saveOrUpdateRecordToDb(){
        String title = mTitleEt.getText().toString();
        String content = getContentText();
        String labelNames = getCurrentLabel();
        if(mStartRecord!=null && mStartRecord.getTitle().equals(title)
                && mStartRecord.getContent().equals(content)
                && mStartRecord.getLabelNames().equals(labelNames)){
            return;//内容没有发生改变，直接返回
        }

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
            RecordModel record=new RecordModel();
            record.setTitle(title);
            record.setContent(content);
            record.setLevel(RecordModel.NORMAL);
            if(getCurrentLabel()!=null){
                record.setLabelNames(getCurrentLabel());
            }
            record.setCreatTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            record.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            record.setAlarmTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            record.setUserId(LoginHelper.getCurrentUserId());
            record.setRecordType(mAddRecordType);
            if(mMode.equals(StatusMode.ADD_RECORD_MODE)){
                saveRecordTask(record);
            }else if(mMode.equals(StatusMode.EDIT_RECORD_MODE)){
                record.setId(mEditRecordModel.getId());
                updateRecordTask(record);
            }
        }
    }

    private String getContentText(){
        if(mAddRecordType == RecordModel.RECORD_TYPE_NORMAL){
            return mContentEt.getText().toString();
        }else if(mAddRecordType == RecordModel.RECORD_TYPE_LIST){
            return mContentRlv.getFormatText();
        }
        return "";
    }

    private void initContentText(){
        if(mAddRecordType == RecordModel.RECORD_TYPE_NORMAL){
            mTitleEt.setText("");
            mContentEt.setText("");
        }else if(mAddRecordType == RecordModel.RECORD_TYPE_LIST){
            mTitleEt.setText("");
            mContentRlv.initList();
        }
    }

    public void goAddLabelActivity(){
        Intent addLabel=new Intent(mContext,AddLabelActivity.class);
        addLabel.putExtra(AddLabelActivity.KEY_EXTRA_SELECT_LABEL,getCurrentLabel());
        startActivity(addLabel);
    }

    /**
     * 获取当前Label标签组数据
     * @return
     */
    private String getCurrentLabel(){
        if(mLabelLsl!=null && mLabelLsl.getLabelTextStr()!=null){
            return mLabelLsl.getLabelTextStr();
        }
        return null;
    }



    private void saveRecordTask(final RecordModel record){
        final String labelName = record.getLabelNames();
        new SaveSingleDbTask(mContext,rc,false){
            @Override
            public void savePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    notifyRecordChange(labelName);
                    initContentText();
                }
            }
        }.execute(record);
    }

    private void updateRecordTask(RecordModel record){
        final String labelName = record.getLabelNames();
        new UpdateSingleDbTask(mContext,rc,false){
            @Override
            public void updatePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    notifyRecordChange(labelName);
                    mTitleEt.setText("");
                    initContentText();
                }
            }
        }.execute(record);
    }

    private void notifyRecordChange(String labelNames){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_LABEL_NAMES,labelNames);
        KeepNotifyCenterHelper.getInstance().notifyRefreshRecord(bundle);
    }


    private View getListViewStub(){
        if(mListView ==null){
            ViewStub stub = (ViewStub) getLayoutView().findViewById(R.id.record_content_list_vs);
            mListView= stub.inflate();
        }
        return mListView;
    }

    private View getNormalViewStub(){
        if(mNormalView==null){
            ViewStub stub = (ViewStub) getLayoutView().findViewById(R.id.record_content_normal_vs);
            mNormalView = stub.inflate();
        }
        return mNormalView;
    }

    private void initTypeView(int addRecordType){
        if(addRecordType == RecordModel.RECORD_TYPE_NORMAL ){
            mContentEt=(EditText) getNormalViewStub().findViewById(R.id.record_content_et);
        }else if(addRecordType == RecordModel.RECORD_TYPE_LIST){
            mContentRlv = (RecordRecyclerView) getListViewStub().findViewById(R.id.record_content_rlv);
        }else{
            LogUtil.e(TAG, "erro addRecordType");
        }
    }

    /**
     * 循环添加Label标签
     * @param selectedLabels
     */
    private void setSelectedLabel(List<SearchVo> selectedLabels){
        if(selectedLabels!=null){
            mLabelLsl.removeAllViews();
            mLabelLsl.initLabelText();
            for(SearchVo searchVo:selectedLabels){
                mLabelLsl.addLabel(searchVo.getName());
            }
        }
    }


    private void showPickDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),R.style.Theme_AlertDialog));
        builder.setTitle("记事等级");
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_pick_dialog, null);
        builder.setView(dialogView).setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    public void showDateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),R.style.Theme_AlertDialog));
        builder.setTitle("时间提醒");
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.layout_calendar_time_dialog, null);
        ImageView remainIv = (ImageView) dialogView.findViewById(R.id.remain_iv);
        final TextView remainTv = (TextView) dialogView.findViewById(R.id.remain_tv);
        DayPickerView dayPickerView = (DayPickerView) dialogView.findViewById(R.id.pickerView);
        dayPickerView.setController(new DatePickerController() {
            @Override
            public int getMaxYear() {
                return Calendar.getInstance().get(Calendar.YEAR)+1;
            }

            @Override
            public void onDayOfMonthSelected(int year, int month, int day) {
                remainTv.setText(year+"年"+(month+1)+"月"+day+"日");
            }

            @Override
            public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

            }
        });

        if(mEditRecordModel!=null && mEditRecordModel.getAlarmTime()!=null){
            remainTv.setText(DateUtil.getDate(mEditRecordModel.getAlarmTime()));
            remainTv.setTextColor(getResources().getColor(R.color.text_select));
            SvgHelper.setImageDrawable(remainIv, R.raw.ic_vibration_select_24px);
        }else{
            remainTv.setText("提醒我");
            remainTv.setTextColor(getResources().getColor(R.color.text_normal));
            SvgHelper.setImageDrawable(remainIv,R.raw.ic_vibration_24px);
        }

        builder.setView(dialogView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        }).setNegativeButton(R.string.remove, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
}
