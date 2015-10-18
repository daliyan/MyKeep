package akiyama.mykeep.widget;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import akiyama.mykeep.R;
import akiyama.mykeep.common.DbConfig;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.event.helper.KeepNotifyCenterHelper;
import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.task.QueryByUserDbTask;
import akiyama.mykeep.task.SaveSingleDbTask;
import akiyama.mykeep.adapter.SearchAdapter;
import akiyama.mykeep.base.BaseObserverActivity;
import akiyama.mykeep.controller.LabelController;
import akiyama.mykeep.db.model.LabelModel;
import akiyama.mykeep.event.EventType;
import akiyama.mykeep.util.LogUtil;
import akiyama.mykeep.util.LoginHelper;
import akiyama.mykeep.util.StringUtil;
import akiyama.mykeep.view.SearchLayout;
import akiyama.mykeep.vo.SearchVo;

/**
 * 添加和搜索标签功能
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-15  17:22
 */
public class AddLabelActivity extends BaseObserverActivity implements SearchLayout.CreatLabelClickEvent,TextWatcher{
    //选定的Label标签，从上一个界面传递过来的,一般在编辑记录标签的时候会触发
    public static final String KEY_EXTRA_SELECT_LABEL="extra_select_label";
    //传递选择后的结果给其它界面
    public static final String KEY_EXTRA_SELECTED_LABEL="extra_selected_label";
    private SearchLayout mSearchSly;
    private SearchAdapter mSearchAdapter;
    private ArrayList<SearchVo> mSearchList;
    private ArrayList<SearchVo> mSelectLabels;//已经选定的Label标签
    private String mLabels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_label);
    }

    @Override
    public void initSvgView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_label,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void findView() {
        mSearchSly = (SearchLayout) findViewById(R.id.search_sly);
    }

    @Override
    protected void initView() {
        setToolBarTitle("增加标签");
        mSearchList = new ArrayList<>();
        mSelectLabels =new ArrayList<>();
        initSelectLabel();
        queryLabelFromDb();
        mSearchAdapter =new SearchAdapter(mContext,mSearchList);
    }

    @Override
    protected void setOnClick() {
        mSearchSly.setCreatLabelClickEvent(this);
        mSearchSly.setInputChangeListener(this);
        mSearchSly.setAdpter(mSearchAdapter);
    }

    @Override
    protected void setBackEvent() {
        notifySelectedLabels();
        super.setBackEvent();
    }

    private void notifySelectedLabels(){
        ArrayList<SearchVo> searchVos=new ArrayList<SearchVo>();
        for(SearchVo searchVo:mSearchList){
            if(searchVo.getIsCheck()){
                searchVos.add(searchVo);
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_EXTRA_SELECTED_LABEL,searchVos);
        KeepNotifyCenterHelper.getInstance().notifyLabelSelect(bundle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onChange(NotifyInfo notifyInfo) {
        if(notifyInfo.getEventType().equals(EventType.EVENT_ADD_LABEL_LIST)){
            mSearchList= mSearchAdapter.getFinalSearchDate();
        }
    }

    @Override
    protected String[] getObserverEventType() {
        return new String[]{
                EventType.EVENT_ADD_LABEL_LIST,
        };
    }

    @Override
    public void setCreatLabelClickEvent() {
        saveLabelToDb();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSearchSly.setHideCreatLayout();
        if(s.length()==0){
            mSearchAdapter.refreshDate(mSearchList);
        }else{
            ArrayList<SearchVo> queryList=queryList(s.toString());
            mSearchAdapter.refreshDate(queryList);
            if(queryList.size()==0 && !TextUtils.isEmpty(mSearchSly.getSearchText())){
                mSearchSly.setShowCreatLayout("创建“"+mSearchSly.getSearchText()+"”");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 保存标签操作
     */
    private void saveLabelToDb(){
        LabelController labelController=new LabelController();
        LabelModel labelModel=new LabelModel();
        labelModel.setName(mSearchSly.getSearchText().replace(" ",""));//移除所有的空格
        labelModel.setCreatTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        labelModel.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        labelModel.setUserId(LoginHelper.getCurrentUserId());
        new SaveSingleDbTask(mContext,labelController,false){
            @Override
            public void savePostExecute(Boolean aBoolean) {
                if(aBoolean){
                    mSearchList.add(new SearchVo(mSearchSly.getSearchText(),true));//保存成功后直接添加一个项目到列表中，无需重新刷新
                    mSearchSly.setSearchText("");
                    InputMethodManager imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchSly.getWindowToken(), 0);
                    KeepNotifyCenterHelper.getInstance().notifyLabelChange();
                }

            }
        }.execute(labelModel);
    }

    /**
     * 查询标签操作
     */
   private void queryLabelFromDb(){
       final LabelController labelController=new LabelController();
       new QueryByUserDbTask(mContext,labelController,false){

           @Override
           public void queryPostExecute(List<? extends BaseModel> models) {
                if(models!=null && models.size()>0){
                    List<LabelModel> labelModels=(ArrayList<LabelModel>) models;
                    if(labelModels==null){
                        return;
                    }

                    if(mSearchList!=null && mSearchList.size()>0){
                        mSearchList.clear();
                    }

                    switchListToVo(labelModels);
                    if(mSelectLabels!=null){
                        addSelectVoList();
                    }
                    mSearchAdapter.refreshDate(mSearchList);
                }
           }

       }.execute(LoginHelper.getCurrentUserId());
   }

    /**
     * 将LabelModel list数据转换成SearchVo的适配器数据
     * @param labelModelList
     */
    private void switchListToVo(List<LabelModel> labelModelList){
        for(int i=0;i<labelModelList.size();i++){
            mSearchList.add(new SearchVo(labelModelList.get(i).getName(),false));
        }
    }

    /**
     * 添加已经选定的项目
     */
    private void addSelectVoList(){
        for(int i=0;i<mSelectLabels.size();i++){
            for(int j=0;j<mSearchList.size();j++){
                if(mSearchList.get(j).getName().equals(mSelectLabels.get(i).getName())){
                    mSearchList.set(j,new SearchVo(mSelectLabels.get(i).getName(),true));
                    break;
                }
            }
        }
    }

    /**
     * 根据字符串查询对应的数据
     * @param name
     * @return
     */
    private ArrayList<SearchVo> queryList(String name){
        ArrayList<SearchVo> searchs=new ArrayList<SearchVo>();
        for(int i=0;i<mSearchList.size();i++){
            if(mSearchList.get(i).getName().contains(name)){
                searchs.add(mSearchList.get(i));
            }
        }
        return searchs;
    }

    /**
     * 初始化选择的Label
     * @return
     */
    public void initSelectLabel(){
        mLabels = getIntent().getStringExtra(KEY_EXTRA_SELECT_LABEL);
        String[] labels=StringUtil.subStringBySymbol(mLabels, DbConfig.SPLIT_SYMBOL);
        if(labels!=null && labels.length > 0){
            for(int i=0;i < labels.length;i++)
                mSelectLabels.add(new SearchVo(labels[i],false));
        }
    }

}
