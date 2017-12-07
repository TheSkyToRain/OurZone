package com.ebaryice.ourzone.activities.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ebaryice.ourzone.activities.MainActivity;
import com.ebaryice.ourzone.activities.PushActivity;
import com.ebaryice.ourzone.adapter.StoryRVAdapter;
import com.ebaryice.ourzone.basics.BaseFragment;
import com.ebaryice.ourzone.bean.StoryBean;
import com.ebaryice.ourzone.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
/**
 * Created by Ebaryice on 2017/10/31.
 */

public class DiscoveryFragment extends BaseFragment {

    @BindView(R.id.discovery_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.discovery_refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    StoryRVAdapter adapter;
    private AVUser user;


    @Override
    protected int getResourcesId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("发现");
        refreshLayout.setColorSchemeColors(Color.parseColor("#29b6f6"));
        refreshLayout.setProgressViewEndTarget(true,150);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AVUser.getCurrentUser()==null){
                    Toast.makeText(getActivity(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                else {intent(PushActivity.class);}
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setRefreshing(true);
        loading();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user = ((MainActivity)activity).getAVUser();
    }

    public void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading();
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private void loading(){
        final List<StoryBean> storyBeans = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("Story");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (int i=0;i<list.size();i++){
                    StoryBean bean = new StoryBean();
                    bean.setIcon(list.get(i).getString("userIcon"));
                    bean.setContent(list.get(i).getString("contentText"));
                    bean.setUsername(list.get(i).getString("username"));
                    bean.setTime(list.get(i).getString("sendTime"));
                    bean.setPicture(list.get(i).getString("contentImage"));
                    bean.setNum_like(list.get(i).getInt("likes"));
                    bean.setNum_comment(list.get(i).getInt("comments"));
                    bean.setUserLiked(list.get(i).getList("userLiked"));
                    bean.setCommentsList(list.get(i).getList("commentsList"));
                    bean.setObjectId(list.get(i).getObjectId());
                    storyBeans.add(bean);
                }
                Collections.reverse(storyBeans);
                adapter = new StoryRVAdapter(storyBeans,getActivity(),user);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
