package com.akiyama.data.dbservice;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import com.akiyama.base.common.DbConfig;
import com.akiyama.base.utils.LogUtil;
import com.akiyama.base.utils.StringUtil;
import com.akiyama.data.db.DataProviderHelper;
import com.akiyama.data.db.model.BaseColumns;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.RecordColumns;
import com.akiyama.data.db.model.RecordModel;
import com.akiyama.data.utils.LoginHelper;

/**
 * 跟View直接打交道的控制器
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  11:24
 */
public class RecordController extends BaseController implements IRecordController {

    private static final String TAG = "RecordController";
    @Override
    public List<? extends BaseModel> getDbByUserId(Context context, String userId) {
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

    @Override
    public List<RecordModel> getRecordByUserAndLabel(Context context, String userId, String labelName) {
        List<RecordModel> recordModels= (List<RecordModel>) getDbByUserId(context,userId);
        List<RecordModel> labelRecords=new ArrayList<>();
        if(recordModels!=null &&recordModels.size() >0){
            for(RecordModel recordModel:recordModels){
                String[] labelNames = StringUtil.subStringBySymbol(recordModel.getLabelNames(), DbConfig.SPLIT_SYMBOL);
                if(labelNames!=null){
                    for(String label:labelNames){
                        if(label.equals(labelName)){
                            labelRecords.add(recordModel);
                        }
                    }
                }
            }
        }
        return labelRecords;
    }

    @Override
    public boolean deleteById(Context context, String id, Class<? extends BaseModel> tClass) {
        try {
            Uri uri= DataProviderHelper.withAppendedId(tClass.newInstance().getContentUri(),id);
            int row=context.getContentResolver().delete(uri,RecordColumns.USERID +  "="+ LoginHelper.getCurrentUserId(),null);
            if(row>0){
                return true;
            }
        } catch (InstantiationException e) {
            LogUtil.e(TAG,"cause:"+e.getCause());
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        }
        return false;
    }
}
