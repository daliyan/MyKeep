package akiyama.mykeep.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.akiyama.base.utils.StringUtil;


/**
 * FIXME
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-10-04  17:30
 */
public class KeepPreference {

    private static final String KEEP_SHAREPREFERCE_NAME="keep_sharepreference";
    private static KeepPreference sKeepPreferce;
    private SharedPreferences sSharePreferces;
    SharedPreferences.Editor mEditor;
    private KeepPreference(Context context){
        sSharePreferces = context.getApplicationContext().getSharedPreferences(KEEP_SHAREPREFERCE_NAME,Context.MODE_PRIVATE);
        mEditor = sSharePreferces.edit();
    }

    public static KeepPreference getInstance(Context context){
        if(sKeepPreferce == null){
            synchronized (context){
                return new KeepPreference(context);
            }
        }
        return sKeepPreferce;
    }

    public void putString(String key,String value){
        if(StringUtil.notIsEmpty(key,value)){
            mEditor.putString(key,value);
            mEditor.apply();
        }
    }

    public String getString(String key,String defValue){
        if(StringUtil.notIsEmpty(key,defValue)){
           return sSharePreferces.getString(key,defValue);
        }
        return null;
    }

    public void putInt(String key,int value){
        if(StringUtil.notIsEmpty(key)){
            mEditor.putInt(key,value);
            mEditor.commit();
        }
    }

    public int getInt(String key,int defValue){
        if(StringUtil.notIsEmpty(key)){
            return sSharePreferces.getInt(key,defValue);
        }
        return defValue;
    }


}
