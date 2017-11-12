package com.ebaryice.ourzone.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVIMClientParcel;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.CustomUserProvider;
import com.ebaryice.ourzone.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by Ebaryice on 2017/10/31.
 */

public class StartActivity extends BaseActivity {

    @BindView(R.id.start_imgview)
    ImageView start_img;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initialize() {
        Log.d("AVCloud","初始化成功");
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(getActivity(),"1gsStpMGOIbiVQJMJ7GRho1R-gzGzoHsz","pvXUhF0qEA7Jdo5K4knCxHDE");
        AVIMClient.setUnreadNotificationEnabled(true);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getActivity().getApplicationContext(),"1gsStpMGOIbiVQJMJ7GRho1R-gzGzoHsz","pvXUhF0qEA7Jdo5K4knCxHDE");

        SharedPreferences pref = getSharedPreferences("currenUser",MODE_PRIVATE);
        String username = pref.getString("username","");
        String password = pref.getString("password","");
        if (username.length()!=0&&password.length()!=0){
            preSignIn(username,password);
        }
        skip();
    }


    private void preSignIn(String username, String password) {
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e==null){
                    Log.d("preSignIn","成功");
                }
                else{
                    Log.d("preSignInFailed","失败");
                }
            }
        });
    }
    private void skip(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                getActivity().finish();
            }
        };
        timer.schedule(task,1000*2);
    }
}
