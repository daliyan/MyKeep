package akiyama.mykeep.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import akiyama.mykeep.util.LogUtil;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-31  14:04
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    private View mLayoutView;
    private boolean mIsInitDate = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mLayoutView==null){
            mLayoutView = inflater.inflate(onSetLayoutId(),container, false);
            findView(mLayoutView);
            initView();
            setOnClick();
        }
        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!mIsInitDate){
            initDate();
            mIsInitDate = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mLayoutView.getParent()).removeView(mLayoutView);
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
}
