package ssj.androiddesign.db.imple;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;

import java.util.List;

import ssj.androiddesign.bean.User;
import ssj.androiddesign.db.service.UserService;

/**
 *
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-24  14:12
 */
public class UserImple extends UserService{

    private boolean isSuccessLogin=false;
    private boolean isSuccessFindPassword=false;
    private User user=new User();
    @Override
    public boolean login(User user) {
        if(user.userName!=null && user.passWord!=null){
            AVUser.logInInBackground(user.userName, user.passWord, new LogInCallback() {
                public void done(AVUser user, AVException e) {
                    if(user!=null){
                        isSuccessLogin=true;
                    }else{
                        isSuccessLogin=false;
                    }
                }
            });
        }
        return isSuccessLogin;
    }

    @Override
    public boolean findPassword(String email) {
        if(!TextUtils.isEmpty(email)){
            AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                public void done(AVException e) {
                    if (e == null) {
                        isSuccessFindPassword=true;
                    } else {
                        isSuccessFindPassword=false;
                    }
                }
            });
        }
        return isSuccessFindPassword;
   }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean updateUser() {
        return false;
    }

    @Override
    public User getUserByUserName(String userName) {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("username", userName);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avUsers, AVException e) {
                if(avUsers!=null){
                    user.userName=avUsers.get(0).getUsername();
                    user.email=avUsers.get(0).getEmail();
                }
            }
        });

        return user;
    }


}
