package akiyama.mykeep.view;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private View mCreatLayout=null;
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

    public void setShowCreatLayout(String labelContet){
        if(mCreatLayout==null){
            mCreatLayout = ((ViewStub) mView.findViewById(R.id.add_label_vs)).inflate();
        }
        TextView title=(TextView) mCreatLayout.findViewById(R.id.creat_label_tv);
        if(!TextUtils.isEmpty(labelContet)){
            title.setText(labelContet);
        }
    }

    public void setHideCreatLayout(){
        if(mCreatLayout!=null){
            mCreatLayout.setVisibility(GONE);
        }
    }

    public String getSearchText(){
        return mEditTextEt.getText().toString();
    }
}
