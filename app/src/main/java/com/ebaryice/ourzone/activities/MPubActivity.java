package com.ebaryice.ourzone.activities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ebaryice.ourzone.R;
import com.ebaryice.ourzone.adapter.StoryRVAdapter;
import com.ebaryice.ourzone.basics.BaseActivity;
import com.ebaryice.ourzone.bean.StoryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Ebaryice on 2017/11/18.
 */

public class MPubActivity extends BaseActivity{

    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.pub_recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_publish;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("我发布过");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setRV(getActivity());
    }

    private void setRV(final Context context) {
        final List<StoryBean> storyBeans = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("Story");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getString("userId")
                            .equals(AVUser.getCurrentUser().getObjectId())) {
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

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                StoryRVAdapter adapter = new StoryRVAdapter(storyBeans,context,AVUser.getCurrentUser());
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
