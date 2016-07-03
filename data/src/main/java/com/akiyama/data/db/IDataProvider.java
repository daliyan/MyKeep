package com.akiyama.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-07  16:27
 */
public interface IDataProvider {

    /**
     * 查询所有的数据
     * @param uri
     * @param columns
     * @param where
     * @param whereArgs
     * @param orderBy
     * @return
     */
    public abstract Cursor queryAllList(Uri uri, String[] columns, String where,String[] whereArgs, String orderBy);

    /**
     * 通过ID查询数据
     * @param uri
     * @return
     */
    public abstract Cursor  queryItemById(Uri uri);

    /**
     * 通过ContentUri插入数据
     * @param uri
     * @param values
     * @return
     */
    public abstract void insertItemByUri(Uri uri,ContentValues values);

    /**
     * 根据条件删除信息
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public abstract int deleteItemByCondition(Uri uri, String selection, String[] selectionArgs);

    /**
     * 根据ContentUri删除对应的信息
     * @param uri
     * @return
     */
    public abstract int deleteItemByUri(Uri uri);

    /**
     * 跟新信息
     * @param uri
     * @return
     */
    public abstract int updateItemByCondition(Uri uri, ContentValues values, String selection, String[] selectionArgs);

    /**
     * 更新信息根据ContentUri
     * @param uri
     * @param values
     * @return
     */
    public abstract int updateItmeByUri(Uri uri, ContentValues values);
}
