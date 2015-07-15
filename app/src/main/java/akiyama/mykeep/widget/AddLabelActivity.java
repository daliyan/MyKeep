package akiyama.mykeep.widget;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseActivity;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-15  17:22
 */
public class AddLabelActivity extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_label,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setOnClick() {

    }

    @Override
    public void onClick(View v) {

    }
}
