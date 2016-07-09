package com.akiyama.data.repository.label;

import android.net.Uri;

import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelModel;

import java.util.List;

import rx.Observable;

/**
 * 标签数据缓存
 * Created by aspen on 16/7/9.
 */
public interface LabelDataSource{
    /**
     * 查询所有的标签数据,返回一个观察者数据列表
     * @param userId
     * @return
     */
    Observable<List<BaseModel>> getDbByUserId(String userId);

    boolean addLabel(LabelModel labelModel);
    boolean deleteLabel(LabelModel labelModel);
}
