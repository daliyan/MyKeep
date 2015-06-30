package ssj.androiddesign.event.imple;

import android.os.Handler;
import android.os.Looper;

import ssj.androiddesign.event.EventObserverI;

/**
 * 具体的观察者
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:38
 */
public abstract class EventObserver implements EventObserverI{

    private Handler mHandler;

    public EventObserver(){
        mHandler=new Handler(Looper.getMainLooper());
    }


    public abstract void onChange(String eventType);

    @Override
    public void dispatchChange(String eventType){
        mHandler.post(new NotificationRunnable(eventType));
    }

    private final class NotificationRunnable implements Runnable{
        private String mEventType;
        public NotificationRunnable(String eventType){
            this.mEventType=eventType;
        }
        @Override
        public void run() {
            EventObserver.this.onChange(mEventType);
        }
    }
}
