package akiyama.mykeep.controller;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import akiyama.mykeep.db.model.RecordModel;

/**
 * 记录数据的接口
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  11:28
 */
public interface IRecordController {

    /**
     * 通过用户ID查询对应的“记录”
     * @param context
     * @param userId
     * @return
     */
    public List<RecordModel> getRecordsByUserId(Context context,String userId);
}
