package akiyama.mykeep.base;

import android.os.Bundle;

import java.lang.ref.WeakReference;

import akiyama.mykeep.event.NotifyInfo;
import akiyama.mykeep.event.imple.EventObserver;
import akiyama.mykeep.event.imple.EventSubject;

/**
 * 带观察者模式的BaseActivity
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:42
 */
public abstract class BaseObserverActivity extends BaseActivity {

    private ActivityEventObserver mActivityEventObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityEventObserver=new ActivityEventObserver(this);
        registerObserver(mActivityEventObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObserver(mActivityEventObserver);
    }


    public void registerObserver(EventObserver observer) {
        final String[] observerEventTypes=getObserverEventType();//获取所有需要监听的业务类型
        if(observerEventTypes!=null && observerEventTypes.length>0){
            final EventSubject eventSubject=EventSubject.getInstance();
            for(String eventType:observerEventTypes){
                eventSubject.registerObserver(eventType,observer);
            }


        }

    }

    public void removeObserver(EventObserver observer) {
        final String[] observerEventTypes=getObserverEventType();//获取所有需要监听的业务类型
        if(observerEventTypes!=null && observerEventTypes.length>0){
            final EventSubject eventSubject=EventSubject.getInstance();
            for(String eventType:observerEventTypes){
                eventSubject.removeObserver(eventType, observer);
            }

        }
    }

    /**
     * 该方法会在具体的观察者对象中调用，可以根据事件的类型来更新对应的UI，这个方法在UI线程中被调用，
     * 所以在该方法中不能进行耗时操作，可以另外开线程
     * @param notifyInfo 事件传递信息
     */
    protected abstract void onChange(NotifyInfo notifyInfo);

    /**
     * 通过这个方法来告诉具体的观察者需要监听的业务类型
     * @return
     */
    protected abstract String[] getObserverEventType();

    private static class ActivityEventObserver extends EventObserver {
        //添加弱引用，防止对象不能被回收
        private final WeakReference<BaseObserverActivity> mActivity;
        public ActivityEventObserver(BaseObserverActivity activity){
            super();
            mActivity=new WeakReference<BaseObserverActivity>(activity);
        }

        @Override
        public void onChange(NotifyInfo notifyInfo) {
            BaseObserverActivity activity=mActivity.get();
            if(activity!=null){
                activity.onChange(notifyInfo);
            }
        }
    }
}
