package akiyama.mykeep.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;


import akiyama.mykeep.R;
import akiyama.mykeep.view.swipemenulistview.SwipeMenu;
import akiyama.mykeep.view.swipemenulistview.SwipeMenuCreator;
import akiyama.mykeep.view.swipemenulistview.SwipeMenuListView;

/**
 * 搜索标签Layout
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-29  14:16
 */
public class SearchLayout extends LinearLayout{
    private static final String TAG="SearchView";
    private Context mContext;
    private EditText mEditTextEt;
    private SwipeMenuListView mListViewLv;
    private View mView;
    private ListAdapter mAdpter=null;
    private LinearLayout mCreatLl;
    private TextView mCreatLabelTitleTv;
    private CreatLabelClickEvent mCreatLabelClickEvent;
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
        mListViewLv = (SwipeMenuListView) mView.findViewById(R.id.search_result_lv);
        mCreatLl =(LinearLayout) mView.findViewById(R.id.add_label_vs);
        mCreatLabelTitleTv = (TextView) mView.findViewById(R.id.creat_label_tv);
        if(mAdpter!=null){
            mListViewLv.setAdapter(mAdpter);
        }

        mCreatLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCreatLabelClickEvent!=null){
                    mCreatLabelClickEvent.setCreatLabelClickEvent();//绑定mCreatLayout事件
                }
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                mCreatLabelClickEvent.setLabelContextMenu(menu);
            }
        };
        mListViewLv.setMenuCreator(creator);
        mListViewLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                return mCreatLabelClickEvent.setLabelContextMenuClick(position,menu,index);
            }
        });

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

    /**
     * 设置显示的创建Label标签功能，并显示搜索出来的内容
     * @param labelContet
     */
    public void setShowCreatLayout(String labelContet){
        if(mCreatLl.getVisibility()==GONE){
            mCreatLl.setVisibility(VISIBLE);
        }
        if(!TextUtils.isEmpty(labelContet)){
            mCreatLabelTitleTv.setText(labelContet);
        }
    }

    public void setHideCreatLayout(){
        if(mCreatLl.getVisibility()==VISIBLE){
            mCreatLl.setVisibility(GONE);
        }
    }

    public String getSearchText(){
        return mEditTextEt.getText().toString();
    }

    public void setSearchText(CharSequence text){
        mEditTextEt.setText(text);
    }

    public void setCreatLabelClickEvent(CreatLabelClickEvent creatLabelClickEvent) {
        this.mCreatLabelClickEvent = creatLabelClickEvent;
    }

    /**
     * 一些item的事件接口
     */
    public interface CreatLabelClickEvent{
        public void setCreatLabelClickEvent();//单击事件
        public void setLabelContextMenu(SwipeMenu menu);//定义滑动快捷菜单项
        public boolean setLabelContextMenuClick(int position, SwipeMenu menu, int index);//滑动快捷菜单项的各个事件方法
    }


}
