package akiyama.mykeep.event;

import akiyama.mykeep.event.imple.EventSubject;

/**
 * 通知工具类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:46
 */
public class Notify {

    private static volatile Notify mNotify;
    private Notify(){

    }

    public static Notify getInstance(){
        if(mNotify==null){
            mNotify=new Notify();
        }
        return mNotify;
    }

    public void NotifyActivity(NotifyInfo notifyInfo){
        EventSubject eventSubject= EventSubject.getInstance();
        EventType eventTypes=EventType.getInstance();
        if(eventTypes.contains(notifyInfo.getEventType())){
            eventSubject.notifyObserver(notifyInfo);
        }
    }

}
