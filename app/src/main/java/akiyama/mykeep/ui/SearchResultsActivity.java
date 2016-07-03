package akiyama.mykeep.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import akiyama.mykeep.R;
import akiyama.mykeep.base.BaseActivity;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-25  10:36
 */
public class SearchResultsActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_result);
        handleIntent(getIntent());
    }

    @Override
    public void initSvgView() {

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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }
}
