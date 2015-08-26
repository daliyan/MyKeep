package akiyama.mykeep.event;

import android.os.Bundle;
import android.support.annotation.Nullable;

import akiyama.mykeep.event.imple.EventObserver;

/**
 * 事件主题
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:35
 */
public interface IEventSubject {
    /**
     * 注册观察者
     * @param observer
     */
    public void registerObserver(String eventType,EventObserver observer);

    /**
     * 反注册观察者
     * @param observer
     */
    public void removeObserver(String eventType,EventObserver observer);

    /**
     * 通知注册的观察者进行数据或者UI的更新
     */
    public void notifyObserver(NotifyInfo notifyInfo);
}
