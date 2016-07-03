package com.akiyama.data.dbservice;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import com.akiyama.data.db.model.BaseColumns;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.LabelCoumnls;
import com.akiyama.data.db.model.LabelModel;

/**
 * 标签访问数据库
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:33
 */
public class LabelController extends BaseController implements ILabelController {

    private static final String TAG="LabelController";

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

    @Override
    public boolean deleteLabelByName(Context context, String labelName) {
        int row=context.getContentResolver().delete(LabelCoumnls.CONTENT_URI," name= '"+labelName+"'",null);
        if(row>0){
            return true;
        }
        return false;
    }
}
