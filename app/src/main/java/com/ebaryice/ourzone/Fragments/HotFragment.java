package com.ebaryice.ourzone.Fragments;

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
import com.ebaryice.ourzone.Activities.MainActivity;
import com.ebaryice.ourzone.Adapter.StoryRVAdapter;
import com.ebaryice.ourzone.Basics.BaseFragment;
import com.ebaryice.ourzone.Bean.StoryBean;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();
        refreshLayout.setColorSchemeColors(Color.parseColor("#29b6f6"));
        refreshLayout.setProgressViewEndTarget(true,150);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        user = ((MainActivity)activity).getAVUser();
    }

    private void refresh(){
        final List<StoryBean> storyBeans = new ArrayList<>();

        final List<StoryBean> orderStories = new ArrayList<>();

        AVQuery<AVObject> query = new AVQuery<>("Story");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                int [] a = new int[list.size()];
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
                    storyBeans.add(bean);
                }

                //快速排序
                for (int j=0;j<list.size();j++){
                    StoryBean bean = storyBeans.get(j);
                    a[j] = bean.getNum_comment()+bean.getNum_like();
                }
                sort(a,0,a.length-1);

                for (int k=0;k<list.size();k++){
                    for (int l=0;l<list.size();l++){
                        StoryBean bean = storyBeans.get(l);
                        if (bean.getNum_like()+bean.getNum_comment() == a[storyBeans.size()-1-k]){
                            orderStories.add(bean);
                            continue;
                        }
                    }
                }

                adapter = new StoryRVAdapter(orderStories,getActivity(),user);
                recyclerView.setAdapter(adapter);
            }
        });
    }


    private void sort(int[] a,int low,int high){
        int start = low;
        int end = high;
        int key = a[low];
        while(end>start){
            //从后往前比较
            while(end>start&&a[end]>=key)  //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if(a[end]<=key){
                int temp = a[end];
                a[end] = a[start];
                a[start] = temp;
            }
            //从前往后比较
            while(end>start&&a[start]<=key)//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if(a[start]>=key){
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if(start>low) sort(a,low,start-1);//左边序列。第一个索引位置到关键值索引-1
        if(end<high) sort(a,end+1,high);//右边序列。从关键值索引+1到最后一个
    }
}
