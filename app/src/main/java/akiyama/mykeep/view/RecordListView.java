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

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.util.LogUtil;

/**
 * 记录清单列表View
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-08  10:44
 */
public class RecordListView extends LinearLayout implements View.OnClickListener{

    private static final String TAG_NOTICK_CONTENT="tag_NO_tick_content";
    private static final String TAG_TICK_CONTENT="tag_tick_content";
    private View mView;
    private Context mContext;
    private List<String> mNoTick;//未打勾的列表数据
    private List<String> mTick;//打勾的列表数据
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
        mNoTick = new ArrayList<>();
        mTick = new ArrayList<>();
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
        ImageView cancelIv = (ImageView)itemView.findViewById(R.id.no_tick_cancel_iv);
        CheckBox checkBox =(CheckBox) itemView.findViewById(R.id.no_tick_select_cb);
        EditText contentEt = (EditText) itemView.findViewById(R.id.no_tick_content_message_et);
        contentEt.setTypeface(AppContext.getRobotoSlabLight());
        if(message!=null){
            contentEt.setText(message);
        }
        checkBox.setChecked(false);
        checkBox.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
        //给当前添加的View设置一个TAG，便于后面删除的时候移除
        itemView.setTag(cancelIv.hashCode());
        checkBox.setTag(cancelIv.hashCode());
        LogUtil.d(""+cancelIv.hashCode());
        contentEt.setTag(TAG_NOTICK_CONTENT);
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
        contentTv.setTypeface(AppContext.getRobotoSlabLight());
        if(message!=null){
            contentTv.setText(message);
        }
        checkBox.setChecked(true);
        checkBox.setOnClickListener(this);
        cancelIv.setOnClickListener(this);
        //给当前添加的View设置一个TAG，便于后面删除的时候移除,使用cancelIv.hashCode()来标识唯一的一个TAG
        itemView.setTag(cancelIv.hashCode());
        checkBox.setTag(cancelIv.hashCode());
        LogUtil.d(""+cancelIv.hashCode());
        contentTv.setTag(TAG_TICK_CONTENT);
        mTickLl.addView(itemView);
    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.add_list_item_ll:
                addNoTickView(null);
                break;
            case R.id.no_tick_cancel_iv:
                mNoTickLl.removeView(mNoTickLl.findViewWithTag(v.hashCode()));
                break;
            case R.id.no_tick_select_cb:
                View notickView =mNoTickLl.findViewWithTag(v.getTag());
                LogUtil.d(""+v.getTag());
                mNoTickLl.removeView(notickView);
                addTickView(((EditText)(notickView.findViewWithTag(TAG_NOTICK_CONTENT))).getText().toString());
                break;
            case R.id.tick_cancel_iv:
                mTickLl.removeView(mTickLl.findViewWithTag(v.hashCode()));
                break;
            case R.id.tick_select_cb:
                View tickView =mTickLl.findViewWithTag(v.getTag());
                mTickLl.removeView(tickView);
                addNoTickView(((TextView)(tickView.findViewWithTag(TAG_TICK_CONTENT))).getText().toString());
                break;
        }
    }

    /**
     * 获取未打勾的数据项
     * @return
     */
    public List<String> getNoTickStrs(){
        mNoTick.clear();
        int child = mNoTickLl.getChildCount();
        for(int i=0;i<child;i++){
            View childView = mNoTickLl.getChildAt(i);
            String tickRecordStr=((EditText)(childView.findViewWithTag(TAG_NOTICK_CONTENT))).getText().toString();
            mNoTick.add(tickRecordStr);
        }
        return mNoTick;
    }

    /**
     * 获取打勾的数据项
     * @return
     */
    public List<String> getTickStrs(){
        mTick.clear();
        int child = mTickLl.getChildCount();
        for(int i=0;i<child;i++){
            View childView = mTickLl.getChildAt(i);
            String tickRecordStr=((TextView)(childView.findViewWithTag(TAG_TICK_CONTENT))).getText().toString();
            mTick.add(tickRecordStr);
        }
        return mTick;
    }

    /**
     * 获取格式化后的字符数据
     * @return
     */
    public String getFormatText(){
        StringBuffer str=new StringBuffer("");
        List<String> noTickStrs=getNoTickStrs();
        List<String> tickStrs=getTickStrs();
        for(int i=0;i<noTickStrs.size();i++){
            if(i==noTickStrs.size()-1 && tickStrs.size()==0){
                str.append("未完成："+noTickStrs.get(i).toString());
            }else{
                str.append("未完成："+noTickStrs.get(i).toString()+"\n");
            }
        }

        for(int j=0;j<tickStrs.size();j++){
            if(j==tickStrs.size()-1){
                str.append("完成："+tickStrs.get(j).toString());
            }else {
                str.append("完成："+tickStrs.get(j).toString()+"\n");
            }

        }

        return str.toString();
    }

    public void setFormatText(List<String> tickStrs,List<String> noTickStrs){
        for(int i=0;i<noTickStrs.size();i++){
            addNoTickView(noTickStrs.get(i).toString());
        }

        for(int j=0;j<tickStrs.size();j++){
            addTickView(tickStrs.get(j).toString());
        }

    }

    public void initList(){
        mNoTickLl.removeAllViews();
        mTickLl.removeAllViews();
    }

}
