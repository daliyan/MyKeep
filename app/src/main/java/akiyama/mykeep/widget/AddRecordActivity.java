package akiyama.mykeep.widget;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseObserverActivity;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.task.SaveSingleDbTask;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.Notify;
import akiyama.mykeep.task.UpdateSingleDbTask;
import akiyama.mykeep.util.DateUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.StringUtil;
import akiyama.mykeep.view.LabelsLayout;
import akiyama.mykeep.vo.SearchVo;

/**
 * 添加一条记录
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  09:55
 */
public class AddRecordActivity extends BaseObserverActivity {

    private static final String TAG="AddRecordActivity";
    public static final String KEY_RECORD_MODE ="key_record_mode";//编辑状态
    public static final String KEY_EDIT_RECORD_LIST="ket_edit_record_list";//编辑模式下带的参数
    private String mMode = StatusMode.RECORD_ADD_MODE;//默认是记录添加模式
    private EditText mTitleEt;
    private EditText mContentEt;
    private TextView mUpdateTimeTv;
    private Button mSaveBtn;
    private Button mGiveUpBtn;
    private LabelsLayout mLabelLsl;
    private RecordModel mEditRecordModel;
    private static RecordController rc=new RecordController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_record);
        setInitUiByMode();
    }

    /**
     * 根据当前模式设置不同的UI数据
     */
    private void setInitUiByMode(){
        mMode = getIntent().getStringExtra(KEY_RECORD_MODE);
        if(mMode!=null && mMode.equals(StatusMode.RECORD_EDIT_MODE)){
            mEditRecordModel = getIntent().getParcelableExtra(KEY_EDIT_RECORD_LIST);
            if(mEditRecordModel!=null){
                mTitleEt.setText(mEditRecordModel.getTitle());
                mContentEt.setText(mEditRecordModel.getContent());
                mLabelLsl.setLabels(StringUtil.subStringBySymbol(mEditRecordModel.getLabelNames(),","));
                mUpdateTimeTv.setText("修改时间："+DateUtil.getDate(mEditRecordModel.getUpdateTime()));
            }
        }
    }

    @Override
    protected void findView() {
        mTitleEt=(EditText) findViewById(R.id.record_title_et);
        mContentEt=(EditText) findViewById(R.id.record_content_et);
        mLabelLsl =(LabelsLayout) findViewById(R.id.label_lsl);
        mUpdateTimeTv = (TextView) findViewById(R.id.record_update_time_tv);
        mSaveBtn=(Button) findViewById(R.id.save_btn);
        mGiveUpBtn=(Button) findViewById(R.id.give_up_btn);
    }

    @Override
    protected void initView() {
        setToolBarTitle("添加记事");
    }

    @Override
    protected void setOnClick() {
        mSaveBtn.setOnClickListener(this);
        mGiveUpBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        if(notifyInfo!=null){
            String eventType = notifyInfo.getEventType();
            if(eventType.equals(EventType.EVENT_SELECTED_LABEL_LIST)){
                Bundle bundle = notifyInfo.getBundle();
                List<SearchVo> searchSelectedVos = new ArrayList<>();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_add_label:
                goAddLabel();
                break;
            case R.id.action_share_content:
                break;
            case R.id.action_delete_record:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.save_btn:
                saveRecordToDb();
                break;
            case R.id.give_up_btn:
                break;
            default:
                break;
        }
    }

    /**
     * 保存数据
     */
    private void saveRecordToDb(){
        String title=mTitleEt.getText().toString();
        String content=mContentEt.getText().toString();
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
            if(mMode.equals(StatusMode.RECORD_ADD_MODE)){
                saveRecordTask(record);
            }else if(mMode.equals(StatusMode.RECORD_EDIT_MODE)){
                record.setId(mEditRecordModel.getId());
                updateRecordTask(record);
            }
        }else{
            Toast.makeText(mContext,"必须填写标题和内容哦！",Toast.LENGTH_LONG).show();
        }
    }

    private void goAddLabel(){
        Intent addLabel=new Intent(this,AddLabelActivity.class);
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

    private void saveRecordTask(RecordModel record){
        new SaveSingleDbTask(mContext,rc){
            @Override
            public void savePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(mContext,"保存成功！",Toast.LENGTH_LONG).show();
                    Notify.getInstance().NotifyActivity(new NotifyInfo(EventType.EVENT_REFRESH_RECORD));
                    mTitleEt.setText("");
                    mContentEt.setText("");
                    AddRecordActivity.this.finish();
                }
            }
        }.execute(record);
    }

    private void updateRecordTask(RecordModel record){
        new UpdateSingleDbTask(mContext,rc){
            @Override
            public void updatePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(mContext,"更新成功！",Toast.LENGTH_LONG).show();
                    Notify.getInstance().NotifyActivity(new NotifyInfo(EventType.EVENT_REFRESH_RECORD));
                    mTitleEt.setText("");
                    mContentEt.setText("");
                    AddRecordActivity.this.finish();
                }
            }
        }.execute(record);
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
}
