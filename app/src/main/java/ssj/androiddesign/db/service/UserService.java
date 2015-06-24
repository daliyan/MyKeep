package ssj.androiddesign.db.service;

import ssj.androiddesign.bean.User;

/**
 * 操作用户数据库的方法
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-24  13:59
 */
public abstract class UserService {

    /**
     * 登录，只需要传入用户名和密码即可
     * @param user
     * @return
     */
    public abstract boolean login(User user);

    public abstract boolean findPassword(String email);

    /**
     * 添加一个用户也就是注册一个用户
     * @param user
     * @return
     */
    public abstract boolean addUser(User user);

    /**
     * 更新用户信息
     * @return
     */
    public abstract boolean updateUser();


    public abstract User getUserByUserName(String userName);
}
