package akiyama.mykeep.event.imple;

import android.os.Handler;
import android.os.Looper;

import akiyama.mykeep.event.IEventObserver;
import akiyama.mykeep.event.NotifyInfo;

/**
 * 具体的观察者
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  14:38
 */
public abstract class EventObserver implements IEventObserver {

    private Handler mHandler;

    public EventObserver(){
        mHandler=new Handler(Looper.getMainLooper());
    }


    public abstract void onChange(NotifyInfo mNotifyInfo);

    @Override
    public void dispatchChange(NotifyInfo notifyInfo){
        mHandler.post(new NotificationRunnable(notifyInfo));
    }

    private final class NotificationRunnable implements Runnable{
        private NotifyInfo mNotifyInfo;
        public NotificationRunnable(NotifyInfo notifyInfo){
            this.mNotifyInfo=notifyInfo;
        }

        @Override
        public void run() {
            EventObserver.this.onChange(mNotifyInfo);
        }
    }
}
