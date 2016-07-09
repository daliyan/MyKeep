package akiyama.mykeep.ui.main;

import android.util.Log;

import java.util.List;


import com.akiyama.base.AppContext;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.dbservice.LabelController;
import akiyama.mykeep.preferences.KeepPreferenceUtil;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import com.akiyama.base.utils.Preconditions;
import com.akiyama.data.repository.label.LabelDataSource;
import com.akiyama.data.repository.label.LabelRepository;
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
    private LabelRepository mLabelRepository;
    private MainContract.MainView mMainView;
    private CompositeSubscription mSubscriptions;
    public MainPresenter(MainContract.MainView mainView){
        mMainView = Preconditions.checkNotNull(mainView,"view is null");
        mSubscriptions = new CompositeSubscription();
        mLabelRepository = LabelRepository.getInstance();
        mMainView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void queryAllLabel() {
        mSubscriptions.clear();
        Subscription subscription = mLabelRepository.getDbByUserId(LoginHelper.getCurrentUserId())
                .flatMap(new Func1<List<BaseModel>, Observable<BaseModel>>() {
                    @Override
                    public Observable<BaseModel> call(List<BaseModel> BaseModels) {
                        return Observable.from(BaseModels);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BaseModel>>() {
                    @Override
                    public void onCompleted() {
                        //mBaseModelsView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                       // mBaseModelsView.showLoadingBaseModelsError();
                    }

                    @Override
                    public void onNext(List<BaseModel> BaseModels) {
                        mMainView.refreshUi(BaseModels);
                    }
                });
        mSubscriptions.add(subscription);
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
