package akiyama.mykeep.ui.main;

import java.util.List;


import com.akiyama.base.AppContext;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.dbservice.LabelController;
import akiyama.mykeep.preferences.KeepPreferenceUtil;
import akiyama.mykeep.task.QueryDataByUserDbTask;

import com.akiyama.base.utils.Preconditions;
import com.akiyama.data.utils.LoginHelper;

/**
 * Created by aspen on 16/7/3.
 */
public class MainPresenter implements MainContract.Presenter{
    /**
     * 单行视图
     */
    private static final int SINGLE_VIEW=1;
    /**
     * 多行视图
     */
    private static final int MANY_VIEW=2;
    private LabelController mLc = new LabelController();
    private MainContract.MainView mMainView;

    public MainPresenter(MainContract.MainView mainView){
        mMainView = Preconditions.checkNotNull(mainView,"view is null");
        mMainView.setPresenter(this);
    }

    @Override
    public void queryAllLabel() {
        new QueryDataByUserDbTask(mLc) {
            @Override
            public void queryPostExecute(List<? extends BaseModel> models) {
                mMainView.refreshUi(models);
            }
        }.execute(LoginHelper.getCurrentUserId());
    }

    @Override
    public boolean isSingleView() {
        if(KeepPreferenceUtil.getInstance(AppContext.getInstance()).getShowViewCount()==SINGLE_VIEW){
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        queryAllLabel();
    }

    @Override
    public void switchShowViewModel() {
        if(isSingleView()){
            KeepPreferenceUtil.getInstance(AppContext.getInstance()).setShowViewCount(MANY_VIEW);
        } else {
            KeepPreferenceUtil.getInstance(AppContext.getInstance()).setShowViewCount(SINGLE_VIEW);
        }
    }
}
