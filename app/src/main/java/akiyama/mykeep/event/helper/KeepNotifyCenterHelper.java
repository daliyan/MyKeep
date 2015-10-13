package akiyama.mykeep.event.helper;

import android.os.Bundle;

import akiyama.mykeep.event.EventType;
import akiyama.mykeep.event.NotifyInfo;

/**
 * 事件通知中心，所有的事件调用该处方法作为统一入口，便于管理
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-09-01  15:18
 */
public class KeepNotifyCenterHelper {

    private static volatile KeepNotifyCenterHelper mNotifyCenter;
    private KeepNotifyCenterHelper(){

    }

    public static KeepNotifyCenterHelper getInstance(){
        if(mNotifyCenter==null){
            mNotifyCenter=new KeepNotifyCenterHelper();
        }
        return mNotifyCenter;
    }

    private void notifyObserver(NotifyInfo notifyInfo){
        if(notifyInfo!=null){
            Notify.getInstance().NotifyActivity(notifyInfo);
        }
    }

    /**
     * 标签选择状态发生改变，这个是在标签选择页面发出
     */
    public void notifyLabelStatusChange(){
        notifyObserver(new NotifyInfo(EventType.EVENT_ADD_LABEL_LIST));
    }

    /**
     * 传递标签页面选择的信息
     * @param bundle
     */
    public void notifyLabelSelect(Bundle bundle){
        notifyObserver(new NotifyInfo(EventType.EVENT_SELECTED_LABEL_LIST,bundle));
    }

    /**
     * 刷新记录信息
     */
    public void notifyRefreshRecord(Bundle bundle){
        notifyObserver(new NotifyInfo(EventType.EVENT_REFRESH_RECORD,bundle));
    }

    /**
     * 登录成功
     */
    public void notifyLoginSuccess(){
        notifyObserver(new NotifyInfo(EventType.EVENT_LOGIN));
    }

    /**
     * 登出成功
     */
    public void notifyLoginout(){
        notifyObserver(new NotifyInfo(EventType.EVENT_LOGINOUT));
    }

    /**
     * 标签记录发生改变
     */
    public void notifyLabelChange(){
        notifyObserver(new NotifyInfo(EventType.EVENT_CHANGE_LABEL));
    }

    /**
     * 通知到主页切换到actionbar item
     */
    public void notifySwitchMenu(Bundle bundle) {
        notifyObserver(new NotifyInfo(EventType.EVENT_CHANGE_MAIN_MENU, bundle));
    }

    public void notifySwitchView(){
        notifyObserver(new NotifyInfo(EventType.EVENT_SWITCH_VIEW));
    }
}
