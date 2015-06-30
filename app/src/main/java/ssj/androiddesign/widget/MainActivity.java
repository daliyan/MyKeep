package ssj.androiddesign.widget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.ArrayList;
import java.util.List;

import ssj.androiddesign.R;
import ssj.androiddesign.adapter.RecyclerAdapter;
import ssj.androiddesign.base.BaseObserverActivity;
import ssj.androiddesign.bean.Record;
import ssj.androiddesign.bean.vo.ChildRocommend;
import ssj.androiddesign.bean.vo.Recommend;
import ssj.androiddesign.event.EventType;
import ssj.androiddesign.event.Notify;
import ssj.androiddesign.util.DateUtil;
import ssj.androiddesign.util.LoginHelper;


public class MainActivity extends BaseObserverActivity implements View.OnClickListener{

    private final static String TAG="MainActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBarCircularIndeterminate mProgressBar;
    private List<Recommend> mRecommends;

    private DrawerLayout mDrawerDl;//侧滑菜单布局控件
    private ActionBarDrawerToggle mDrawerToggle;

    private View mRecordView;
    private View mFiledView;
    private View mRecycleView;
    private View mSettingView;
    private View mSyncView;//同步数据
    private View mHelpView;//帮助

    private TextView mRecordTv;
    private TextView mFiledTv;
    private TextView mRecycleTv;
    private TextView mSettingTv;
    private TextView mSyncTv;
    private TextView mHelpTv;

