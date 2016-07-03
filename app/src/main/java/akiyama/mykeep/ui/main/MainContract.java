package akiyama.mykeep.ui.main;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import akiyama.mykeep.BasePresenter;
import akiyama.mykeep.BaseView;
import akiyama.mykeep.db.model.BaseModel;
import akiyama.mykeep.db.model.RecordModel;

/**
 * Created by aspen on 16/7/3.
 */
public interface MainContract {
    interface MainView extends BaseView<Presenter> {
        /**
         * 创建主页下拉菜单
         * @param menu
         */
        void createMainMenu(Menu menu);

        /**
         *  处理主页菜单选项
         * @param item
         * @return
         */
        boolean setMainMenuSelect(MenuItem item);
        /**
         * 创建查看详情页面下拉菜单
         * @param menu
         */
        void createDetailMenu(Menu menu);

        /**
         * 处理详情页面下拉菜单选择项目
         * @param item
         * @return
         */
        boolean setDetailMenuSelect(MenuItem item);

        /**
         * 创建长按下拉菜单
         * @param menu
         */
        void createLongEditMenu(Menu menu);

        /**
         * 处理长按编辑菜单选项
         * @param item
         * @return
         */
        boolean setLongEditSelect(MenuItem item);
        /**
         * 绑定主页下拉菜单
         */
        void bindMainToolBar();

        /**
         * 跳到纪录详情页面
         * @param recordMode
         * @param recordType
         */
        void goAddRecordFragment(String recordMode, int recordType);

        /**
         * 编辑详情页面
         * @param recordModel
         * @param view
         */
        void goEditRecordFragment(RecordModel recordModel, View view);
        /**
         *  登录页面
         */
        void goLogin();

        /**
         * 刷新页面数据
         * @param models
         */
        void refreshUi(List<? extends BaseModel> models);
    }

    interface Presenter extends BasePresenter{
        /**
         * 查询所有存在的标签
         */
        void queryAllLabel();

        /**
         * 查询当前显示的列表模式
         * @return 返回显示的行数
         */
        boolean isSingleView();

        /**
         * 设置切换标签模式
         */
        void switchShowViewModel();
    }
}
