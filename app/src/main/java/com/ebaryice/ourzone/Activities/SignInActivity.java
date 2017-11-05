package com.ebaryice.ourzone.Activities;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;

/**
 * Created by Ebaryice on 2017/10/31.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.input_account)
    MaterialEditText input_account;

    @BindView(R.id.input_password)
    MaterialEditText input_password;

    @BindView(R.id.btn_back)
    ImageButton btn_back;

    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.signIn)
    Button signIn;
    @BindView(R.id.to_signUp)
    TextView to_signUp;

    private String account,psw;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_signin;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("登录");
        btn_back.setVisibility(View.VISIBLE);
        btn_back.setOnClickListener(this);
        signIn.setOnClickListener(this);
        to_signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signIn:
                account = input_account.getText().toString();
                psw = input_password.getText().toString();
                if (account.isEmpty()||psw.isEmpty()){
                    Toast.makeText(getActivity(),"请正确输入!",Toast.LENGTH_SHORT).show();
                }else{
                    signIn(account,psw);
                }
                break;
            case R.id.btn_back:
                getActivity().finish();
                break;
            case R.id.to_signUp:
                intent(SignUpActivity.class);
                break;
            default:
        }
    }

    private void signIn(final String account, final String psw) {
        AVUser.logInInBackground(account, psw, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    SharedPreferences.Editor editor = getSharedPreferences("currentUser",MODE_PRIVATE).edit();
                    editor.putString("username",account);
                    editor.putString("password",psw);
                    editor.commit();
                    Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    Log.d("gg",e.toString());
                    Toast.makeText(getActivity(),"密码错误或用户名不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
