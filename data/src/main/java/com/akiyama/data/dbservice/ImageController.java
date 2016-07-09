package com.akiyama.data.dbservice;

import android.content.Context;

import java.util.List;

import com.akiyama.data.db.model.BaseModel;

import rx.Observable;

/**
 * Created by Administrator on 2015/12/27.
 */
public class ImageController extends BaseController implements IImageController{
    /**
     * 通过对应的用户ID获取对应的数据
     *
     * @param context
     * @param userId
     * @return
     */
    @Override
    public Observable<List<BaseModel>> getDbByUserId(Context context, String userId) {
        return null;
    }
}
