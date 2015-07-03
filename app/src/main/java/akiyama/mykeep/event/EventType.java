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

    public final static String EVENT_LOGIN="com.myapp.login";//登录成功
    public final static String EVENT_LOGINOUT="com.myapp.loginout";//注销登录信息
    public final static String EVENT_ADD_RECORD="com.myapp.add.record";//添加记录
    private EventType(){
        eventsTypes.add(EVENT_LOGIN);
        eventsTypes.add(EVENT_LOGINOUT);
        eventsTypes.add(EVENT_ADD_RECORD);
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
