package akiyama.mykeep.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import akiyama.mykeep.db.model.ImageColumns;
import akiyama.mykeep.db.model.LabelCoumnls;
import akiyama.mykeep.db.model.LabelModel;
import akiyama.mykeep.db.model.RecordColumns;
import akiyama.mykeep.db.model.UserColumns;

/**
 * 数据库创建帮助类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-07-03  17:03
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    public static final String TAG = "SQLiteHelper";

    public static final String DATABASE_NAME = "mykeep.db";
    public static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserColumns.CREATE_TABLE);
        db.execSQL(RecordColumns.CREATE_TABLE);
        db.execSQL(ImageColumns.CREATE_TABLE);
        db.execSQL(LabelCoumnls.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecordColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ImageColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LabelCoumnls.CREATE_TABLE);
        onCreate(db);
    }
}
