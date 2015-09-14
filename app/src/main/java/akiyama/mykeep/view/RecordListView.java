package akiyama.mykeep.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.R;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.StringUtil;

/**
 * 记录清单列表View
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-08  10:44
 */
public class RecordListView extends LinearLayout implements View.OnClickListener{

    private static final String TAG_NOTICK_CONTENT="tag_no_tick_content";
    private static final String TAG_TICK_CONTENT="tag_tick_content";
    private static final String TAG_NOTICK_VALUE="□ ";
    private static final String TAG_TICK_VALUE="▣ ";
    private View mView;
    private Context mContext;
    private List<String> mNoTick;//未打勾的列表数据
    private List<String> mTick;//打勾的列表数据
    private LinearLayout mNoTickLl;
    private LinearLayout mTickLl;
    private LinearLayout mAddListLl;
    private InputMethodManager mImm;
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
        mImm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        contentEt.requestFocus();
        if(!mImm.isActive()){
            mImm.showSoftInput(contentEt,InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
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
    public List<String> getNoTicks(){
        mNoTick.clear();
        int child = mNoTickLl.getChildCount();
        for(int i=0;i<child;i++){
            View childView = mNoTickLl.getChildAt(i);
            String noTickRecordStr=((EditText)(childView.findViewWithTag(TAG_NOTICK_CONTENT))).getText().toString();
            if(!TextUtils.isEmpty(noTickRecordStr)){
                mNoTick.add(noTickRecordStr);
            }
        }
        return mNoTick;
    }

    /**
     * 获取打勾的数据项
     * @return
     */
    public List<String> getTicks(){
        mTick.clear();
        int child = mTickLl.getChildCount();
        for(int i=0;i<child;i++){
            View childView = mTickLl.getChildAt(i);
            String tickRecordStr=((TextView)(childView.findViewWithTag(TAG_TICK_CONTENT))).getText().toString();
            if(!TextUtils.isEmpty(tickRecordStr)){
                mTick.add(tickRecordStr);
            }
        }
        return mTick;
    }

    /**
     * 获取格式化后的字符数据
     * <br>格式化规则 1&2&3&4,6&7&8
     * <br>如果有一个项目为空则格式化成：2&3&4&5,
     * @return
     */
    public String getFormatText(){
        StringBuffer str=new StringBuffer("");
        List<String> noTickStrs=getNoTicks();
        List<String> tickStrs=getTicks();

        for(int i=0;i<noTickStrs.size();i++){
            if(i==noTickStrs.size()-1 && tickStrs.size()==0){
                str.append(TAG_NOTICK_VALUE+noTickStrs.get(i)+DbConfig.SMAIL_SPLIT_SYMBOL);
            }else if(i==noTickStrs.size()-1){
                str.append(TAG_NOTICK_VALUE+noTickStrs.get(i)+DbConfig.SMAIL_SPLIT_SYMBOL+DbConfig.BIG_SPLIT_SYMBOL+"\n");
            }else{
                str.append(TAG_NOTICK_VALUE+noTickStrs.get(i)+DbConfig.SMAIL_SPLIT_SYMBOL+"\n");
            }
        }

        if(noTickStrs.size()==0){
            str.append(" "+DbConfig.BIG_SPLIT_SYMBOL);
        }

        for(int j=0;j<tickStrs.size();j++){
            if(j==tickStrs.size()-1){
                str.append(TAG_TICK_VALUE+tickStrs.get(j)+DbConfig.SMAIL_SPLIT_SYMBOL+DbConfig.BIG_SPLIT_SYMBOL);
            }else {
                str.append(TAG_TICK_VALUE+tickStrs.get(j)+DbConfig.SMAIL_SPLIT_SYMBOL+"\n");
            }
        }

        if(tickStrs.size()==0){
            str.append(DbConfig.BIG_SPLIT_SYMBOL+" ");
        }

        return str.toString();
    }

    /**
     * 根据{link getFormatText()} 规则设置对应的控件数据
     * @param content
     */
    public void setFormatText(String content){
        content = content.replace("\n","");
        content = content.replace(TAG_NOTICK_VALUE,"");
        content = content.replace(TAG_TICK_VALUE,"");
        String[] contents = StringUtil.subStringBySymbol(content,DbConfig.BIG_SPLIT_SYMBOL);
        if(content!=null){
            String[] noTicks = StringUtil.subStringBySymbol(contents[0],DbConfig.SMAIL_SPLIT_SYMBOL);
            String[] ticks = StringUtil.subStringBySymbol(contents[1],DbConfig.SMAIL_SPLIT_SYMBOL);

            if(noTicks!=null){
                for(int i=0;i<noTicks.length;i++){
                    if(!TextUtils.isEmpty(noTicks[i].trim())){
                        addNoTickView(noTicks[i]);
                    }
                }
            }

            if(ticks!=null){
                for(int j=0;j<ticks.length;j++){
                    if(!TextUtils.isEmpty(ticks[j].trim())){
                        addTickView(ticks[j]);
                    }
                }
            }
        }


    }

    public void initList(){
        mNoTickLl.removeAllViews();
        mTickLl.removeAllViews();
    }

}
