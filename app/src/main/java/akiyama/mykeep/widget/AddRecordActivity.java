package akiyama.mykeep.widget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.Task.SaveSingleDbTask;
import akiyama.mykeep.adapter.SpinnerAdapter;
import akiyama.mykeep.base.BaseActivity;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.Notify;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.vo.LabelVo;

/**
 * 添加一条记录
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  09:55
 */
public class AddRecordActivity extends BaseActivity {

    private static final String TAG="AddRecordActivity";
    private SpinnerAdapter mSpa;
    private List<LabelVo> mLabels;
    private EditText mTitleEt;
    private EditText mContentEt;
    private Button mSaveBtn;
    private Button mGiveUpBtn;
    private Spinner mLabelSp;
    private static RecordController rc=new RecordController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_record);
    }


    @Override
    protected void findView() {
        mTitleEt=(EditText) findViewById(R.id.record_title_et);
        mContentEt=(EditText) findViewById(R.id.record_content_et);
        mLabelSp=(Spinner) findViewById(R.id.label_sp);
        mSaveBtn=(Button) findViewById(R.id.save_btn);
        mGiveUpBtn=(Button) findViewById(R.id.give_up_btn);
    }

    @Override
    protected void initView() {
        setToolBarTitle("添加记事");
        mLabels=new ArrayList<LabelVo>();
        new GetLabelsTask().execute();
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

    private void saveRecordToDb(){
        String title=mTitleEt.getText().toString();
        String content=mContentEt.getText().toString();
        if(!LoginHelper.isLogin()){
            goLogin();
        }else{
            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                RecordModel record=new RecordModel();
                record.setTitle(title);
                record.setContent(content);
                record.setLevel(RecordModel.NORMAL);
                record.setLabelId(mLabels.get(mLabelSp.getSelectedItemPosition()).getLabelName());
                record.setCreatTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setAlarmTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setUserId(LoginHelper.getCurrentUser().getObjectId());
                saveRecordTask(record);
            }else{
                Toast.makeText(mContext,"必须填写标题和内容哦！",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }

    private void goAddLabel(){
        Intent addLabel=new Intent(this,AddLabelActivity.class);
        startActivity(addLabel);
    }

    private class GetLabelsTask extends AsyncTask<Void,Void,Void>{
        private List<LabelVo> labelVos=new ArrayList<LabelVo>();
        @Override
        protected Void doInBackground(Void... params) {
            labelVos.add(new LabelVo("0","家庭"));
            labelVos.add(new LabelVo("1","工作"));
            labelVos.add(new LabelVo("2","个人"));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mLabels=labelVos;
            mSpa=new SpinnerAdapter(mContext,mLabels);
            mLabelSp.setAdapter(mSpa);
        }
    }

    private void saveRecordTask(RecordModel record){
        new SaveSingleDbTask(mContext,rc){
            @Override
            public void savePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(mContext,"保存成功！",Toast.LENGTH_LONG).show();
                    Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_RECORD);
                    mTitleEt.setText("");
                    mContentEt.setText("");
                    AddRecordActivity.this.finish();
                }
            }
        }.execute(record);
    }

}
