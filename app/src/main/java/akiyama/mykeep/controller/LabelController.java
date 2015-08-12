package akiyama.mykeep.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.controller.imple.ILabelController;
import akiyama.mykeep.db.model.BaseColumns;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.LabelCoumnls;
import akiyama.mykeep.db.model.LabelModel;

/**
 * 标签访问数据库
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:33
 */
public class LabelController  extends BaseController implements ILabelController {

    @Override
    public List<? extends BaseModel> getDbByUserId(Context context, String userId) {
        List<LabelModel> labelModels=new ArrayList<LabelModel>();
        Cursor cursor=context.getContentResolver().query(LabelCoumnls.CONTENT_URI,null, LabelCoumnls.USERID + " =? ",new String[]{userId}, BaseColumns.UPDATEAT+" DESC");
        if(cursor!=null ){
            while (cursor.moveToNext()){
                LabelModel model=new LabelModel().getModel(cursor);
                labelModels.add(model);
            }
            cursor.close();
        }
        return labelModels;
    }
}
