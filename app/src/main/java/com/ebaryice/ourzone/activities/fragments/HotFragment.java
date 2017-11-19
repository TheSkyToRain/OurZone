package com.ebaryice.ourzone.activities.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ebaryice.ourzone.activities.MainActivity;
import com.ebaryice.ourzone.adapter.StoryRVAdapter;
import com.ebaryice.ourzone.basics.BaseFragment;
import com.ebaryice.ourzone.bean.StoryBean;
import com.ebaryice.ourzone.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * Created by Ebaryice on 2017/10/31.
 */

public class HotFragment extends BaseFragment {

    @BindView(R.id.hot_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hot_refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    StoryRVAdapter adapter;
    private AVUser user;


    @Override
    protected int getResourcesId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("热门");
        refreshLayout.setColorSchemeColors(Color.parseColor("#29b6f6"));
        refreshLayout.setProgressViewEndTarget(true,150);
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
    public void onResume() {
        super.onResume();
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
                int[] sum = new int[storyBeans.size()];
                //计算总数
                for (int i=0;i<storyBeans.size();i++){
                    StoryBean bean = storyBeans.get(i);
                    sum[i] = bean.getCommentsList().size()+bean.getUserLiked().size();
                }
                //排序
                int k,temp;
                for (int i=0;i<storyBeans.size()-1;i++){
                    k = i;
                    for (int j=i+1;j<storyBeans.size();j++){
                        if (sum[j]>sum[k]) k=j;
                    }
                    if (k!=i){
                        temp = sum[k];
                        sum[k] = sum[i];
                        sum[i] = temp;
                    }
                }
                //重新加data
                List<StoryBean> beans = new ArrayList<>();
                int[] sum1 = new int[storyBeans.size()];
                //计算总数
                for (int i=0;i<storyBeans.size();i++){
                    StoryBean bean = storyBeans.get(i);
                    sum1[i] = bean.getCommentsList().size()+bean.getUserLiked().size();
                }
                for (int i=0;i<sum.length;i++){
                    for (int j=0;j<sum.length;j++){
                        if (sum1[j] == sum[i]){
                            beans.add(storyBeans.get(j));
                            sum1[j] = -1;
                            break;
                        }
                    }
                }
                Log.d("beanSize",beans.size()+"");
                adapter = new StoryRVAdapter(beans,getActivity(),user);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
