package ssj.androiddesign.widget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import ssj.androiddesign.R;
import ssj.androiddesign.adapter.RecyclerAdapter;
import ssj.androiddesign.base.BaseActivity;
import ssj.androiddesign.bean.vo.ChildRocommend;
import ssj.androiddesign.bean.vo.Recommend;
import ssj.androiddesign.util.DateUtil;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private final static String TAG="MainActivity";
    public final static int LOGIN_REQUEST=0;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBarCircularIndeterminate mProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDrawer();
    }

    @Override
    protected void setBackListener() {
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
    }

    @Override
    protected void initView(){
        setToolBarTitle("记事");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
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

    private List<Recommend> getData(){
        List<Recommend> mainRecylers=new ArrayList<Recommend>();
        for(int i=0;i<100;i++){
            if(i%2==0){
                mainRecylers.add(new Recommend(new ChildRocommend("aspen","读书或者旅行",R.drawable.test),DateUtil.getNowDayMothString(i)));
                for(int j=0;j<2;j++){
                    mainRecylers.add(new Recommend(new ChildRocommend("aspen","读书或者旅行",R.drawable.test),null));
                }
            }else{
                mainRecylers.add(new Recommend(new ChildRocommend("yzw","高富帅",R.drawable.me),DateUtil.getNowDayMothString(i)));
                for(int j=0;j<1;j++){
                    mainRecylers.add(new Recommend(new ChildRocommend("yzw","高富帅",R.drawable.me),null));
                }
            }
        }
        return mainRecylers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_login:
                showLoginDilog();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    private void showLoginDilog(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivityForResult(login,LOGIN_REQUEST);
    }
}
