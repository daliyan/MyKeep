package ssj.androiddesign.widget;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import java.util.Calendar;

import ssj.androiddesign.R;
import ssj.androiddesign.base.BaseActivity;
import ssj.androiddesign.base.BaseObserverActivity;
import ssj.androiddesign.bean.Record;
import ssj.androiddesign.event.EventType;
import ssj.androiddesign.event.Notify;
import ssj.androiddesign.util.LoginHelper;

/**
 * 添加一条记录
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  09:55
 */
public class AddRecordActivity extends BaseActivity {

    private EditText mTitleEt;
    private EditText mContentEt;
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
                saveRecord();
                break;
        }
        return true;
    }

    private void saveRecord(){
        String title=mTitleEt.getText().toString();
        String content=mContentEt.getText().toString();
        if(!LoginHelper.isLogin()){
            goLogin();
        }else{
            if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                Record record=new Record();
                record.setTitle(title);
                record.setContent(content);
                record.setLevel(Record.NORMAL);
                record.setDateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                record.setCreator(LoginHelper.getCurrentUser());
                record.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Toast.makeText(mContext,"保存成功！",Toast.LENGTH_LONG).show();
                            Notify.getInstance().NotifyActivity(EventType.EVENT_ADD_RECORD);
                            AddRecordActivity.this.finish();
                        }
                    }
                });
            }else{
                Toast.makeText(mContext,"必须填写标题和内容哦！",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }
}
