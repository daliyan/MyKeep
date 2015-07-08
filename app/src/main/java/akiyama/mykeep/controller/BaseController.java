package akiyama.mykeep.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.IModel;
import akiyama.mykeep.db.model.ImageColumns;
import akiyama.mykeep.db.model.RecordColumns;
import akiyama.mykeep.db.model.UserColumns;
import akiyama.mykeep.util.DataProviderHelper;

/**
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  13:54
 */
public class BaseController implements IBaseController{


    public static void clearDatabase(Context context) {
        ContentResolver cr = context.getContentResolver();
        cr.delete(UserColumns.CONTENT_URI, null, null);
        cr.delete(RecordColumns.CONTENT_URI, null, null);
        cr.delete(ImageColumns.CONTENT_URI, null, null);
    }

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
        try{
            return context.getContentResolver().insert(uri,model.values());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
