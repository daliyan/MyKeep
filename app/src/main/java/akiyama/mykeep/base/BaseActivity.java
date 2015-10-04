package akiyama.mykeep.base;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;

/**
 * Activity基类
 * <br/>.继承该BaseActivity必须在布局文件中实现Toolbar，否则会找不到mToolbar而报错
 * <br/>.ToolBar的书写方式为：<include layout="@layout/layout_toolbar"/>
 * <br/>.如果需要重新定义ToolBar上面的HomeAsUp图标只需要重写setBackEvent()事件即可
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-12  09:59
 */
public abstract class BaseActivity extends ActionBarActivity implements View.OnClickListener{

    private final static String TAG="BaseActivity";
    protected final Activity mContext=this;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.setActiveContext(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        findView();
        initView();
        setOnClick();
        setStatusBarView();
    }

    /**
     * 设置ToolBar
     */
    protected void setToolBarTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mToolbar.setTitle(title);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        setBackListener();
    }

    protected void setBackListener(){
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackEvent();
            }
        });
    }

    /**
     * 设置返回按钮的事件，默认是返回到前一个界面
     */
    protected void setBackEvent(){
        onBackPressed();
    }

    /**
     * 设置状态栏的颜色，目前只是在4.4上面有效
     */
    protected void setStatusBarView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setTintColor(getResources().getColor(R.color.main_bg));
        }
    }

    /**
     * 获取字符串资源
     * @param resId
     * @return
     */
    public String getResString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取布局控件
     */
    protected abstract void findView();

    /**
     * 初始化View的一些数据
     */
    protected abstract void initView();

    /**
     * 设置点击监听
     */
    protected abstract void setOnClick();

}
