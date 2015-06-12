package ssj.androiddesign.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ssj.androiddesign.AppContext;

/**
 * Activity基类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-12  09:59
 */
public class BaseActivity extends ActionBarActivity{

    protected final Activity mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.setActiveContext(this);
    }
}
