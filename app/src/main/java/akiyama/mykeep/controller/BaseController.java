package akiyama.mykeep.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.List;

import akiyama.mykeep.controller.imple.IBaseController;
import akiyama.mykeep.db.SQLiteHelper;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.IModel;
import akiyama.mykeep.util.DataProviderHelper;
import akiyama.mykeep.util.LogUtil;

/**
 * 提供基本的插入、删除、查询操作
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  13:54
 */
public abstract class BaseController implements IBaseController {

    private static final String TAG="BaseController";
    @Override
    public int insert(Context context, List<? extends BaseModel> models) {
        if(models==null && models.size()==0){
            return -1;
        }
        Uri uri=models.get(0).getContentUri();//获取Content_uri
        return context.getContentResolver().bulkInsert(uri, DataProviderHelper.getContentValuesByModels(models));
    }

    @Override
    public Uri insert(Context context, IModel model) {
        if(model==null){
            return null;
        }
        Uri uri=model.getContentUri();//获取Content_uri
        LogUtil.d(TAG,"insert"+uri);
        try{
            return context.getContentResolver().insert(uri,model.values());
        }catch (Exception e){
            LogUtil.e(TAG,"cause:"+e.getCause()+"\n"+e.getMessage());
        }
        return null;
    }

    @Override
    public <T extends BaseModel> T query(Context context, String id, Class<T> tClass) {
        try {
            T model= tClass.newInstance();
            Uri uri=DataProviderHelper.withAppendedId(model.getContentUri(),id);
            Cursor cursor=context.getContentResolver().query(uri,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                if(model!=null){
                    model=tClass.newInstance().getModel(cursor);
                    cursor.close();
                    return model;
                }
            }
        } catch (InstantiationException e) {
            LogUtil.e(TAG,"cause:"+e.getCause());
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        }

        return null;
    }

    @Override
    public boolean deleteById(Context context, String id,Class<? extends BaseModel> tClass) {
        try {
            Uri uri=DataProviderHelper.withAppendedId(tClass.newInstance().getContentUri(),id);
            int row=context.getContentResolver().delete(uri,null,null);
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

    @Override
    public boolean deleteByIds(Context context, String[] ids,Class<? extends BaseModel> tClass) {
        SQLiteDatabase db=new SQLiteHelper(context).getReadableDatabase();
        try {
            db.beginTransaction();//开启事务
            for(int i=0;i<ids.length;i++){
                Uri uri=DataProviderHelper.withAppendedId(tClass.newInstance().getContentUri(),ids[i]);
                context.getContentResolver().delete(uri, null, null);
            }
            db.setTransactionSuccessful();//事务处理成功
            return true;
        } catch (InstantiationException e) {
            LogUtil.e(TAG,"cause:"+e.getCause());
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        }finally {
            db.endTransaction();//事务结束
        }
        return false;
    }

    @Override
    public boolean updateById(Context context, String id, BaseModel model, Class<? extends BaseModel> tClass) {
        try {
            Uri uri= DataProviderHelper.withAppendedId(tClass.newInstance().getContentUri(),id);
            int row=context.getContentResolver().update(uri,model.values(),null,null);
            if(row!=0){
                return true;
            }
        } catch (InstantiationException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        } catch (IllegalAccessException e) {
            LogUtil.e(TAG, "cause:" + e.getCause());
        }
        return false;
    }

    /**
     * 通过对应的用户ID获取对应的数据
     * @return
     */
    public abstract List<? extends BaseModel> getDbByUserId(Context context, String userId);
}
