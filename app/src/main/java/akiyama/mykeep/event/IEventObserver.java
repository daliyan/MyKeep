package akiyama.mykeep.event;

/**
 * 抽象观察者
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:37
 */
public interface IEventObserver {
    /**
     * 根据事件进行数据或者UI的更新
     * @param eventType
     */
    public void dispatchChange(String eventType);
}
