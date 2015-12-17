package akiyama.mykeep.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseActivity;
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
import akiyama.mykeep.view.colorpicker.ColorPickerDialog;
import akiyama.mykeep.view.colorpicker.ColorPickerSwatch;
import akiyama.mykeep.vo.SearchVo;
import akiyama.mykeep.vo.ViewPivot;

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
    public static final String KEY_PIVOT_XY = "pivot_x_y";//列表页面的pivot xy值，用来现实fragment动画效果
    private String mAlarmsTime;
    private Context mContext;
    private String mMode = StatusMode.ADD_RECORD_MODE;//默认是记录添加模式
    private int mAddRecordType;//添加记录
    private EditText mTitleEt;
    private EditText mContentEt;
    private LinearLayout mAlarmLl;
    private LinearLayout mBodyLl;
    private ImageView mAlarmIv;
    private TextView mAlarmTimeTv;
    private TextView mAlarmDateTv;
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
    private String mLevel = RecordModel.DEFAULT_COLOR;//当前记事的优先级
    private RecordModel mEditRecordModel;
    private RecordModel mStartRecord = new RecordModel();//刚刚进入添加记录页面的时候的数据，为了比较数据是否发生改变
    private RecordController rc=new RecordController();

    @Override
    public int onSetLayoutId() {
        return R.layout.layout_add_record;
    }

    @Override
    public void findView(View view) {
        mTitleEt=(EditText) view.findViewById(R.id.record_title_et);
        mLabelLsl =(LabelsLayout) view.findViewById(R.id.label_lsl);
        mUpdateTimeTv = (TextView) view.findViewById(R.id.record_update_time_tv);
        mFragmentToolBar = (Toolbar) view.findViewById(R.id.toolbar);
        mAlarmLl = (LinearLayout) view.findViewById(R.id.alarm_ll);
        mAlarmIv = (ImageView) view.findViewById(R.id.alarm_iv);
        mAlarmTimeTv = (TextView) view.findViewById(R.id.alarm_time_tv);
        mAlarmDateTv = (TextView) view.findViewById(R.id.alarm_date_tv);
        mBodyLl =(LinearLayout) view.findViewById(R.id.body_ll);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setViewPivot();
        return view;
    }

    @Override
    public void initView() {
        mContext = getActivity();
        mTitleEt.setTypeface(AppContext.getRobotoSlabLight());
        mUpdateTimeTv.setTypeface(AppContext.getRobotoSlabLight());
        mAlarmTimeTv.setTypeface(AppContext.getRobotoSlabLight());
        mAlarmDateTv.setTypeface(AppContext.getRobotoSlabLight());
        setFragmentToolBarTitle("编辑记事");
    }

    @Override
    public void initDate() {
        setInitUiByMode();
    }

    @Override
    public void setOnClick() {
        mLabelLsl.setOnClickListener(this);
        mAlarmLl.setOnClickListener(this);
        mAlarmTimeTv.setOnClickListener(this);
        mAlarmDateTv.setOnClickListener(this);
    }

    @Override
    public void initSvgView() {
        SvgHelper.setImageDrawable(mAlarmIv,R.raw.ic_access_alarms_24px);
    }


    private void setViewPivot(){
        ViewPivot viewPivot = getArguments().getParcelable(KEY_PIVOT_XY);
        if(viewPivot!=null){
            mLayoutView.setPivotX(viewPivot.pivotX);
            mLayoutView.setPivotY(viewPivot.pivotY);
        }
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
                mLevel = mEditRecordModel.getLevel();
                mTitleEt.setText(mEditRecordModel.getTitle());
                if(mLevel!=null){
                    mBodyLl.setBackgroundColor(Color.parseColor(mLevel));
                }
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
            case R.id.alarm_ll:
                if(mAlarmTimeTv.getVisibility()==View.GONE){
                    createAlarmDialog();
                }
                break;
            case R.id.alarm_time_tv:
                if(mAlarmTimeTv.getVisibility()==View.VISIBLE){
                    showTimeDialog();
                }else {
                    createAlarmDialog();
                }
                break;
            case R.id.alarm_date_tv:
                if(mAlarmTimeTv.getVisibility()==View.VISIBLE){
                    showDateDialog();
                }else {
                    createAlarmDialog();
                }
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
        String level = mLevel;
        if(mStartRecord!=null && mStartRecord.getTitle().equals(title)
                && mStartRecord.getContent().equals(content)
                && mStartRecord.getLabelNames().equals(labelNames)
                && mStartRecord.getLevel().equals(level)){
            return;//内容没有发生改变，直接返回
        }

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
            RecordModel record=new RecordModel();
            record.setTitle(title);
            record.setContent(content);
            record.setLevel(level);
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

    private void setLayoutColor(int color){
        getView().setBackgroundColor(color);
        mFragmentToolBar.setBackgroundColor(color);
        ((BaseActivity)getActivity()).setStatusBarView(color);
    }


    private void createAlarmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.alarm_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    showDateDialog();
                }else if(which==1){
                    showTimeDialog();
                }
            }
        });

        builder.create().show();
    }


    /**
     * 显示优先级对话框
     */
    public void showPriorityDialog(){
        int[] colors = new int[] {
                Color.parseColor(RecordModel.NORMAL_COLOR),
                Color.parseColor(RecordModel.X_NORMAL_COLOR),
                Color.parseColor(RecordModel.XX_NORMAL_COLOR),
                Color.parseColor(RecordModel.IMPORTANT),
                Color.parseColor(RecordModel.X_IMPORTANT),
                Color.parseColor(RecordModel.XX_IMPORTANT)
        };
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(R.string.colorPicker_title,colors,Color.parseColor(mLevel),3,2);
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mLevel =  String.format("#%06X", 0xFFFFFF & color);
                setLayoutColor(color);
            }
        });
        colorPickerDialog.show(getFragmentManager(), "colorpicker");
    }



    private void showTimeDialog(){
        mAlarmTimeTv.setVisibility(View.VISIBLE);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    private void showDateDialog(){
        mAlarmTimeTv.setVisibility(View.VISIBLE);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mAlarmTimeTv.setText(hourOfDay + ":" + minute);
        }
    }


    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            mAlarmDateTv.setText(year+"年"+month+"月"+day+"日");
        }
    }

}
