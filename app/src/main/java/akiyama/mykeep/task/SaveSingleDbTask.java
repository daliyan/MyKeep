package akiyama.mykeep.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import akiyama.mykeep.controller.IBaseController;
import akiyama.mykeep.db.model.IModel;

/**
 * 插入保存单条数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class SaveSingleDbTask extends AsyncTask<IModel,Void,Boolean> {

    private IBaseController mBaseController;
    private volatile Context mContext;
    private ProgressDialog mProgressBar;
    private boolean mIsShowPregressBar;
    public SaveSingleDbTask(Context context,IBaseController baseController,boolean isShowPregressBar){
        this.mContext=context;
        this.mBaseController=baseController;
        this.mIsShowPregressBar = isShowPregressBar;
    }
    @Override
    protected void onPreExecute() {
        savePreExecute();
    }

    @Override
    protected Boolean doInBackground(IModel... params) {
        if(mBaseController.insert(mContext,params[0])!=null){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        savePostExecute(aBoolean);
        if(mProgressBar!=null){
            mProgressBar.dismiss();
        }
    }

    /**
     * 保存数据前执行的操作
     */
    protected void savePreExecute(){
        if(mIsShowPregressBar){
            mProgressBar=new ProgressDialog(mContext);
            mProgressBar.setMessage("正在保存，请稍后......");
            mProgressBar.show();
        }
    }


    /**
     * 保存数据成功后执行的操作
     */
    public abstract void savePostExecute(Boolean aBoolean);
}
