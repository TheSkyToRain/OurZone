package com.ebaryice.ourzone.basics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
/**
 * Created by Ebaryice on 2017/10/31.
 */

public abstract class BaseFragment extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getResourcesId(),container,false);
        ButterKnife.bind(this,mView);
        initialize();
        return mView;
    }

    protected abstract int getResourcesId();

    protected abstract void initialize();

    public void intent(Class<?> c){
        Intent intent = new Intent();
        intent.setClass(getActivity(),c);
        getActivity().startActivity(intent);
    }
}
