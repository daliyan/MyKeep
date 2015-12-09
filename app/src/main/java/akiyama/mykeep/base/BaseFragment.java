package akiyama.mykeep.base;

import android.app.Fragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import akiyama.mykeep.R;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:04
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "BaseFragment";
    protected View mLayoutView;
    private boolean mIsInitDate = false;
    protected Toolbar mFragemntToolBar;//详情页面的toolbar。因为需要toolbar的动画效果，所以可以不使用main页面的toolbar

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mLayoutView==null){
            mLayoutView = inflater.inflate(onSetLayoutId(),container, false);
            findView(mLayoutView);
            initFragmentToolBar();
            initSvgView();
            initView();
            setOnClick();
        }
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!mIsInitDate){
            mIsInitDate = true;
            initDate();
        }
    }
    /**
     * 设置fragment ToolBar标题
     */
    protected void setFragmentToolBarTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mFragemntToolBar.setTitle(title);
        }
    }

    protected void initFragmentToolBar(){
        if(mFragemntToolBar!=null){
            ((AppCompatActivity)getActivity()).setSupportActionBar(mFragemntToolBar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
            onFragmentBack();
        }
    }

    public View getLayoutView() {
        return mLayoutView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 设置布局文件
     * @return 返回布局文件资源Id
     */
    public abstract int onSetLayoutId();

    /**
     * 获取布局控件viw.findViewById(R.id.xx);
     * @param view
     */
    public abstract void findView(View view);

    /**
     * 初始化View相关参数，例如实例化对象、设置View参数等等
     */
    public abstract void initView();
    /**
     * 初始化一些数据，一般放置一些比较耗时的操作，例如读取数据库或者从服务端获取数据等等
     */
    public abstract void initDate();

    /**
     * 设置一些点击监听
     */
    public abstract void setOnClick();

    /**
     * 初始化SVG资源
     */
    public abstract void initSvgView();


    protected void onFragmentBack(){
        mFragemntToolBar.setNavigationOnClickListener(new View.OnClickListener() {
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
        getActivity().onBackPressed();
    }
}
