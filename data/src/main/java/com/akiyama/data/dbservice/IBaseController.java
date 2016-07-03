package com.akiyama.data.dbservice;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import com.akiyama.data.db.model.BaseModel;
import com.akiyama.data.db.model.IModel;

/**
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-08  13:54
 */
public interface IBaseController {
    /**
     * 批量插入数据
     * @param context
     * @param models
     * @return
     */
    public int insert(Context context,List<? extends BaseModel> models);

    /**
     * 插入单个数据
     * @param context
     * @param model
     * @return
     */
    public Uri insert(Context context,IModel model);


    /**
     * 通过ID查询对应的数据
     * @param context
     * @param id
     * @param <T>
     * @return
     */
    public <T extends BaseModel> T query(Context context, String id,Class<T> tClass);

    /**
     * 通过ID删除数据
     * @param context
     * @param id
     * @param tClass
     * @return
     */
    public boolean deleteById(Context context,String id,Class<? extends BaseModel> tClass);

    /**
     * 通过id组批量删除数据,失败返回0
     * @param context
     * @param ids
     * @param tClass
     * @return
     */
    public boolean deleteByIds(Context context,String[] ids,Class<? extends BaseModel> tClass);

    /**
     * 根据ID更新某一条“记录”数据
     * @param context
     * @param model 可以只是包含ID编号
     * @return
     */
    public boolean updateById(Context context,BaseModel model);

}
