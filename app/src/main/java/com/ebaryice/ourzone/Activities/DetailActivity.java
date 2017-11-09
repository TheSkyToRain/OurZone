package com.ebaryice.ourzone.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.Bean.StoryBean;
import com.ebaryice.ourzone.R;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by Ebaryice on 2017/11/2.
 */

public class DetailActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.detail_user_icon)
    RoundedImageView detail_icon;
    @BindView(R.id.detail_user_name)
    TextView detail_username;
    @BindView(R.id.detail_send_time)
    TextView detail_sendTime;
    @BindView(R.id.detail_content_text)
    TextView detail_contentText;
    @BindView(R.id.detail_content_image)
    ImageView detail_contentImage;
    @BindView(R.id.detail_likes)
    ImageView detail_likes;
    @BindView(R.id.detail_like_num)
    TextView detail_like_num;
    @BindView(R.id.detail_comments)
    ImageView detail_comment;
    @BindView(R.id.detail_comment_num)
    TextView detail_comment_num;
    @BindView(R.id.btn_back)
    ImageButton back;
    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.chat)
    ImageButton chat;
    StoryBean bean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initialize() {
        bean = (StoryBean) getIntent().getSerializableExtra("story_data");
        toolbar_text.setText("动态详情");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        detail_contentImage.setOnClickListener(this);
        chat.setOnClickListener(this);
        resolveBean(bean);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.detail_content_image:
                final Intent intent = new Intent(getActivity(),PhotoDetailActivity.class);
                intent.putExtra("image",bean.getPicture());
                getActivity().startActivity(intent);
                break;
            case R.id.chat:
                LCChatKit.getInstance().open("Tom", new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (e == null){
                            finish();
                            Intent intentChat = new Intent(getActivity(), LCIMConversationActivity.class);
                            intentChat.putExtra(LCIMConstants.PEER_ID,"Jerry");
                            startActivity(intentChat);
                        }else{
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
        }
    }
    private void resolveBean(StoryBean bean){
        Glide.with(this).load(bean.getIcon()).into(detail_icon);
        detail_username.setText(bean.getUsername());
        detail_sendTime.setText(bean.getTime());
        detail_contentText.setText(bean.getContent());
        Glide.with(this).load(bean.getPicture()).into(detail_contentImage);
        detail_like_num.setText(bean.getNum_like()+"");
        detail_comment_num.setText(bean.getNum_comment()+"");
        if (bean.getUserLiked().contains(AVUser.getCurrentUser().getObjectId())){
            Glide.with(this).load(R.drawable.like).into(detail_likes);
        }else{
            Glide.with(this).load(R.drawable.likes).into(detail_likes);
        }
    }
}
