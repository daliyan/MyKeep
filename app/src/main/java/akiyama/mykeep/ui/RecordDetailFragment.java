package akiyama.mykeep.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseActivity;
import akiyama.mykeep.base.BaseObserverFragment;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.dbservice.RecordController;
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
public class RecordDetailFragment extends BaseObserverFragment implements DateFragment.OnTimeSetListener{

    private static final String TAG="AddRecordActivity";
    public static final String KEY_RECORD_MODE ="key_record_mode";//编辑状态
    public static final String KEY_EDIT_RECORD_LIST="ket_edit_record_list";//编辑模式下带的参数
    public static final String KEY_ADD_RECORD_TYPE ="key_add_record_type";//添加记录的类型，如列表、普通、音频、视屏 etc
    public static final String KEY_PIVOT_XY = "pivot_x_y";//列表页面的pivot xy值，用来现实fragment动画效果
    public static final int REQUEST_SELECT_IMAGE = 0;
    public static final int REQUEST_SELECT_CAMERA = 1;
    private long mAlarmsTime = 0;
    private Context mContext;
    private String mMode = StatusMode.ADD_RECORD_MODE;//默认是记录添加模式
    private int mAddRecordType;//添加记录
    private EditText mTitleEt;
    private EditText mContentEt;
    private LinearLayout mAlarmLl;
    private LinearLayout mBodyLl;
    private ImageView mAlarmIv;
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
    private SimpleDraweeView mSimpleDraweView;
    private String mUrl = null;

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
        mAlarmDateTv = (TextView) view.findViewById(R.id.alarm_date_tv);
        mBodyLl =(LinearLayout) view.findViewById(R.id.body_ll);
        mSimpleDraweView = (SimpleDraweeView) view.findViewById(R.id.record_view_dv);
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
        mAlarmDateTv.setTypeface(AppContext.getRobotoSlabLight());
    }

    @Override
    public void initDate() {
        setInitUiByMode();
    }

    @Override
    public void setOnClick() {
        mLabelLsl.setOnClickListener(this);
        mAlarmLl.setOnClickListener(this);
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
            initEditDate();
        }else if(mMode!=null && mMode.equals(StatusMode.ADD_RECORD_MODE)){
            initAddDate();
        }
    }

    private void initEditDate(){
        setFragmentToolBarTitle("编辑记事");
        mEditRecordModel = getArguments().getParcelable(KEY_EDIT_RECORD_LIST);
        mAddRecordType = mEditRecordModel.getRecordType();
        initTypeView(mAddRecordType);
        mStartRecord = mEditRecordModel;
        if(mEditRecordModel!=null){
            mLevel = mEditRecordModel.getLevel();
            mTitleEt.setText(mEditRecordModel.getTitle());
            if(mLevel!=null){
                setLayoutColor(Color.parseColor(mLevel));
            }
            mAlarmsTime = Long.parseLong(mEditRecordModel.getAlarmTime());
            mAlarmDateTv.setText(formatTime(mEditRecordModel.getAlarmTime()));
            if(mAddRecordType == RecordModel.RECORD_TYPE_NORMAL){
                mContentEt.setText(mEditRecordModel.getContent());
            }else if(mAddRecordType == RecordModel.RECORD_TYPE_LIST){
                mContentRlv.setFormatText(mEditRecordModel.getContent());
            }
            mLabelLsl.setLabels(StringUtil.subStringBySymbol(mEditRecordModel.getLabelNames(), DbConfig.SPLIT_SYMBOL));
            mUpdateTimeTv.setText("修改时间："+DateUtil.getDate(mEditRecordModel.getUpdateTime()));
        }
    }

    private void initAddDate(){
        setFragmentToolBarTitle("添加记事");
        mAddRecordType = getArguments().getInt(KEY_ADD_RECORD_TYPE,RecordModel.RECORD_TYPE_NORMAL);
        initTypeView(mAddRecordType);
        mStartRecord.setContent("");
        mStartRecord.setTitle("");
        mStartRecord.setLabelNames("");
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.label_lsl:
                goAddLabelActivity();
                break;
            case R.id.alarm_ll:
                showDateDialog();
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
                && mStartRecord.getLevel().equals(level)
                && mStartRecord.getAlarmTime().equals(mAlarmsTime)){
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
            record.setCreateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            record.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            //默认提醒时间是0
            record.setAlarmTime(String.valueOf(mAlarmsTime));
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

    public void setSelectImage(Uri uri){
        mSimpleDraweView.setImageURI(uri);
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
        new SaveSingleDbTask(mContext,rc,false){
            @Override
            public void savePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    notifyRecordChange();
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
                    notifyRecordChange();
                    mTitleEt.setText("");
                    initContentText();
                }
            }
        }.execute(record);
    }

    private void notifyRecordChange(){
        KeepNotifyCenterHelper.getInstance().notifyRefreshRecord();
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
        mLayoutView.setBackgroundColor(color);
        //默认颜色就不修改状态栏和toolbar颜色
        if(color != Color.parseColor(RecordModel.DEFAULT_COLOR)){
            mFragmentToolBar.setBackgroundColor(color);
            ((BaseActivity)getActivity()).setStatusBarView(color);
        }
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
                //大小写需要区分一下
                mLevel =  String.format("#%06X", 0xFFFFFF & color);
                setLayoutColor(color);
            }
        });
        colorPickerDialog.show(getFragmentManager(), "colorpicker");
    }

    /**
     * 调用相机获取图像
     */
    public void showPhoneDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_select_image_dialog,null);
        builder.setView(view);
        builder.setTitle("选择照片");
        LinearLayout phoneLl= (LinearLayout)view.findViewById(R.id.photo_ll);
        LinearLayout galleryLl= (LinearLayout)view.findViewById(R.id.gallery_ll);
        ImageView phoneIv =(ImageView) view.findViewById(R.id.photo_iv);
        ImageView galleryIv =(ImageView) view.findViewById(R.id.gallery_iv);
        SvgHelper.setImageDrawable(phoneIv,R.raw.ic_photo_camera_48px);
        SvgHelper.setImageDrawable(galleryIv,R.raw.ic_photo_album_24px);
        final AlertDialog alertDialog = builder.create();
        phoneLl.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_SELECT_CAMERA);
                alertDialog.dismiss();
            }
        });

        galleryLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //注意要通过activity来
                startActivityForResult(intent, REQUEST_SELECT_IMAGE);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE) {
            Uri photoUri = data.getData();
            mUrl = photoUri.toString();
            if (photoUri != null) {
                try {
                    setSelectImage(photoUri);
                } catch (Exception e) {
                    LogUtil.e(TAG,e.getStackTrace().toString());
                }
            }
        }else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_CAMERA){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            mSimpleDraweView.setImageBitmap(photo);
        }
    }

    private void showDateDialog(){
        DateFragment dateFragment = new DateFragment();
        dateFragment.setOnTimeSetListener(this);
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onInitDateTimeSet(DatePicker datePicker, TimePicker timePicker) {
        if(mAlarmsTime!=0){
            Calendar dateTime = Calendar.getInstance();
            dateTime.setTimeInMillis(mAlarmsTime);
            int year = dateTime.get(Calendar.YEAR);
            int month = dateTime.get(Calendar.MONTH);
            int day = dateTime.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = dateTime.get(Calendar.HOUR_OF_DAY);
            int minute = dateTime.get(Calendar.MINUTE);
            if(datePicker!=null && timePicker!=null){
                datePicker.updateDate(year,month,day);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(hourOfDay);
                timePicker.setCurrentMinute(minute);
            }
        }

    }

    @Override
    public void onDateTimeSet(int year, int month, int day, int hourOfDay, int minute) {
        Calendar dateTime = Calendar.getInstance();
        dateTime.set(year,month,day,hourOfDay,hourOfDay,minute);
        mAlarmsTime = dateTime.getTimeInMillis();
        mAlarmDateTv.setText(year+"年"+(month+1)+"月"+day+"日"+" "+hourOfDay+":"+minute);
    }

    private String formatTime(String timeInMillis){
        Calendar dateTime = Calendar.getInstance();
        dateTime.setTimeInMillis(Long.parseLong(timeInMillis));
        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH);
        int day = dateTime.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = dateTime.get(Calendar.HOUR_OF_DAY);
        int minute = dateTime.get(Calendar.MINUTE);
        return year+"年"+(month+1)+"月"+day+"日"+" "+hourOfDay+":"+minute;
    }

}
