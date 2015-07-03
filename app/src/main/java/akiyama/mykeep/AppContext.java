package akiyama.mykeep;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import akiyama.mykeep.bean.Record;
import akiyama.mykeep.util.LogUtil;

/**
 *  全局Application
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-12  09:56
 */
public class AppContext extends Application{

    public final static boolean DEBUG=BuildConfig.DEBUG;
    public final static String TAG="AppContext";
    private static AppContext mInstance;
    private static int mVersionCode;
    public static String mVersionName;
    public static String mPackageName;
    public static PackageInfo mInfo;
    private static HashMap<String, WeakReference<Activity>> mContexts = new HashMap<String, WeakReference<Activity>>();

    private static List<Activity> mActivityList = new LinkedList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(Record.class);//子类化记事表
        AVOSCloud.initialize(this,"0t6l98r6429fu5z6pde2f6zn9r8ykm5itbrmuxzormpuifva",
                "1aw548nzzzhxetq0b8yxgbdjpatr9pvj8m8zttebl1z2t73l");
        init();
        initAppInfo();
    }

    private void init(){
        this.mInstance=this;
    }

    private void initAppInfo(){
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pageInfo=pm.getPackageInfo(getPackageName(),0);
            mPackageName=pageInfo.packageName;
            mVersionName=pageInfo.versionName;
            mVersionCode=pageInfo.versionCode;
            mInfo=pageInfo;
            if(DEBUG){
                LogUtil.d(TAG, "initAppInfo: versionName:" + mVersionName + " VersionCode:" + mVersionCode + " PackageName:" + mPackageName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static synchronized void setActiveContext(Activity context) {
        WeakReference<Activity> reference = new WeakReference<Activity>(context);
        mContexts.put(context.getClass().getSimpleName(), reference);
    }

    public static synchronized Activity getActiveContext(String className) {
        WeakReference<Activity> reference = mContexts.get(className);
        if (reference == null) {
            return null;
        }

        final Activity context = reference.get();

        if (context == null) {
            mContexts.remove(className);
        }
        return context;
    }


    public static void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    public static List<Activity> getmActivityList() {
        return mActivityList;
    }

    public static void removeAcitivity(Activity activity){
        if(mActivityList.contains(activity)){
            mActivityList.remove(activity);
        }
    }

    public static HashMap<String, WeakReference<Activity>> getmContexts() {
        return mContexts;
    }

    public static AppContext getInstance(){
        return mInstance;
    }

}
