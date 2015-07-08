package akiyama.mykeep.controller;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.IModel;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  13:54
 */
public interface IBaseController {
    /**
     * 批量插入数据
     * @param context
     * @param models
     * @return
     */
    public int insert(Context context,List<? extends BaseModel> models);

    /**
     * 插入单个数据
     * @param context
     * @param model
     * @return
     */
    public Uri insert(Context context,IModel model);
}
