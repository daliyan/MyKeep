package akiyama.mykeep;

/**
 * Created by aspen on 16/7/3.
 */
public interface BasePresenter {
    /**
     * 启动Presenter,一般在onResume生命周期内进处理
     */
    void start();

    void subscribe();

    void unsubscribe();
}
