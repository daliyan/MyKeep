package akiyama.mykeep.widget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;



import java.util.Calendar;

import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseActivity;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.Notify;
import akiyama.mykeep.util.LoginHelper;

/**
 * 添加一条记录
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  09:55
 */
public class AddRecordActivity extends BaseActivity {

    private static final String TAG="AddRecordActivity";
    private EditText mTitleEt;
    private EditText mContentEt;
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
    }

    @Override
    protected void initView() {
        setToolBarTitle("添加记事");
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_record,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_check:
                saveRecordToDb();
                break;
        }
        return true;
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
                record.setCreatTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setAlarmTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setUserId(LoginHelper.getCurrentUser().getObjectId());
                new SaveRecordTask().execute(record);
            }else{
                Toast.makeText(mContext,"必须填写标题和内容哦！",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }

    private class SaveRecordTask extends AsyncTask<RecordModel,Void,Boolean>{

        private ProgressDialog mProgressBar;

        @Override
        protected void onPreExecute() {
            mProgressBar=new ProgressDialog(mContext);
            mProgressBar.setMessage("正在保存，请稍后......");
            mProgressBar.show();
        }

        @Override
        protected Boolean doInBackground(RecordModel... params) {
            if(rc.insert(mContext,params[0])!=null){
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                Toast.makeText(mContext,"保存成功！",Toast.LENGTH_LONG).show();
                Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_RECORD);
                mTitleEt.setText("");
                mContentEt.setText("");
                mProgressBar.hide();
            }
        }
    }
}
