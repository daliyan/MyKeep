package akiyama.mykeep.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import java.util.List;

import akiyama.mykeep.AppContext;
import akiyama.mykeep.common.Constants;
import akiyama.mykeep.db.model.BaseColumns;
import akiyama.mykeep.db.model.ImageColumns;
import akiyama.mykeep.db.model.LabelCoumnls;
import akiyama.mykeep.db.model.RecordColumns;
import akiyama.mykeep.db.model.UserColumns;
import akiyama.mykeep.util.LogUtil;

/**
 * 提供给内部和外部的程序调用数据
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-07  16:09
 */
public class DataProvider extends ContentProvider implements IDataProvider{

    private static final String TAG="DataProvider";
    private SQLiteHelper dbHelper;

    private static final UriMatcher sUriMatcher;

    public static final int USERS=1;//查询全部ID的用户
    public static final int USER_ID=2;//根据ID来查询单个用户

    public static final int RECORDS=3;
    public static final int RECORD_ID=4;

    public static final int IMAGES=5;
    public static final int IMAGE_ID=6;

    public static final int LABELS=7;//查询全部的标签
    public static final int LABEL_ID =8;

    static{
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Constants.AUTHORITY, UserColumns.TABLE_NAME,USERS);
        sUriMatcher.addURI(Constants.AUTHORITY,UserColumns.TABLE_NAME+"/id/*",USER_ID);

        sUriMatcher.addURI(Constants.AUTHORITY,RecordColumns.TABLE_NAME,RECORDS);
        sUriMatcher.addURI(Constants.AUTHORITY,RecordColumns.TABLE_NAME+"/id/*",RECORD_ID);

        sUriMatcher.addURI(Constants.AUTHORITY,RecordColumns.TABLE_NAME,IMAGES);
        sUriMatcher.addURI(Constants.AUTHORITY,RecordColumns.TABLE_NAME+"/id/*",IMAGE_ID);

        sUriMatcher.addURI(Constants.AUTHORITY,LabelCoumnls.TABLE_NAME,LABELS);
        sUriMatcher.addURI(Constants.AUTHORITY,LabelCoumnls.TABLE_NAME+"/id/*", LABEL_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)){
            case USERS:
            case RECORDS:
            case IMAGES:
            case LABELS:
                return queryAllList(uri, projection, selection, selectionArgs, sortOrder);
            case USER_ID:
            case RECORD_ID:
            case IMAGE_ID:
            case LABEL_ID:
                return queryItemById(uri);
            default:
                throw new IllegalArgumentException("query Unknown URI " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case USERS:
                return UserColumns.CONTENT_TYPE;
            case USER_ID:
                return UserColumns.CONTENT_ITEM_TYPE;
            case RECORDS:
                return RecordColumns.CONTENT_TYPE;
            case RECORD_ID:
                return RecordColumns.CONTENT_ITEM_TYPE;
            case IMAGES:
                return ImageColumns.CONTENT_TYPE;
            case IMAGE_ID:
                return ImageColumns.CONTENT_ITEM_TYPE;
            case LABELS:
                return LabelCoumnls.CONTENT_TYPE;
            case LABEL_ID:
                return LabelCoumnls.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("getType() Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)){
            case USERS:
            case RECORDS:
            case IMAGES:
            case LABELS:
                insertItemByUri(uri, values);
                return uri;
            case USER_ID:
            case RECORD_ID:
            case IMAGE_ID:
            case LABEL_ID:
            default:
                throw new IllegalArgumentException("query Unknown URI " + uri);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numInserted = 0;
        String table = uri.getPathSegments().get(0);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                if (AppContext.DEBUG) {
                    Log.d(TAG, "bulkInsert() " + value);
                }
                long id = db.insert(table, null, value);
                if (id > 0) {
                    ++numInserted;
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case USERS:
            case RECORDS:
            case IMAGES:
            case LABELS:
                return deleteItemByCondition(uri,selection, selectionArgs);
            case USER_ID:
            case RECORD_ID:
            case IMAGE_ID:
            case LABEL_ID:
                return deleteItemByUri(uri);
            default:
                throw new IllegalArgumentException("query Unknown URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)){
            case USERS:
            case RECORDS:
            case IMAGES:
            case LABELS:
                return updateItemByCondition(uri,values,selection,selectionArgs);
            case USER_ID:
            case RECORD_ID:
            case IMAGE_ID:
            case LABEL_ID:
                return updateItmeByUri(uri,values);
            default:
                throw new IllegalArgumentException("query Unknown URI " + uri);
        }
    }

    @Override
    public Cursor queryAllList(Uri uri, String[] columns, String where, String[] whereArgs, String orderBy) {
        String tableName = uri.getPathSegments().get(0);
        SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(tableName,null,where,whereArgs,null,null,orderBy);
        return queryWithNotify(uri,cursor);
    }

    @Override
    public Cursor queryItemById(Uri uri) {
        SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        String tableName=uri.getPathSegments().get(0);
        String id=uri.getPathSegments().get(2);
        String where=BaseColumns._ID+" =? ";
        String[] whereArgs= new String[]{id};
        Cursor cursor=sqLiteDatabase.query(tableName,null,where,whereArgs,null,null,null);
        return queryWithNotify(uri,cursor);
    }

    @Override
    public void insertItemByUri(Uri uri, ContentValues values) {
        if(values==null){
            LogUtil.e(TAG,"出现错误：插入的值为空。");
            return;
        }
        SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        String tableName=uri.getPathSegments().get(0);
        long rowId=sqLiteDatabase.insert(tableName,null,values);//插入的行数
        getContext().getContentResolver().notifyChange(uri, null);
        if(rowId>0){
            Uri resultUri = ContentUris.withAppendedId(uri, rowId);
            LogUtil.d(TAG,"insert resultUri"+resultUri);
        }
    }

    @Override
    public int deleteItemByCondition(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        try{
            SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
            String tableName=uri.getPathSegments().get(0);
            count=sqLiteDatabase.delete(tableName,selection,selectionArgs);//插入的行数
        }catch (SQLiteException e){
            new Exception(""+e.getMessage());
        }

        return count;
    }

    @Override
    public int deleteItemByUri(Uri uri) {
        List<String> path = uri.getPathSegments();
        String table = path.get(0);
        String id = path.get(2);
        String where = BaseColumns._ID + " =? ";
        String[] whereArgs = new String[]{id};
        return dbHelper.getWritableDatabase().delete(table, where, whereArgs);
    }

    @Override
    public int updateItemByCondition(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        List<String> path = uri.getPathSegments();
        String table = path.get(0);
        return dbHelper.getWritableDatabase().update(table, values, selection,
                selectionArgs);
    }

    @Override
    public int updateItmeByUri(Uri uri, ContentValues values) {
        List<String> path = uri.getPathSegments();
        String table = path.get(0);
        String id = path.get(2);
        return dbHelper.getWritableDatabase().update(table, values,
                BaseColumns._ID + "=?", new String[]{id});
    }


    private Cursor queryWithNotify(Uri uri, Cursor cursor) {
        if (cursor == null) {
            LogUtil.d(TAG,"cursor is null");
        } else {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }
}
