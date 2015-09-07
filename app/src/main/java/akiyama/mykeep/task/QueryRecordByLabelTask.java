package akiyama.mykeep.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import akiyama.mykeep.controller.BaseController;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.RecordModel;

/**
 * 通过用户ID和标签分类查询数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class QueryRecordByLabelTask extends AsyncTask<String,Void,List<RecordModel>> {

    private RecordController mRecordController;
    private Context mContext;
    private ProgressDialog mProgressBar;
    private boolean mIsShowProgressBar;
    public QueryRecordByLabelTask(Context context, RecordController recordController,boolean isShowProgressBar){
        this.mContext = context;
        this.mRecordController = recordController;
        this.mIsShowProgressBar = isShowProgressBar;
    }

    @Override
    protected void onPreExecute() {
        queryPreExecute();
    }

    @Override
    protected List<RecordModel> doInBackground(String... params) {
        List<RecordModel> models=new ArrayList<RecordModel>();
        if(params[0]!=null){
           models=mRecordController.getRecodrByUserAndLabel(mContext,params[0],params[1]);
        }
        return models;
    }

    @Override
    protected void onPostExecute(List<RecordModel> models) {
        queryPostExecute(models);
        if(mProgressBar!=null){
            mProgressBar.dismiss();
        }
    }

    /**
     * 保存数据前执行的操作
     */
    protected void queryPreExecute(){
        if(mIsShowProgressBar){
            mProgressBar=new ProgressDialog(mContext);
            mProgressBar.setMessage("正在查询，请稍后......");
            mProgressBar.show();
        }
    }


    /**
     * 查询数据成功后执行的操作
     */
    public abstract void queryPostExecute(List<? extends BaseModel> models);
}
