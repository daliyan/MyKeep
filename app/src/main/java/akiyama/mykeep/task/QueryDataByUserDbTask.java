package akiyama.mykeep.task;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import com.akiyama.base.AppContext;
import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.dbservice.BaseController;

/**
 * 通过用户ID查询所有数据的Task
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-07  10:43
 */
public abstract class QueryDataByUserDbTask extends AsyncTask<String,Void,List<? extends BaseModel>> {

    private BaseController mBaseController;
    public QueryDataByUserDbTask(BaseController baseController){
        this.mBaseController = baseController;
    }

    @Override
    protected List<? extends BaseModel> doInBackground(String... params) {
        List<? extends BaseModel> models=new ArrayList<BaseModel>();
        /*if(params[0]!=null){
            models=mBaseController.getDbByUserId(AppContext.getInstance(), params[0]);
        }*/
        return models;
    }

    @Override
    protected void onPostExecute(List<? extends BaseModel> models) {
        queryPostExecute(models);
    }

    /**
     * 查询数据成功后执行的操作
     */
    public abstract void queryPostExecute(List<? extends BaseModel> models);
}
