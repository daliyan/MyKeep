package akiyama.mykeep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import akiyama.mykeep.R;

/**
 * 记录清单列表View
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-08  10:44
 */
public class RecordListView extends LinearLayout implements View.OnClickListener{

    private static final String TAG_CONTENT="tag_content";
    private View mView;
    private Context mContext;
    private Map<View,String> mNoTick;//未打勾的列表数据
    private Map<View,String> mTick;//打勾的列表数据
    private LinearLayout mNoTickLl;
    private LinearLayout mTickLl;
    private LinearLayout mAddListLl;
    public RecordListView(Context context) {
        super(context);
        init(context);
    }

    public RecordListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        mNoTick = new HashMap<>();
        mTick = new HashMap<>();
        setOrientation(VERTICAL);
        mView = LayoutInflater.from(context).inflate(R.layout.layout_recordlist_view, this);
        mNoTickLl = (LinearLayout) mView.findViewById(R.id.record_list_noTick_ll);
        mTickLl = (LinearLayout) mView.findViewById(R.id.record_list_tick_ll);
        mAddListLl = (LinearLayout) mView.findViewById(R.id.add_list_item_ll);
        mAddListLl.setOnClickListener(this);
    }

    /**
     * 添加一个新的列表数据
     */
    private void addNoTickView(String message){
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recordlist_no_tick_view, mNoTickLl, false);
        ImageView cancelIv = (ImageView)itemView.findViewById(R.id.cancel_iv);
        CheckBox checkBox =(CheckBox) itemView.findViewById(R.id.select_cb);
        EditText contentEt = (EditText) itemView.findViewById(R.id.content_message_et);
        if(message!=null){
            contentEt.setText(message);
        }
        checkBox.setChecked(false);
        checkBox.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
        //给当前添加的View设置一个TAG，便于后面删除的时候移除
        itemView.setTag(cancelIv.getId());
        checkBox.setTag(cancelIv.getId());
        contentEt.setTag(TAG_CONTENT);
        mNoTickLl.addView(itemView);
    }

    /**
     * 添加一个新的列表数据
     */
    private void addTickView(String message){
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recordlist_tick_view, mNoTickLl, false);
        ImageView cancelIv = (ImageView)itemView.findViewById(R.id.tick_cancel_iv);
        CheckBox checkBox =(CheckBox) itemView.findViewById(R.id.tick_select_cb);
        TextView contentTv = (TextView) itemView.findViewById(R.id.tick_content_tv);
        if(message!=null){
            contentTv.setText(message);
        }
        checkBox.setChecked(true);
        checkBox.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
        //给当前添加的View设置一个TAG，便于后面删除的时候移除
        itemView.setTag(cancelIv.getId());
        checkBox.setTag(cancelIv.getId());
        contentTv.setTag(TAG_CONTENT);
        mTickLl.addView(itemView);
    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.add_list_item_ll:
                addNoTickView(null);
                break;
            case R.id.cancel_iv:
                mNoTickLl.removeView(mNoTickLl.findViewWithTag(id));
                break;
            case R.id.tick_cancel_iv:
                mTickLl.removeView(mNoTickLl.findViewWithTag(id));
                break;
            case R.id.tick_select_cb:
                View tickView =mTickLl.findViewWithTag(v.getTag());
                mTickLl.removeView(tickView);
                addNoTickView(((EditText)(tickView.findViewWithTag(TAG_CONTENT))).getText().toString());
                break;
            case R.id.select_cb:
                View notickView =mNoTickLl.findViewWithTag(v.getTag());
                mNoTickLl.removeView(notickView);
                addTickView(((TextView)(notickView.findViewWithTag(TAG_CONTENT))).getText().toString());
                break;
        }
    }

}
