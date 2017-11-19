package com.ebaryice.ourzone.activities.fragments;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.activities.CDataActivity;
import com.ebaryice.ourzone.activities.LogOutActivity;
import com.ebaryice.ourzone.activities.MPubActivity;
import com.ebaryice.ourzone.activities.MainActivity;
import com.ebaryice.ourzone.activities.SignInActivity;
import com.ebaryice.ourzone.basics.BaseFragment;
import com.ebaryice.ourzone.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Ebaryice on 2017/10/31.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.myIcon)
    RoundedImageView myIcon;
    @BindViews({R.id.toolbar_text, R.id.myUsername,R.id.myAutograph})
    List<TextView> textViewList;
    @BindView(R.id.myData)
    RelativeLayout relativeLayout;
    @BindView(R.id.myPublish)
    TextView publish;
    @BindView(R.id.changeData)
    TextView changeData;
    @BindView(R.id.changePassword)
    TextView changePassword;
    @BindView(R.id.shareSoftware)
    TextView share;
    @BindView(R.id.update)
    TextView update;
    private AVUser user;

    @Override
    protected int getResourcesId() {return R.layout.fragment_my;}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user = ((MainActivity)activity).getAVUser();
    }

    @Override
    protected void initialize() {
        textViewList.get(0).setText("我");
        relativeLayout.setOnClickListener(this);
        publish.setOnClickListener(this);
        changeData.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        update.setOnClickListener(this);
        share.setOnClickListener(this);
        //有用户登录时
        if (user != null){
            textViewList.get(1).setText(user.getString("nickname"));
            textViewList.get(2).setText(user.getString("autograph"));
            Glide.with(getActivity()).load(user.getAVFile("userIcon").getUrl()).asBitmap().override(100,100).into(myIcon);
        }else{
            //无用户登录时
            textViewList.get(1).setText("请先登录");
            textViewList.get(2).setText("编辑个性签名");
            Glide.with(getActivity()).load(R.drawable.icon).into(myIcon);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myData:
                if (user==null) {
                    intent(SignInActivity.class);
                    SignInActivity.listener = new SignInActivity.FinishSignIn() {
                        @Override
                        public void onFinish(String username,String psw) {
                            try {
                                AVUser.logIn(username,psw);
                            } catch (AVException e) {
                                e.printStackTrace();
                            }
                            textViewList.get(1).setText(AVUser.getCurrentUser().getString("nickname"));
                            textViewList.get(2).setText(AVUser.getCurrentUser().getString("autograph"));
                            Glide.with(getActivity()).load(AVUser.getCurrentUser().getAVFile("userIcon").getUrl()).asBitmap().override(100,100).into(myIcon);
                        }
                    };
                }
                else {
                    intent(LogOutActivity.class);
                    LogOutActivity.listener = new LogOutActivity.FinishLogOut() {
                        @Override
                        public void onFinish(String msg) {
                            AVUser.getCurrentUser().logOut();
                            //无用户登录时
                            textViewList.get(1).setText("请先登录");
                            textViewList.get(2).setText("编辑个性签名");
                            Glide.with(getActivity()).load(R.drawable.icon).into(myIcon);
                        }
                    };
                }
                break;
            case R.id.myPublish:
                if (AVUser.getCurrentUser()==null){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }else{
                    intent(MPubActivity.class);
                }
                break;
            case R.id.changeData:
                if (AVUser.getCurrentUser()==null){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }else{
                    intent(CDataActivity.class);
                    CDataActivity.listener = new CDataActivity.FinishSave() {
                        @Override
                        public void onFinish(String msg) {
                            textViewList.get(1).setText(user.getString("nickname"));
                            textViewList.get(2).setText(user.getString("autograph"));
                            Glide.with(getActivity()).load(user.getAVFile("userIcon").getUrl()).asBitmap().override(100,100).into(myIcon);
                        }
                    };
                }
                break;
            case R.id.update:
                break;
            case R.id.shareSoftware:
                break;
            default:
        }
    }

}
