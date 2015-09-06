package akiyama.mykeep.event;

import android.os.Bundle;

/**
 * 通知信息，包括事件类型和事件携带的bundle信息
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-08-26  14:33
 */
public class NotifyInfo {
    private String eventType;
    private Bundle bundle;

    public NotifyInfo(String eventType){
        this.eventType = eventType;
    }

    public NotifyInfo(String eventType,Bundle bundle){
        this.eventType = eventType;
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBundleString(String key){
        if(bundle!=null){
            return bundle.getString(key);
        }
        return null;
    }
}
