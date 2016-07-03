package akiyama.mykeep.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.akiyama.data.dbservice.IBaseController;
import com.akiyama.data.db.model.BaseModel;

/**
 * 插入保存单条数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class UpdateSingleDbTask extends AsyncTask<BaseModel,Void,Boolean> {

    private IBaseController mBaseController;
    private Context mContext;
    private ProgressDialog mProgressBar;
    private boolean mIsShowProgressBar;
    public UpdateSingleDbTask(Context context, IBaseController baseController,boolean isShowProgressBar){
        this.mContext=context;
        this.mBaseController=baseController;
        this.mIsShowProgressBar = isShowProgressBar;
    }
    @Override
    protected void onPreExecute() {
        updatePreExecute();
    }

    @Override
    protected Boolean doInBackground(BaseModel... params) {
        if(mBaseController.updateById(mContext,params[0])){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        updatePostExecute(aBoolean);
        if(mProgressBar!=null){
            mProgressBar.dismiss();
        }
    }

    /**
     * 保存数据前执行的操作
     */
    protected void updatePreExecute(){
        if(mIsShowProgressBar){
            mProgressBar=new ProgressDialog(mContext);
            mProgressBar.setMessage("正在更新，请稍后......");
            mProgressBar.show();
        }
    }


    /**
     * 保存数据成功后执行的操作
     */
    public abstract void updatePostExecute(Boolean aBoolean);
}
