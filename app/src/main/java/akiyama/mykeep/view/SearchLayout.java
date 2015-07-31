package akiyama.mykeep.view;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import akiyama.mykeep.R;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  14:16
 */
public class SearchLayout extends LinearLayout{
    private static final String TAG="SearchView";
    private Context mContext;
    private EditText mEditTextEt;
    private ListView mListViewLv;
    private View mView;
    private ListAdapter mAdpter=null;
    public SearchLayout(Context context) {
        super(context);
        init(context);
    }
    public SearchLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public SearchLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.layout_search_view, this);
        mEditTextEt = (EditText) mView.findViewById(R.id.search_content_et);
        mListViewLv = (ListView) mView.findViewById(R.id.search_result_lv);
        if(mAdpter!=null){
            mListViewLv.setAdapter(mAdpter);
        }
    }

    public void setInputChangeListener(TextWatcher textWatcher){
        mEditTextEt.addTextChangedListener(textWatcher);
    }

    public void setmAdpter(ListAdapter adapter){
        this.mAdpter=adapter;
        if(mAdpter!=null){
            mListViewLv.setAdapter(mAdpter);
        }
    }
}
