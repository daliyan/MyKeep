package akiyama.mykeep.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.akiyama.base.AppContext;
import com.akiyama.data.dbservice.BaseController;
import com.akiyama.data.db.model.BaseModel;

/**
 * 通过用户ID查询所有数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class QueryByUserDbTask extends AsyncTask<String,Void,List<? extends BaseModel>> {

    private BaseController mBaseController;
    private Context mContext;
    private ProgressDialog mProgressBar;
    private boolean mIsShowProgressBar;
    public QueryByUserDbTask(Context context, BaseController baseController,boolean isShowProgressBar){
        this.mContext = context;
        this.mBaseController = baseController;
        this.mIsShowProgressBar = isShowProgressBar;
    }

    @Override
    protected void onPreExecute() {
        queryPreExecute();
    }

    @Override
    protected List<? extends BaseModel> doInBackground(String... params) {
        List<? extends BaseModel> models=new ArrayList<BaseModel>();
        if(params[0]!=null){
            //models=mBaseController.getDbByUserId(AppContext.getInstance(), params[0]);
        }
        return models;
    }

    @Override
    protected void onPostExecute(List<? extends BaseModel> models) {
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
