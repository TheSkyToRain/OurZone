package com.ebaryice.ourzone.activities;

import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.basics.BaseActivity;
import com.ebaryice.ourzone.R;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Ebaryice on 2017/11/7.
 */

public class PDetailActivity extends BaseActivity {

    @BindView(R.id.photoView)
    PhotoView photoView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initialize() {
        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(photoView);
    }
}
