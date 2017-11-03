package com.ebaryice.ourzone.Fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.Activities.SignActivity;
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

    @Override
    protected int getResourcesId() {return R.layout.fragment_my;}

    @Override
    protected void initialize() {
        textViewList.get(0).setText("我");
        AVUser user = AVUser.getCurrentUser();
        //有用户登录时
        if (user != null){
            textViewList.get(1).setText(AVUser.getCurrentUser().getString("nickname"));
            textViewList.get(2).setText(AVUser.getCurrentUser().getString("autograph"));
            //检查头像
            if (user.getAVFile("userIcon")==null){
                Log.d("userIcon","没有设置头像");
                Glide.with(getActivity()).load(R.drawable.icon).into(myIcon);
            }else{
                Glide.with(getActivity()).load(user.getAVFile("userIcon").getUrl()).asBitmap().override(100,100).into(myIcon);
            }
        }
        //无用户登录时
        textViewList.get(1).setText("请先登录");
        textViewList.get(2).setText("编辑个性签名");
        Glide.with(getActivity()).load(R.drawable.icon).into(myIcon);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SignActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            default:
        }
    }
}