    private ImageView mRecordIv;
    private ImageView mFiledIv;
    private ImageView mRecycleIv;
    private ImageView mSettingIv;
    private ImageView mSyncIv;
    private ImageView mHelpIv;
    private TextView mUserNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDrawer();
        setUserInfo();
        getRecord();
    }


    @Override
    protected void findView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mProgressBar=(ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        mDrawerDl=(DrawerLayout)findViewById(R.id.left_menu_dl);
        mLayoutManager = new LinearLayoutManager(this);

        mRecordView= findViewById(R.id.item_menu_1);
        mRecordTv =(TextView) mRecordView.findViewById(R.id.title_tv);
        mRecordIv =(ImageView) mRecordView.findViewById(R.id.title_iv);

        mFiledView= findViewById(R.id.item_menu_2);
        mFiledTv =(TextView) mFiledView.findViewById(R.id.title_tv);
        mFiledIv =(ImageView) mFiledView.findViewById(R.id.title_iv);

        mRecycleView= findViewById(R.id.item_menu_3);
        mRecycleTv =(TextView) mRecycleView.findViewById(R.id.title_tv);
        mRecycleIv =(ImageView) mRecycleView.findViewById(R.id.title_iv);

        mSettingView = findViewById(R.id.item_menu_4);
        mSettingTv =(TextView) mSettingView.findViewById(R.id.title_tv);
        mSettingIv =(ImageView) mSettingView.findViewById(R.id.title_iv);

        mSyncView= findViewById(R.id.item_menu_5);
        mSyncTv=(TextView) mSyncView.findViewById(R.id.title_tv);
        mSyncIv=(ImageView) mSyncView.findViewById(R.id.title_iv);

        mHelpView= findViewById(R.id.item_menu_6);
        mHelpTv=(TextView) mHelpView.findViewById(R.id.title_tv);
        mHelpIv=(ImageView) mHelpView.findViewById(R.id.title_iv);

        mUserNameTv =(TextView) findViewById(R.id.username_tv);
    }

    @Override
    protected void initView(){
        setToolBarTitle("记事");
        mProgressBar.setVisibility(View.GONE);
        mRecommends=new ArrayList<Recommend>();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(mRecommends);
        mRecyclerView.setAdapter(mAdapter);
        mRecordView.setBackgroundResource(R.color.light_gray);
        mFiledView.setBackgroundResource(R.color.white);
        mRecycleView.setBackgroundResource(R.color.white);
        mSettingView.setBackgroundResource(R.color.white);
        setLeftMenuItem();
    }

    @Override
    protected void setOnClick(){
        mRecordView.setOnClickListener(this);
        mRecycleView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mFiledView.setOnClickListener(this);
    }

    @Override
    protected void setBackListener() {
    }

    @Override
    protected void onChange(String eventType) {
        if(eventType.equals(EventType.EVENT_LOGIN)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(LoginHelper.getCurrentUser().getUsername());
            getRecord();
        }else if(eventType.equals(EventType.EVENT_LOGINOUT)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(getResources().getString(R.string.no_login));
            mRecommends.clear();
            mAdapter = new RecyclerAdapter(mRecommends);
            mRecyclerView.setAdapter(mAdapter);
        }else if(eventType.equals(EventType.EVENT_ADD_RECORD)){
            getRecord();
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_LOGIN,
                EventType.EVENT_LOGINOUT,
                EventType.EVENT_ADD_RECORD
        };
    }

    /**
     * 设置左侧菜单的文字和图标
     */
   private void setLeftMenuItem(){
       mRecordTv.setText(getResources().getString(R.string.item_menu_1));
       mRecordIv.setImageResource(R.drawable.ic_assignment_black_24dp);

       mFiledTv.setText(getResources().getString(R.string.item_menu_2));
       mFiledIv.setImageResource(R.drawable.ic_access_alarms_black_24dp);

       mRecycleTv.setText(getResources().getString(R.string.item_menu_3));
       mRecycleIv.setImageResource(R.drawable.ic_drafts_black_24dp);

       mSettingTv.setText(getResources().getString(R.string.item_menu_4));
       mSettingIv.setImageResource(R.drawable.ic_settings_black_24dp);

       mSyncTv.setText(getResources().getString(R.string.item_menu_5));
       mSyncIv.setImageResource(R.drawable.ic_loop_black_24dp);

       mHelpTv.setText(getResources().getString(R.string.item_menu_6));
       mHelpIv.setImageResource(R.drawable.ic_help_black_24dp);
   }

    private void setDrawer(){
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerDl, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerDl.setDrawerListener(mDrawerToggle);
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(){
        if(LoginHelper.isLogin()){
            mUserNameTv.setText(LoginHelper.getCurrentUser().getUsername());
        }else{
            mUserNameTv.setText(getResources().getString(R.string.no_login));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem login=menu.findItem(R.id.action_login);
        AVUser avUser=LoginHelper.getCurrentUser();
        if(avUser!=null){
            login.setTitle(getResources().getString(R.string.loginOut));
        }else{
            login.setTitle(getResources().getString(R.string.action_login));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_login:
                if(LoginHelper.isLogin()){
                    AVUser.logOut();//清除当前缓存的数据
                    Notify.getInstance().NotifyActivity(EventType.EVENT_LOGINOUT);//通知注销登录信息
                    Toast.makeText(this, "注销成功！", Toast.LENGTH_LONG).show();
                }else{
                    goLogin();
                }
                break;
            case R.id.action_add:
                goAddRcord();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.item_menu_1:
                mRecordView.setBackgroundResource(R.color.light_gray);
                mFiledView.setBackgroundResource(R.color.white);
                mRecycleView.setBackgroundResource(R.color.white);
                mSettingView.setBackgroundResource(R.color.white);
                break;
            case R.id.item_menu_2:
                mRecordView.setBackgroundResource(R.color.white);
                mFiledView.setBackgroundResource(R.color.light_gray);
                mRecycleView.setBackgroundResource(R.color.white);
                mSettingView.setBackgroundResource(R.color.white);
                break;
            case R.id.item_menu_3:
                mRecordView.setBackgroundResource(R.color.white);
                mFiledView.setBackgroundResource(R.color.white);
                mRecycleView.setBackgroundResource(R.color.light_gray);
                mSettingView.setBackgroundResource(R.color.white);
                break;
            case R.id.item_menu_4:
                mRecordView.setBackgroundResource(R.color.white);
                mFiledView.setBackgroundResource(R.color.white);
                mRecycleView.setBackgroundResource(R.color.white);
                mSettingView.setBackgroundResource(R.color.light_gray);
                break;
            default:
                break;
        }
    }

    private void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }

    private void goAddRcord(){
        Intent addRecord=new Intent(this,AddRecordActivity.class);
        startActivity(addRecord);
    }

    private List<Recommend> getData(List<Record> records){
        List<Recommend> mainRecylers=new ArrayList<Recommend>();
        for(int i=0;i<records.size();i++){
            mainRecylers.add(new Recommend(new ChildRocommend(records.get(i).getTitle(),records.get(i).getContent(),R.drawable.test),
                    DateUtil.getDate(records.get(i).getDateTime())));
        }
        return mainRecylers;
    }


    private void getRecord(){
        if(LoginHelper.isLogin()){
            mProgressBar.setVisibility(View.VISIBLE);
            AVQuery<Record> query = new AVQuery<Record>("Record");
            query.whereNotEqualTo("creator", LoginHelper.getCurrentUser().getUsername());
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Record>() {
                public void done(List<Record> avObjects, AVException e) {
                    if (null == avObjects || null != e) {
                        return;
                    }
                    mRecommends=getData(avObjects);
                    mAdapter = new RecyclerAdapter(mRecommends);
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void loginOut(){

    }
}
