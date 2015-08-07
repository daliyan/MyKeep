package akiyama.mykeep.controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.controller.imple.IRecordController;
import akiyama.mykeep.db.model.BaseColumns;
import akiyama.mykeep.db.model.RecordColumns;
import akiyama.mykeep.db.model.RecordModel;

/**
 * 跟View直接打交道的控制器
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  11:24
 */
public class RecordController extends BaseController implements IRecordController {

    @Override
    public List<RecordModel> getRecordsByUserId(Context context, String userId) {
        List<RecordModel> recordModels=new ArrayList<RecordModel>();
        Cursor cursor=context.getContentResolver().query(RecordColumns.CONTENT_URI,null, RecordColumns.USERID + " =? ",new String[]{userId},BaseColumns.UPDATEAT+" DESC");
        if(cursor!=null ){
            while (cursor.moveToNext()){
                RecordModel model=new RecordModel().getModel(cursor);
                recordModels.add(model);
            }
            cursor.close();
        }
        return recordModels;
    }

}
