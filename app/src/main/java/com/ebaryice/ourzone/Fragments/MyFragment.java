package com.ebaryice.ourzone.Fragments;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.Activities.MainActivity;
import com.ebaryice.ourzone.Activities.SignInActivity;
import com.ebaryice.ourzone.Basics.BaseFragment;
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
    @BindView(R.id.logOut)
    Button logout;

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
        logout.setOnClickListener(this);
        textViewList.get(0).setText("我");
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
            relativeLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myData:
                if (user==null){
                    intent(SignInActivity.class);
                }
                break;
            case R.id.myPublish:
                break;
            case R.id.changeData:
                break;
            case R.id.changePassword:
                break;
            case R.id.advice:
                break;
            case R.id.about:
                break;
            case R.id.shareSoftware:
                break;
            case R.id.logOut:
                AVUser.getCurrentUser().logOut();
                //无用户登录时
                textViewList.get(1).setText("请先登录");
                textViewList.get(2).setText("编辑个性签名");
                Glide.with(getActivity()).load(R.drawable.icon).into(myIcon);
                break;
            default:
        }
    }

}
