package akiyama.mykeep.ui.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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

import com.akiyama.data.utils.LoginHelper;
import com.avos.avoscloud.AVUser;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.RecordByLabelAdapter;
import akiyama.mykeep.base.BaseObserverActivity;
import com.akiyama.base.common.StatusMode;
import com.akiyama.data.dbservice.LabelController;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelModel;
import com.akiyama.data.db.model.RecordModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;

import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.ui.LoginRegActivity;
import akiyama.mykeep.ui.RecordDetailFragment;

import com.akiyama.base.utils.Preconditions;
import akiyama.mykeep.util.SvgHelper;
import akiyama.mykeep.vo.ViewPivot;


public class MainActivity extends BaseObserverActivity
        implements View.OnClickListener, MainContract.MainView {
    private static final String TAG="MainActivity";
    /**
     * 当前页面在主页的某个fragment中
     */
    private static final int MAIN = 0;
    /**
     * 在记事详情页面中，或者编辑或者添加
     */
    private static final int DETAIL = 1;
    /**
     * 长按编辑某条记事本的时候
     */
    public static final int LONG_EDIT_RECORD = 2;



    private MainContract.Presenter mPresenter;
    private MainPresenter mMainPresenter;
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
    private TextView mAddNormalRecordTv;//增加普通计事
    private ImageView mAddListRecordIv;//添加列表数据
    private ImageView mAddVoiceIv;
    private ImageView mAddPhoneIv;

    private ViewPager mRecordVp;
    private TabLayout mTabLy;
    private RecordByLabelAdapter mRecordLabelAdapter;
    private List<LabelModel> mLabelList;
    private LabelController mLc = new LabelController();


    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;
    private int mCurrentFragment;

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化Presenter
        mMainPresenter = new MainPresenter(this);
        mPresenter.start();
        Fresco.initialize(mContext);
        setContentView(R.layout.activity_main);
        setDrawer();
        setUserInfo();
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
        mAddNormalRecordTv = (TextView) findViewById(R.id.add_normal_tv);
        mAddListRecordIv = (ImageView) findViewById(R.id.add_list_iv);
        mAddVoiceIv = (ImageView) findViewById(R.id.add_voice_iv);
        mAddPhoneIv = (ImageView) findViewById(R.id.add_phone_iv);
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
        //添加列表菜单
        SvgHelper.setImageDrawable(mAddListRecordIv,R.raw.ic_format_list_bulleted_48px);
        //添加语音记事
        SvgHelper.setImageDrawable(mAddVoiceIv,R.raw.ic_mic_none_48px);
        //添加相片记事
        SvgHelper.setImageDrawable(mAddPhoneIv,R.raw.ic_photo_camera_48px);
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
        mTabLy.setupWithViewPager(mRecordVp);
        mDetailFragment = new RecordDetailFragment();
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
    }

    @Override
    protected void setOnClick(){
        mLoginLl.setOnClickListener(this);
        mRecordView.setOnClickListener(this);
        mRecycleView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mFiledView.setOnClickListener(this);
        mAddNormalRecordTv.setOnClickListener(this);
        mAddListRecordIv.setOnClickListener(this);
    }

    private void refreshToolBar(int currentFragment){
        mCurrentFragment = currentFragment;
        invalidateOptionsMenu();
    }


    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() >0){
            setBackStackPressed();
        }else{
            setNormalPressed();
        }
    }

    /**
     * 设置有回推fragment的返回按钮响应事件
     */
    private void setBackStackPressed(){
        switch (mCurrentFragment){
            case DETAIL:
                if(mDetailFragment!=null){
                    mDetailFragment.saveOrUpdateRecordToDb();
                }
                getFragmentManager().popBackStack();
                mDetailFragment = null;//设置为NULL，让下一次进入界面的时候重新渲染
                setStatusBarView(getResources().getColor(R.color.main_bg));
                hideKeyBoard();
                bindMainToolBar();
                break;
            default:
                getFragmentManager().popBackStack();
                break;
        }
    }

    /**
     * 无fragment回退栈情况下
     */
    private void setNormalPressed(){
        if(mCurrentFragment==LONG_EDIT_RECORD){
            refreshToolBar(MAIN);//如果当前界面是长按状态的话，按返回键就重新刷新主菜单
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 从fragment返回可能需要重新绑定Toolbar
     */
    @Override
    public void bindMainToolBar(){
        iniToolBar();//因为在fragment中已经绑定过toolbar了，重新绑定toolbar
        setDrawer();//重新监听侧滑菜单
        refreshToolBar(MAIN);
    }

    /**
     * 长按菜单
     */
    public void refreshLongToolBar(){
        refreshToolBar(LONG_EDIT_RECORD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mCurrentFragment){
            case MAIN:
                createMainMenu(menu);
                break;
            case DETAIL:
                createDetailMenu(menu);
                break;
            case LONG_EDIT_RECORD:
                createLongEditMenu(menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mCurrentFragment==DETAIL && mDetailFragment!=null){
            return setDetailMenuSelect(item);
        }else if(mCurrentFragment==LONG_EDIT_RECORD){
            return setLongEditSelect(item);
        }else {
            return setMainMenuSelect(item);
        }
    }


    /**
     * 创建主页默认toolbar菜单
     * @param menu
     */
    @Override
    public void createMainMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem login=menu.findItem(R.id.action_login);
        MenuItem switchView = menu.findItem(R.id.action_switch_view);
        AVUser avUser=LoginHelper.getCurrentUser();
        if(avUser!=null){
            login.setTitle(getResources().getString(R.string.loginOut));
        }else{
            login.setTitle(getResources().getString(R.string.action_login));
        }
        if(mPresenter.isSingleView()){
            switchView.setTitle(getResources().getString(R.string.many_view));
        }else {
            switchView.setTitle(getResources().getString(R.string.single_view));
        }
    }

    /**
     * 详情页面的toolbar菜单
     * @param menu
     */
    @Override
    public void createDetailMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_add_record,menu);
    }

    /**
     * 长按编辑记事项菜单
     * @param menu
     */
    @Override
    public void createLongEditMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_long_click_edit,menu);
    }


    /**
     * 详情页面的toolbar菜单跳转
     * @param item
     * @return
     */
    @Override
    public boolean setDetailMenuSelect(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_add_label:
                mDetailFragment.goAddLabelActivity();
                break;
            case R.id.action_share_content:
                break;
            case R.id.action_delete_record:
                break;
            case R.id.action_camera:
                mDetailFragment.showPhoneDialog();
                break;
            case R.id.action_select_style:
                mDetailFragment.showPriorityDialog();
                break;
        }
        return true;
    }

    @Override
    public boolean setLongEditSelect(MenuItem item){
        int id=item.getItemId();
        switch (id){
            case R.id.action_main_delete:
                 mRecordLabelAdapter.getCurrentFragment().deleteRecord();
                break;
            case R.id.action_share_content:
                break;
            case R.id.action_delete_record:
                break;
            case R.id.action_select_style:
                mDetailFragment.showPriorityDialog();
                break;
        }
        return true;
    }

    /**
     * 主页的菜单跳转
     * @param item
     * @return
     */
    @Override
    public boolean setMainMenuSelect(MenuItem item){
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
            case R.id.action_switch_view:
                mPresenter.switchShowViewModel();
                supportInvalidateOptionsMenu();
                KeepNotifyCenterHelper.getInstance().notifySwitchView();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        String eventType = notifyInfo.getEventType();
        if(eventType.equals(EventType.EVENT_LOGIN)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(LoginHelper.getCurrentUser().getUsername());
            mPresenter.queryAllLabel();
        }else if(eventType.equals(EventType.EVENT_LOGINOUT)){
            supportInvalidateOptionsMenu();
            mUserNameTv.setText(getResources().getString(R.string.no_login));
            mPresenter.queryAllLabel();
        }else if(eventType.equals(EventType.EVENT_CHANGE_LABEL)){
            mPresenter.queryAllLabel();//标签发生改变，刷新标签记录
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
            case R.id.add_normal_tv:
                goAddRecordFragment(StatusMode.ADD_RECORD_MODE, RecordModel.RECORD_TYPE_NORMAL);
                break;
            case R.id.add_list_iv:
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
    @Override
    public void goLogin(){
        Intent login=new Intent(this,LoginRegActivity.class);
        startActivity(login);
    }

    @Override
    public void refreshUi(List<? extends BaseModel> models) {
        if(models!=null){
            mLabelList =(List<LabelModel>) models;
            mLabelList.add(0,new LabelModel(getString(R.string.all_label), LoginHelper.getCurrentUserId()));
            mRecordLabelAdapter.refreshList(mLabelList);
            if(mLabelList.size()==1){
                mTabLy.setVisibility(View.GONE);
            }else {
                mTabLy.setVisibility(View.VISIBLE);
            }
            mTabLy.setTabsFromPagerAdapter(mRecordLabelAdapter);
        }
    }

    @Override
    public void goAddRecordFragment(String recordMode, int recordType){
        mCurrentFragment = DETAIL;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_bottom_enter, R.anim.fragment_bottom_exit,R.anim.fragment_bottom_enter, R.anim.fragment_bottom_exit);
        if(mDetailFragment == null){
            mDetailFragment = new RecordDetailFragment();
        }
        mDetailContentLy.setVisibility(View.VISIBLE);
        Bundle bundle= new Bundle();
        bundle.putString(RecordDetailFragment.KEY_RECORD_MODE, recordMode);
        bundle.putInt(RecordDetailFragment.KEY_ADD_RECORD_TYPE, recordType);
        if(mDetailFragment.getArguments()==null){
            mDetailFragment.setArguments(bundle);
            ft.replace(R.id.detail_ll, mDetailFragment, "mDetailFragment");
            ft.addToBackStack(null);
            ft.commit();
            supportInvalidateOptionsMenu();
        }

    }

    @Override
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
        if(mDetailFragment.getArguments()==null){
            mDetailFragment.setArguments(bundle);
            ft.replace(R.id.detail_ll, mDetailFragment, "mDetailFragment");
            ft.addToBackStack(null);
            ft.commit();
        }

    }

}
