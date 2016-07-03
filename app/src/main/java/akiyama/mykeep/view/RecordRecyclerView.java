package akiyama.mykeep.view;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.adapter.NoTickAdapter;
import akiyama.mykeep.adapter.TickAdapter;
import akiyama.mykeep.adapter.helper.OnStartDragListener;
import akiyama.mykeep.adapter.helper.RecyclerViewTouchCallback;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.util.StringUtil;
import akiyama.mykeep.util.SvgHelper;

/**
 * 记录清单列表View
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-08  10:44
 */
public class RecordRecyclerView extends LinearLayout implements View.OnClickListener,
        NoTickAdapter.NoTickCallback,
        TickAdapter.TickCallback,OnStartDragListener {
    private static final String TAG_NOTICK_VALUE="□ ";
    private static final String TAG_TICK_VALUE="■ ";
    private View mView;
    private Context mContext;
    private List<String> mNoTick;//未打勾的列表数据
    private List<String> mTick;//打勾的列表数据
    private RecyclerView mNoTickRlv;
    private WrapLinearLayoutManager mNoTickLayoutManager;
    private WrapLinearLayoutManager mTickLayoutManager;
    private RecyclerView mTickRlv;
    private LinearLayout mAddListLl;
    private ImageView mAddListIv;
    private TickAdapter mTickAdapter;
    private NoTickAdapter mNoTickAdapter;
    private ItemTouchHelper mItemTouchHelper;
    public RecordRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public RecordRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        setOrientation(VERTICAL);
        mNoTick = new ArrayList<>();
        mTick = new ArrayList<>();
        mView = LayoutInflater.from(context).inflate(R.layout.layout_recordlist_view, this);

        mNoTickRlv = (RecyclerView) mView.findViewById(R.id.record_list_noTick_rv);
        mTickRlv = (RecyclerView) mView.findViewById(R.id.record_list_tick_rv);

        mAddListLl = (LinearLayout) mView.findViewById(R.id.add_list_item_ll);
        mAddListIv = (ImageView)mView.findViewById(R.id.add_list_iv);
        mAddListLl.setOnClickListener(this);
        SvgHelper.setImageDrawable(mAddListIv,R.raw.ic_playlist_add_24px);

        mNoTickLayoutManager = new WrapLinearLayoutManager(mNoTickRlv);
        mTickLayoutManager = new WrapLinearLayoutManager(mContext);

        mNoTickRlv.setHasFixedSize(true);
        mNoTickRlv.setLayoutManager(mNoTickLayoutManager);
        mNoTickRlv.setItemAnimator(new DefaultItemAnimator());

        mTickRlv.setHasFixedSize(true);
        mTickRlv.setLayoutManager(mTickLayoutManager);
        mTickRlv.setItemAnimator(new DefaultItemAnimator());

        mTickAdapter = new TickAdapter(mTick);
        mTickAdapter.setTickCallback(this);
        mTickRlv.setAdapter(mTickAdapter);

        mNoTickAdapter = new NoTickAdapter(mNoTick,mContext);
        mNoTickAdapter.setNoTickCallback(this);
        mNoTickRlv.setAdapter(mNoTickAdapter);

        mNoTickAdapter.setOnStartDragListener(this);
        ItemTouchHelper.Callback noTickCallback = new RecyclerViewTouchCallback(mNoTickAdapter);
        mItemTouchHelper = new ItemTouchHelper(noTickCallback);
        mItemTouchHelper.attachToRecyclerView(mNoTickRlv);

    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.add_list_item_ll:
                mNoTickAdapter.addItem("");
                noTickInvalidate();
                if(mNoTickAdapter.getNullContentSize()>=NoTickAdapter.MAX_EMPTY_CONTENT){
                    mAddListLl.setVisibility(GONE);
                }
                break;
        }
    }


    /**
     * 根据{link getFormatText()} 规则设置对应的控件数据
     * @param content
     */
    public void setFormatText(String content){
        content = content.replace("\n","");
        content = content.replace(TAG_NOTICK_VALUE,"");
        content = content.replace(TAG_TICK_VALUE,"");
        String[] contents = StringUtil.subStringBySymbol(content, DbConfig.BIG_SPLIT_SYMBOL);
        if(content!=null){
            String[] noTicks = StringUtil.subStringBySymbol(contents[0],DbConfig.SMAIL_SPLIT_SYMBOL);
            String[] ticks = StringUtil.subStringBySymbol(contents[1],DbConfig.SMAIL_SPLIT_SYMBOL);

            if(noTicks!=null){
                for(int i=0;i<noTicks.length;i++){
                    if(!TextUtils.isEmpty(noTicks[i].trim())){
                        mNoTick.add(noTicks[i]);
                    }
                }
            }

            if(ticks!=null){
                for(int j=0;j<ticks.length;j++){
                    if(!TextUtils.isEmpty(ticks[j].trim())){
                        mTick.add(ticks[j]);
                    }
                }
            }
        }
        refreshRecyclerView();
    }

    /**
     * 刷新列表
     */
    public void refreshRecyclerView(){
        if(mNoTick.size()>0){
            mNoTickAdapter.notifyDataSetChanged();
        }

        if(mTick.size()>0){
            mTickAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取格式化后的字符数据
     * <br>格式化规则 1&2&3&4,6&7&8
     * <br>如果有一个项目为空则格式化成：2&3&4&5,
     * @return
     */
    public String getFormatText(){
        StringBuffer str=new StringBuffer("");
        List<String> noTickStrs=mNoTick;
        List<String> tickStrs=mTick;

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
            str.append(DbConfig.BIG_SPLIT_SYMBOL);
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

    public void initList(){
        mNoTickRlv.removeAllViews();
        mTickRlv.removeAllViews();
    }

    @Override
    public void onNoTickRemoveItem(int position) {
        autoRemoveFocus(position);
        mNoTickAdapter.removeItem(position);
        noTickInvalidate();

    }

    /**
     * 删除项目后自动移动焦点，如果删除项a后面有b，则焦点自动移动到b,否则焦点移动到a的前一项c
     */
    private void autoRemoveFocus(int position){
        if(position == mNoTickAdapter.getItemCount()-1){
            //移动到前一个位置
            EditText editText = (EditText) mNoTickLayoutManager.findViewByPosition(position-1).findViewById(R.id.no_tick_content_et);
            if(editText!=null){
                editText.requestFocus();
            }
        }else {
            //移动到后一个位置position+1
            EditText editText = (EditText) mNoTickLayoutManager.findViewByPosition(position+1).findViewById(R.id.no_tick_content_et);
            if(editText!=null){
                editText.requestFocus();
            }
        }
    }

    @Override
    public void onNoTickCheckItem(int position) {
        mTickAdapter.addItem(mNoTick.get(position));
        tickInvalidate();
        mNoTickAdapter.removeItem(position);
        noTickInvalidate();
    }

    /**
     * 当前列表项空内容数量
     */
    @Override
    public void onNoTickTextChanged() {
        if(mNoTickAdapter.getNullContentSize()< NoTickAdapter.MAX_EMPTY_CONTENT){
            mAddListLl.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onTickRemoveItem(int position) {
        mTickAdapter.removeItem(position);
    }

    @Override
    public void onTickCheckItem(int position) {
        mNoTickAdapter.addItem(mNoTick.get(position));
        noTickInvalidate();
        mTickAdapter.removeItem(position);
        tickInvalidate();
    }

    private void noTickInvalidate(){
        mNoTickRlv.requestLayout();
        mNoTickRlv.invalidate();
    }

    private void tickInvalidate(){
        mTickRlv.requestLayout();
        mTickRlv.invalidate();
    }

    /**
     * 开始拖动
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder, int position) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
