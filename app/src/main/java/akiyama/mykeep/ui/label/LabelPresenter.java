package akiyama.mykeep.ui.label;

import android.net.Uri;

import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelModel;
import com.akiyama.data.repository.label.LabelRepository;
import com.akiyama.data.utils.LoginHelper;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by aspen on 16/7/9.
 */
public class LabelPresenter implements LabelContract.LabelPresenter{
    private LabelRepository mLabelRepository;
    private LabelContract.LabelView mLabelView;
    private CompositeSubscription mSubscriptions;
    public LabelPresenter(LabelContract.LabelView labelView){
        this.mLabelView = labelView;
        mSubscriptions = new CompositeSubscription();
        mLabelRepository = LabelRepository.getInstance();
        mLabelView.setPresenter(this);
    }
    @Override
    public void start() {
        queryLabel();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void queryLabel() {
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
                        mLabelView.loadLabelUi(BaseModels);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void addLabel(LabelModel labelModel) {
        Observable.just(mLabelRepository.addLabel(labelModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mLabelView.refreshUi(aBoolean);
                    }
                });
    }

    @Override
    public void deleteLabel(LabelModel labelModel) {
        Observable.just(mLabelRepository.deleteLabel(labelModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mLabelView.refreshUi(aBoolean);
                    }
                });
    }
}
