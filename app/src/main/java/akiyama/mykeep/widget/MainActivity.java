package akiyama.mykeep.widget;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.RecordByLabelAdapter;
import akiyama.mykeep.base.BaseObserverActivity;
import akiyama.mykeep.common.StatusMode;
import akiyama.mykeep.controller.LabelController;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.LabelModel;
import akiyama.mykeep.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;




import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.preferences.KeepPreferenceUtil;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.SvgHelper;
import akiyama.mykeep.vo.ViewPivot;


public class MainActivity extends BaseObserverActivity implements View.OnClickListener{
    private static final String TAG="MainActivity";

    private static final int MAIN = 0;
    private static final int DETAIL = 1;
    private String mMenuMode=StatusMode.MENU_NORMAL;
    /**
     * 单行视图
     */
    private static final int SINGLE_VIEW=1;
    /**
     * 多行视图
     */
    private static final int MANY_VIEW=2;
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
    private LinearLayout mLoginLl;
    private LinearLayout mDetailContentLy;
    private RecordDetailFragment mDetailFragment;
    private FloatingActionsMenu mAddRecordMenuFam;//记事菜单
    private FloatingActionButton mAddNormalRecordFab;//增加普通计事
    private FloatingActionButton mAddListRecordFab;//添加列表数据

    private ViewPager mRecordVp;
    private TabLayout mTabLy;
    private RecordByLabelAdapter mRecordLabelAdapter;
    private List<LabelModel> mLabelList;
    private LabelController mLc = new LabelController();


    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private int mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDrawer();
        setUserInfo();
        queryLabeles();
    }

    @Override
    protected void findView(){
        mDrawerDl=(DrawerLayout)findViewById(R.id.left_menu_dl);
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
        mLoginLl = (LinearLayout) findViewById(R.id.login_ll);

        mRecordVp = (ViewPager) findViewById(R.id.content_vp);
        mTabLy = (TabLayout) findViewById(R.id.pager_strip_tsv);

        mDetailContentLy = (LinearLayout) findViewById(R.id.detail_ll);
        mAddRecordMenuFam = (FloatingActionsMenu) findViewById(R.id.add_record_fam);
        mAddNormalRecordFab = (FloatingActionButton) findViewById(R.id.add_normal_record_fab);
        mAddListRecordFab = (FloatingActionButton) findViewById(R.id.add_list_record_fab);

    }

    /**
     * 初始化SVG资源
     */
    @Override
    public void initSvgView() {
        mRecordTv.setText(getResources().getString(R.string.item_menu_1));
        SvgHelper.setImageDrawable(mRecordIv, R.raw.ic_assignment_24px);

        mFiledTv.setText(getResources().getString(R.string.item_menu_2));
        SvgHelper.setImageDrawable(mFiledIv, R.raw.ic_access_alarms_24px);

        mRecycleTv.setText(getResources().getString(R.string.item_menu_3));
        SvgHelper.setImageDrawable(mRecycleIv, R.raw.ic_drafts_24px);

        mSettingTv.setText(getResources().getString(R.string.item_menu_4));
        SvgHelper.setImageDrawable(mSettingIv, R.raw.ic_settings_24px);

        mSyncTv.setText(getResources().getString(R.string.item_menu_5));
        SvgHelper.setImageDrawable(mSyncIv, R.raw.ic_loop_24px);

        mHelpTv.setText(getResources().getString(R.string.item_menu_6));
        SvgHelper.setImageDrawable(mHelpIv, R.raw.ic_help_24px);
    }

    @Override
    protected void initView(){
        mCurrentFragment = MAIN;
        setToolBarTitle("记事");
        mLabelList = new ArrayList<>();
        mRecordView.setBackgroundResource(R.color.light_gray);
        mFiledView.setBackgroundResource(R.color.white);
        mRecycleView.setBackgroundResource(R.color.white);
        mSettingView.setBackgroundResource(R.color.white);
        mRecordLabelAdapter = new RecordByLabelAdapter(getFragmentManager(),mLabelList);
        mRecordVp.setAdapter(mRecordLabelAdapter);
        mRecordVp.setOffscreenPageLimit(0);
        /*mTabLy.setTabsFromPagerAdapter(mRecordLabelAdapter);
        mRecordVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLy));*/
        mTabLy.setupWithViewPager(mRecordVp);
        mDetailFragment = new RecordDetailFragment();
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() >0){
            switch (mCurrentFragment){
                case DETAIL:
                    if(mDetailFragment!=null){
                        mDetailFragment.saveOrUpdateRecordToDb();
                    }
                    getFragmentManager().popBackStack();
                    mCurrentFragment = MAIN;
                    //mDetailContentLy.setVisibility(View.GONE);
                    mDetailFragment = null;//设置为NULL，让下一次进入界面的时候重新渲染
                    break;
                default:
                    getFragmentManager().popBackStack();
                    break;
            }
        }else{
            super.onBackPressed();
        }
    }


    @Override
    protected void setOnClick(){
        mLoginLl.setOnClickListener(this);
        mRecordView.setOnClickListener(this);
        mRecycleView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mFiledView.setOnClickListener(this);
        mAddListRecordFab.setOnClickListener(this);
        mAddNormalRecordFab.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCurrentFragment == MAIN){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            MenuItem login=menu.findItem(R.id.action_login);
            MenuItem switchView = menu.findItem(R.id.action_switch_view);
            AVUser avUser=LoginHelper.getCurrentUser();

            if(avUser!=null){
                login.setTitle(getResources().getString(R.string.loginOut));
            }else{
                login.setTitle(getResources().getString(R.string.action_login));
            }

            if(isSingleView()){
                switchView.setTitle(getResources().getString(R.string.many_view));
            }else {
                switchView.setTitle(getResources().getString(R.string.single_view));
            }
        }else if(mCurrentFragment == DETAIL){
            getMenuInflater().inflate(R.menu.menu_add_record,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        String eventType = notifyInfo.getEventType();
        if(eventType.equals(EventType.EVENT_LOGIN)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(LoginHelper.getCurrentUser().getUsername());
            queryLabeles();
        }else if(eventType.equals(EventType.EVENT_LOGINOUT)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(getResources().getString(R.string.no_login));
            queryLabeles();
        }else if(eventType.equals(EventType.EVENT_CHANGE_LABEL)){
            queryLabeles();//标签发生改变，刷新标签记录
        }else if(eventType.equals(EventType.EVENT_CHANGE_MAIN_MENU)){
            mMenuMode = notifyInfo.getBundleString(RecordByLabelFragment.KEY_CHANGE_MENU);
            supportInvalidateOptionsMenu();
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_LOGIN,
                EventType.EVENT_LOGINOUT,
                EventType.EVENT_CHANGE_LABEL,
                EventType.EVENT_CHANGE_MAIN_MENU
        };
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mCurrentFragment==DETAIL && mDetailFragment!=null){
            return setDetailMenuSelect(item);
        }else{
           return setMainMenuSelect(item);
        }
    }

    /**
     * 详情页面的toolbar菜单跳转
     * @param item
     * @return
     */
    private boolean setDetailMenuSelect(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_add_label:
                mDetailFragment.goAddLabelActivity();
                break;
            case R.id.action_share_content:
                break;
            case R.id.action_delete_record:
                break;
            case R.id.action_select_style:
                mDetailFragment.showPickDialog();
                break;
        }
        return true;
    }

    /**
     * 主页的菜单跳转
     * @param item
     * @return
     */
    private boolean setMainMenuSelect(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.action_login:
                if(LoginHelper.isLogin()){
                    AVUser.logOut();//清除当前缓存的数据
                    KeepNotifyCenterHelper.getInstance().notifyLoginout();//通知注销登录信息
                    Toast.makeText(this, "注销成功！", Toast.LENGTH_LONG).show();
                }else{
                    goLogin();
                }
                break;
            case R.id.action_add:
                break;
            case R.id.action_switch_view:
                if(isSingleView()){
                    KeepPreferenceUtil.getInstance(this).setShowViewCount(MANY_VIEW);
                } else {
                    KeepPreferenceUtil.getInstance(this).setShowViewCount(SINGLE_VIEW);
                }
                supportInvalidateOptionsMenu();
                KeepNotifyCenterHelper.getInstance().notifySwitchView();
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
            case R.id.add_normal_record_fab:
                goAddRecordFragment(StatusMode.ADD_RECORD_MODE, RecordModel.RECORD_TYPE_NORMAL);
                break;
            case R.id.add_list_record_fab:
                goAddRecordFragment(StatusMode.ADD_RECORD_MODE, RecordModel.RECORD_TYPE_LIST);
                break;
            case R.id.login_ll:
                if(!LoginHelper.isLogin()){
                    goLogin();
                }
                break;
            default:
                break;
        }
    }

    /**
     *  去登录
     */
    private void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }

    /**
     * 跳转到添加记录界面
     * @param recordMode 设置当前增加记事的模式：添加或者编辑
     * @param recordType 设置当前记事的类型，如清单列表型、普通模式
     */
    private void goAddRecord(String recordMode, int recordType){
        Intent addRecord=new Intent(this,AddRecordActivity.class);
        addRecord.putExtra(AddRecordActivity.KEY_RECORD_MODE, recordMode);
        addRecord.putExtra(AddRecordActivity.KEY_ADD_RECORD_TYPE, recordType);
        startActivity(addRecord);
    }

    private void goAddRecordFragment(String recordMode, int recordType){
        mCurrentFragment = DETAIL;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out);
        if(mDetailFragment == null){
            mDetailFragment = new RecordDetailFragment();
        }
        mDetailContentLy.setVisibility(View.VISIBLE);
        Bundle bundle= new Bundle();
        bundle.putString(RecordDetailFragment.KEY_RECORD_MODE, recordMode);
        bundle.putInt(RecordDetailFragment.KEY_ADD_RECORD_TYPE, recordType);
        mDetailFragment.setArguments(bundle);
        ft.replace(R.id.detail_ll, mDetailFragment, "mDetailFragment");
        ft.addToBackStack(null);
        ft.commit();
        //setToolBarTitle("添加记事");
        supportInvalidateOptionsMenu();
    }

    public void goEditRecordFragment(RecordModel recordModel,View view){
        mCurrentFragment = DETAIL;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_scale_enter, R.anim.fragment_scale_exit, R.anim.fragment_scale_enter, R.anim.fragment_scale_exit);
        if(mDetailFragment == null){
            mDetailFragment = new RecordDetailFragment();
        }
        mDetailContentLy.setVisibility(View.VISIBLE);
        Bundle bundle= new Bundle();
        bundle.putString(RecordDetailFragment.KEY_RECORD_MODE, StatusMode.EDIT_RECORD_MODE);
        bundle.putParcelable(RecordDetailFragment.KEY_EDIT_RECORD_LIST, recordModel);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        bundle.putParcelable(RecordDetailFragment.KEY_PIVOT_XY,new ViewPivot(location[0]+(view.getWidth()/2),location[1]+(view.getHeight()/2)));
        mDetailFragment.setArguments(bundle);
        ft.replace(R.id.detail_ll, mDetailFragment, "mDetailFragment");
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * 查询当前用户的所有标签
     */
    private void queryLabeles(){
        new QueryByUserDbTask(mContext, mLc,false) {
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null){
                    mLabelList =(List<LabelModel>) models;
                    mLabelList.add(0,new LabelModel(getString(R.string.all_label),LoginHelper.getCurrentUserId()));
                    mRecordLabelAdapter.refreshList(mLabelList);
                    mTabLy.setTabsFromPagerAdapter(mRecordLabelAdapter);
                }
            }
        }.execute(LoginHelper.getCurrentUserId());
    }


    private boolean isSingleView(){
        if(KeepPreferenceUtil.getInstance(this).getShowViewCount()==SINGLE_VIEW){
            return true;
        }
        return false;
    }
}
