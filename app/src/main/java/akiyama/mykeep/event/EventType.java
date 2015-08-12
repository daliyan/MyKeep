package akiyama.mykeep.event;

import java.util.HashSet;
import java.util.Set;

/**
 * 事件定义中心
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:46
 */
public class EventType {

    private static volatile EventType mEventType;
    private final static Set<String> eventsTypes = new HashSet<String>();

    public final static String EVENT_LOGIN="akiyama.mykeep.login";//登录成功
    public final static String EVENT_LOGINOUT="akiyama.mykeep.loginout";//注销登录信息
    public final static String EVENT_ADD_RECORD="akiyama.mykeep.add.record";//添加记录
    public final static String EVENT_ADD_LABEL_LIST="akiyama.mykeep.lable.list";//标签记录发生改变
    private EventType(){
        eventsTypes.add(EVENT_LOGIN);
        eventsTypes.add(EVENT_LOGINOUT);
        eventsTypes.add(EVENT_ADD_RECORD);
        eventsTypes.add(EVENT_ADD_LABEL_LIST);
    }

    public static EventType getInstance(){
        if(mEventType==null){
            mEventType=new EventType();
        }
        return mEventType;
    }

    public boolean contains(String eventType){
        return eventsTypes.contains(eventType);
    }
}
