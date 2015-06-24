package ssj.androiddesign.widget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ssj.androiddesign.R;
import ssj.androiddesign.base.BaseActivity;
import ssj.androiddesign.bean.User;
import ssj.androiddesign.db.imple.UserImple;
import ssj.androiddesign.db.service.UserService;

/**
 * 登录和注册帐号界面
 * @author zhiwu_yan
 * @version 1.0
 * @since 2015-06-23  15:29
 */
public class LoginRegActivity extends BaseActivity implements View.OnClickListener{

    private final static String TAG="LoginRegActivity";

    private EditText userNameEt;
    private EditText passwordEt;
    private Button loginBtn;
    private TextView forgotPasswordTv;
    private UserService userService=new UserImple();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
    }

    @Override
    protected void findView() {
        userNameEt=(EditText) findViewById(R.id.username_et);
        passwordEt=(EditText) findViewById(R.id.password_et);
        loginBtn=(Button) findViewById(R.id.login_btn);
        forgotPasswordTv=(TextView)findViewById(R.id.forgot_password_tv);
    }

    @Override
    protected void initView() {
        setToolBarTitle("注册");
    }

    @Override
    protected void setOnClick() {
        loginBtn.setOnClickListener(this);
        forgotPasswordTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.login_btn:
                if(isLogin()){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(this,"用户名或密码错误！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.forgot_password_tv:
                if(isForgotPassword()){
                    Toast.makeText(this,"密码重置成功，请查看邮件重置密码！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"用户名不存在！",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean isLogin(){

        String userName=userNameEt.getText().toString();
        String password=passwordEt.getText().toString();
        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
            User user=new User();
            user.userName=userName;
            user.passWord=password;
            return userService.login(user);
        }
        return false;
    }

    private boolean isForgotPassword(){
        String userName=userNameEt.getText().toString();
        if(!TextUtils.isEmpty(userName)){
            return userService.findPassword(userService.getUserByUserName(userName).email);
        }

        return false;
    }


}
