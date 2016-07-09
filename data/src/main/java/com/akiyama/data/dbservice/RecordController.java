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

import rx.Observable;
import rx.functions.Func1;

/**
 * 获取标签数据
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  11:24
 */
public class RecordController extends BaseController implements IRecordController {

    private static final String TAG = "RecordController";
    private Func1<Cursor, BaseModel> mRecordModeFunction;

    public RecordController() {
        mRecordModeFunction = new Func1<Cursor, BaseModel>() {
            @Override
            public BaseModel call(Cursor cursor) {
                return new RecordModel().getModel(cursor);
            }
        };
    }

    @Override
    public List<RecordModel> getRecordByUserAndLabel(Context context, String userId, String labelName) {
        //List<RecordModel> recordModels = (List<RecordModel>) getDbByUserId(context, userId);
        List<RecordModel> labelRecords = new ArrayList<>();
        /*if (recordModels != null && recordModels.size() > 0) {
            for (RecordModel recordModel : recordModels) {
                String[] labelNames = StringUtil.subStringBySymbol(recordModel.getLabelNames(), DbConfig.SPLIT_SYMBOL);
                if (labelNames != null) {
                    for (String label : labelNames) {
                        if (label.equals(labelName)) {
                            labelRecords.add(recordModel);
                        }
                    }
                }
            }
        }*/
        return labelRecords;
    }

    @Override
    public boolean deleteById(Context context, String id, Class<? extends BaseModel> tClass) {
        try {
            Uri uri = DataProviderHelper.withAppendedId(tClass.newInstance().getContentUri(), id);
            int row = context.getContentResolver().delete(uri, RecordColumns.USERID + "=" + LoginHelper.getCurrentUserId(), null);
            if (row > 0) {
                return true;
            }
        } catch (InstantiationException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        }
        return false;
    }

    @Override
    public Observable<List<BaseModel>> getDbByUserId(Context context, String userId) {
        return mBriteContentResolver.createQuery(
                RecordColumns.CONTENT_URI,
                null,
                RecordColumns.USERID + " =? ",
                new String[]{userId},
                BaseColumns.CREATAT + " DESC",
                false).mapToList(mRecordModeFunction);
    }
}