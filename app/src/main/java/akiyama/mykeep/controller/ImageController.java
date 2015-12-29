package akiyama.mykeep.controller;

import android.content.Context;

import java.util.List;

import akiyama.mykeep.db.model.BaseModel;

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
    public List<? extends BaseModel> getDbByUserId(Context context, String userId) {
        return null;
    }
}
