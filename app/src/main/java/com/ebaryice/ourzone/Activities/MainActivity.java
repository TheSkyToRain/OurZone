package com.ebaryice.ourzone.Activities;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.Fragments.DiscoveryFragment;
import com.ebaryice.ourzone.Fragments.HotFragment;
import com.ebaryice.ourzone.Fragments.MyFragment;
import com.ebaryice.ourzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

public class MainActivity extends BaseActivity {
    //亮蓝色,主题色
    private int BottomNavigationBar_Color = Color.parseColor("#03a9f4");

    private List<Fragment> fragments = new ArrayList<>();

    @BindView(R.id.bottomBar)
    BottomNavigationBar bottomBar;

    /**
     * 上一次点击 back 键的时间
     * 用于双击退出的判断
     */
    private static long lastBackTime = 0;

    /**
     * 当双击 back 键在此间隔内是直接触发 onBackPressed
     */
    private final int BACK_INTERVAL = 1000;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initialize() {
        bottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomBar.setBackgroundColor(BottomNavigationBar_Color);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.discovery_bottom,"发现").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.hot_bottom,"热门").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.message_bottom,"消息").setActiveColor(BottomNavigationBar_Color))
                .addItem(new BottomNavigationItem(R.drawable.me_bottom,"我").setActiveColor(BottomNavigationBar_Color))
                .setFirstSelectedPosition(0)
                .initialise();
        fragments = getFragments();
        setDefaultFragment(fragments);
        setBottomBar();
    }

    private void setBottomBar(){
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (fragments != null){
                    if (position < fragments.size()){
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        if (fragment.isAdded()){
                            ft.show(fragment);
                        }else{
                            ft.add(R.id.frameLayout,fragment);
                        }
                        ft.commit();
                    }
                }
            }

            @Override
            public void onTabUnselected(int position) {
                if (fragments != null){
                    if (position < fragments.size()){
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Fragment fragment = fragments.get(position);
                        ft.hide(fragment);
                        ft.commit();
                    }
                }
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackTime < BACK_INTERVAL) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
        }
        lastBackTime = currentTime;
    }

    private void setDefaultFragment(List<Fragment> fragments) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.add(R.id.frameLayout,fragments.get(0));
        tx.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DiscoveryFragment());
        fragments.add(new HotFragment());
        fragments.add(new LCIMConversationListFragment());
        fragments.add(new MyFragment());
        return fragments;
    }

    public AVUser getAVUser(){
        AVOSCloud.initialize(getActivity(),"1gsStpMGOIbiVQJMJ7GRho1R-gzGzoHsz","pvXUhF0qEA7Jdo5K4knCxHDE");
        return AVUser.getCurrentUser();
    }
}
