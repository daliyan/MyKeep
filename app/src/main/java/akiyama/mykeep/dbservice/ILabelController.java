package akiyama.mykeep.dbservice;

import android.content.Context;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:32
 */
public interface ILabelController {

    /**
     * 通过名称删除标签
     * @param context
     * @param labelName
     */
    public boolean deleteLabelByName(Context context,String labelName);
}
