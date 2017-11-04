package com.ebaryice.ourzone.Fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ebaryice.ourzone.Activities.PushActivity;
import com.ebaryice.ourzone.Basics.BaseFragment;
import com.ebaryice.ourzone.R;

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

    public DiscoveryFragment(){
        //
    }
    @Override
    protected int getResourcesId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void initialize() {
        toolbar_text.setText("发现");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushActivity.class);
                startActivity(intent);
            }
        });
    }
}
