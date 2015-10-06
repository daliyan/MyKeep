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
    public final static String EVENT_REFRESH_RECORD ="akiyama.mykeep.add.record";//刷新记录信息
    public final static String EVENT_ADD_LABEL_LIST="akiyama.mykeep.lable.list";//标签记录发生改变
    public final static String EVENT_SELECTED_LABEL_LIST="akiyama.mykeep.selected.lable.list";//标签页面选择的标签
    public final static String EVENT_CHANGE_LABEL="akiyama.mykeep.change.label";//标签数据发生改变，删除、修改和添加
    public static final String EVENT_SWITCH_VIEW="akiyama.mykeep.change.switch_view";//切换主页视图，多行和单行显示
    private EventType(){
        eventsTypes.add(EVENT_LOGIN);
        eventsTypes.add(EVENT_LOGINOUT);
        eventsTypes.add(EVENT_REFRESH_RECORD);
        eventsTypes.add(EVENT_ADD_LABEL_LIST);
        eventsTypes.add(EVENT_SELECTED_LABEL_LIST);
        eventsTypes.add(EVENT_CHANGE_LABEL);
        eventsTypes.add(EVENT_SWITCH_VIEW);
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
