package com.ebaryice.ourzone.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.Fragments.DiscoveryFragment;
import com.ebaryice.ourzone.Fragments.HotFragment;
import com.ebaryice.ourzone.Fragments.MessageFragment;
import com.ebaryice.ourzone.Fragments.MyFragment;
import com.ebaryice.ourzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    //亮蓝色,主题色
    private int BottomNavigationBar_Color = Color.parseColor("#03a9f4");

    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initialize() {
        bottomBar.setAutoHideEnabled(false);
        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.setBackgroundColor(BottomNavigationBar_Color);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.discovery_bottom,"发现").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.hot_bottom,"热门").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.message_bottom,"消息").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.me_bottom,"我").setActiveColor(BottomNavigationBar_Color))
                .setFirstSelectedPosition(0)
                .initialise();
        setDefaultFragment();
        fragments = getFragments();
        setBottomBar();
    }

    private void setBottomBar(){
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (fragments != null){
                    if (position < fragments.size()){
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
//                        if (fragment.isAdded()){
//                            ft.show(fragment);
//                        }else{
//                            ft.add(R.id.frameLayout,fragment);
//                        }
                        ft.replace(R.id.frameLayout,fragment);
                        ft.commit();
                    }
                }
            }

            @Override
            public void onTabUnselected(int position) {
                if (fragments != null){
                    if (position < fragments.size()){
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        ft.remove(fragment);
                        ft.commit();
                    }
                }
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.frameLayout,new DiscoveryFragment());
        tx.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DiscoveryFragment());
        fragments.add(new HotFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MyFragment());
        return fragments;
    }

    public AVUser getAVUser(){
        AVOSCloud.initialize(getActivity(),"1gsStpMGOIbiVQJMJ7GRho1R-gzGzoHsz","pvXUhF0qEA7Jdo5K4knCxHDE");
        return AVUser.getCurrentUser();
    }
}
