package akiyama.mykeep.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import akiyama.mykeep.controller.BaseController;
import akiyama.mykeep.controller.RecordController;
import akiyama.mykeep.db.model.RecordModel;

/**
 * 插入保存单条数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class DeleteSingleDbTask extends AsyncTask<String,Void,Boolean> {

    private BaseController mBaseController;
    private Context mContext;
    private ProgressDialog mProgressBar;
    private boolean mIsShowProgressBar;
    public DeleteSingleDbTask(Context context, BaseController baseController, boolean isShowProgressBar){
        this.mContext=context;
        this.mBaseController =baseController;
        this.mIsShowProgressBar = isShowProgressBar;
    }
    @Override
    protected void onPreExecute() {
        deletePreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(mBaseController.deleteById(mContext,params[0], RecordModel.class)){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        deletePostExecute(aBoolean);
        if(mProgressBar!=null){
            mProgressBar.dismiss();
        }
    }

    /**
     * 保存数据前执行的操作
     */
    protected void deletePreExecute(){
        if(mIsShowProgressBar){
            mProgressBar=new ProgressDialog(mContext);
            mProgressBar.setMessage("正在删除，请稍后......");
            mProgressBar.show();
        }
    }


    /**
     * 保存数据成功后执行的操作
     */
    public abstract void deletePostExecute(Boolean aBoolean);
}
