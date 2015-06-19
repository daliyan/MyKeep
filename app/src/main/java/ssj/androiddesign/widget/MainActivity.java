package ssj.androiddesign.widget;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ssj.androiddesign.R;
import ssj.androiddesign.adapter.RecyclerAdapter;
import ssj.androiddesign.base.BaseActivity;
import ssj.androiddesign.bean.ChildRocommend;
import ssj.androiddesign.bean.Recommend;
import ssj.androiddesign.util.DateUtil;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBarCircularIndeterminate mProgressBar;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerDl;//侧滑菜单布局控件
    private ActionBarDrawerToggle mDrawerToggle;

    private View mRecordView;
    private View mFiledView;
    private View mRecycleView;
    private View mSettingView;

    private TextView mRecordTv;
    private TextView mFiledTv;
    private TextView mRecycleTv;
    private TextView mSettingTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
        setDrawer();
        setOnClick();
    }

    private void findView(){
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mProgressBar=(ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        mDrawerDl=(DrawerLayout)findViewById(R.id.left_menu_dl);
        mLayoutManager = new LinearLayoutManager(this);

        mRecordView= findViewById(R.id.item_menu_1);
        mRecordTv =(TextView) mRecordView.findViewById(R.id.title_tv);

        mFiledView= findViewById(R.id.item_menu_2);
        mFiledTv =(TextView) mFiledView.findViewById(R.id.title_tv);

        mRecycleView= findViewById(R.id.item_menu_3);
        mRecycleTv =(TextView) mRecycleView.findViewById(R.id.title_tv);

        mSettingView= findViewById(R.id.item_menu_4);
        mSettingTv =(TextView) mSettingView.findViewById(R.id.title_tv);

    }

    /**
     * 初始化View的基本数据
     */
    private void initView(){
        setToolBar();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
        mRecordView.setBackgroundResource(R.color.light_gray);
        mFiledView.setBackgroundResource(R.color.white);
        mRecycleView.setBackgroundResource(R.color.white);
        mSettingView.setBackgroundResource(R.color.white);

        setStatusBarView();
        setLeftMenuItem();
    }

    /**
     * 设置左侧菜单的文字和图标
     */
   private void setLeftMenuItem(){
       mRecordTv.setText(getResources().getString(R.string.item_menu_1));
       mFiledTv.setText(getResources().getString(R.string.item_menu_2));
       mRecycleTv.setText(getResources().getString(R.string.item_menu_3));
       mSettingTv.setText(getResources().getString(R.string.item_menu_4));
   }
    /**
     * 设置ToolBar
     */
    private void setToolBar(){
        mToolbar.setTitle("akiyamay的博客");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置状态栏的颜色，目前只是在4.4上面有效
     */
    private void setStatusBarView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(getResources().getColor(R.color.main_bg));
        }
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

    private void setOnClick(){
        mRecordView.setOnClickListener(this);
        mRecycleView.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mFiledView.setOnClickListener(this);
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
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
