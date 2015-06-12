package ssj.androiddesign.widget;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import ssj.androiddesign.R;
import ssj.androiddesign.adapter.RecyclerAdapter;
import ssj.androiddesign.base.BaseActivity;
import ssj.androiddesign.bean.Recommend;


public class MainActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBarCircularIndeterminate mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("akiyamay的博客");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

        initView();
        setupStatusBarView();
    }

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mProgressBar=(ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndeterminate);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(getData());
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * 设置状态栏的颜色，目前只是在4.4上面有效
     */
    private void setupStatusBarView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(getResources().getColor(R.color.main_bg));
        }
    }

    private List<Recommend> getData(){
        List<Recommend> mainRecylers=new ArrayList<Recommend>();
        for(int i=0;i<1000;i++){
            if(i%2==0){
                mainRecylers.add(new Recommend("aspen","读书或者旅行",R.drawable.test));
            }else{
                mainRecylers.add(new Recommend("yzw","高富帅",R.drawable.me));
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
}
