package akiyama.mykeep.task;

import android.content.Context;
import android.net.Uri;

import java.util.Calendar;
import java.util.List;

import com.akiyama.data.dbservice.RecordController;
import com.akiyama.data.db.model.IModel;
import com.akiyama.data.db.model.ImageModel;

/**
 * Created by Administrator on 2015/12/27.
 */
public abstract class SaveRecordSingleDbTask extends SaveSingleDbTask{

    protected List<String> mUrls;
    public SaveRecordSingleDbTask(Context context, RecordController recordController, boolean isShowProgressBar, List<String> urls){
        super(context,recordController,isShowProgressBar);
        this.mUrls = urls;
    }

    @Override
    protected Boolean doInBackground(IModel... params) {
        Uri uri=mBaseController.insert(mContext,params[0]);
        //mBaseController.query(mContext,);
        //传入2个参数，一个是记事记录，一个是图
        if(uri!=null){
            for(int i=0;i<mUrls.size();i++){
                ImageModel imageModel = new ImageModel();
                imageModel.setRecordId("12");
                imageModel.setUrl(mUrls.get(i));
                imageModel.setCreateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                imageModel.setUpdateTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                mBaseController.insert(mContext,imageModel);
            }
            return true;
        }
        return false;
    }
}
