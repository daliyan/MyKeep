package akiyama.mykeep.dbservice;

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
     * 通过用户名和标签分类查询对应的记录数据
     * @param context
     * @param labelName
     * @param userId
     * @return
     */
    List<RecordModel> getRecordByUserAndLabel(Context context, String labelName, String userId);
}
