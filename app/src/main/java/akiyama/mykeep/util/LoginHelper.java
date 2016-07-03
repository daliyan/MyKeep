package akiyama.mykeep.util;

import com.avos.avoscloud.AVUser;

import com.akiyama.base.common.UserLoginConfig;

/**
 * 登录帮助类
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-30  09:45
 */
public class LoginHelper {

    /**
     * 获取当前的登录用户
     * @return
     */
    public static AVUser getCurrentUser(){
        return AVUser.getCurrentUser();
    }

    /**
     * 判断是否登录成功
     * @return
     */
    public static boolean isLogin(){
        if(getCurrentUser()!=null){
            return true;
        }
        return false;
    }

    public static String getCurrentUserId(){
        if(getCurrentUser()!=null){
            return getCurrentUser().getObjectId();
        }else{
            return UserLoginConfig.DEFAULT_USER_ID;//没有登录就返回默认用户名称
        }
    }
}
