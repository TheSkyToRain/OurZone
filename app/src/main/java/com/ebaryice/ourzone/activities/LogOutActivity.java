package com.ebaryice.ourzone.activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.R;
import com.ebaryice.ourzone.basics.BaseActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

/**
 * Created by Ebaryice on 2017/11/18.
 */

public class LogOutActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.logout_icon)
    RoundedImageView logout_icon;
    @BindView(R.id.nickname_logout)
    TextView nickname;
    @BindView(R.id.username_logout)
    TextView username;
    @BindView(R.id.logout)
    Button logout;

    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.btn_back)
    ImageButton back;

    public static FinishLogOut listener;


    public interface FinishLogOut{
        void onFinish(String msg);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_logout;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("登录信息");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        logout.setOnClickListener(this);
        AVUser user = AVUser.getCurrentUser();
        nickname.setText(user.getString("nickname"));
        username.setText(user.getString("username"));
        Glide.with(getActivity()).load(user.getAVFile("userIcon").getUrl()).asBitmap().override(100,100).into(logout_icon);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.logout:
                if (listener!=null) listener.onFinish("logout");
                finish();
                break;
        }
    }
}
