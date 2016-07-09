package com.akiyama.data.repository.label;

import android.net.Uri;

import com.akiyama.base.AppContext;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelModel;
import com.akiyama.data.dbservice.LabelController;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 标签数据仓库,做数据缓存
 * Created by aspen on 16/7/9.
 */
public class LabelRepository implements LabelDataSource{

    Map<String, BaseModel> mCachedLabels;
    boolean mCacheIsDirty = false;
    private LabelController mLabelController = new LabelController();
    private LabelRepository(){

    }
    public static class SingleInstance{
        static LabelRepository sLabelRepository = new LabelRepository();
    }
    public static LabelRepository getInstance(){
        return SingleInstance.sLabelRepository;
    }

    @Override
    public Observable<List<BaseModel>> getDbByUserId(String userId) {
        if(mCachedLabels!=null && !mCacheIsDirty){
            return Observable.from(mCachedLabels.values()).toList();
        } else if(mCachedLabels==null){
            mCachedLabels = new LinkedHashMap<>();
        }
        Observable<List<BaseModel>> observableList = mLabelController.getDbByUserId(AppContext.getInstance(),userId)
                .flatMap(new Func1<List<BaseModel>, Observable<BaseModel>>() {

                    @Override
                    public Observable<BaseModel> call(List<BaseModel> baseModels) {
                        return Observable.from(baseModels);
                    }
                }).doOnNext(new Action1<BaseModel>() {
                    @Override
                    public void call(BaseModel baseModel) {
                        mCachedLabels.put(((LabelModel)baseModel).getLabelId(),baseModel);
                    }
                }).toList();

        return Observable.concat(observableList,null).first();

    }

    @Override
    public boolean addLabel(LabelModel labelModel) {
        Uri uri= mLabelController.insert(AppContext.getInstance(),labelModel);
        if(uri!=null){
            if (mCachedLabels == null) {
                mCachedLabels = new LinkedHashMap<>();
            }
            mCachedLabels.put(labelModel.getLabelId(),labelModel);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLabel(LabelModel labelModel) {
        boolean isSuccess= mLabelController.deleteById(AppContext.getInstance(),labelModel.getId(),LabelModel.class);
        if(isSuccess){
            if (mCachedLabels == null) {
                mCachedLabels = new LinkedHashMap<>();
            }
            mCachedLabels.remove(labelModel);
        }
        return isSuccess;
    }
}
