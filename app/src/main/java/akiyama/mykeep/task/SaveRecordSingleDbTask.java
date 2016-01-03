package akiyama.mykeep.task;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import akiyama.mykeep.controller.IBaseController;
import akiyama.mykeep.db.model.IModel;
import akiyama.mykeep.db.model.ImageModel;
import akiyama.mykeep.db.model.RecordModel;

/**
 * Created by Administrator on 2015/12/27.
 */
public abstract class SaveRecordSingleDbTask extends SaveSingleDbTask{

    protected List<String> mUrls;
    public SaveRecordSingleDbTask(Context context, IBaseController baseController, boolean isShowPregressBar,List<String> urls){
        super(context,baseController,isShowPregressBar);
        this.mUrls = urls;
    }

    @Override
    protected Boolean doInBackground(IModel... params) {
        Uri uri=mBaseController.insert(mContext,params[0]);
        //传入2个参数，一个是记事记录，一个是图
        if(uri!=null){
            for(int i=0;i<mUrls.size();i++){
                ImageModel imageModel = new ImageModel();
               // mBaseController.insert(mUrls);
            }
            return true;
        }
        return false;
    }
}
