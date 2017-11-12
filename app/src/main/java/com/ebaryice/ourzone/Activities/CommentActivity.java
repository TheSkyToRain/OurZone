package com.ebaryice.ourzone.Activities;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ebaryice.ourzone.Basics.BaseActivity;
import com.ebaryice.ourzone.Bean.StoryBean;
import com.ebaryice.ourzone.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Ebaryice on 2017/11/10.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.comment_editText)
    MaterialEditText comment_editText;
    @BindView(R.id.btn_comment)
    Button btn_comment;
    @BindView(R.id.toolbar_text)
    TextView toolbar_text;
    @BindView(R.id.btn_back)
    ImageButton back;
    private StoryBean bean;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initialize() {
        bean = (StoryBean) getIntent().getSerializableExtra("data");
        toolbar_text.setText("评论");
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        btn_comment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_comment:
                String realComment = AVUser.getCurrentUser().getString("nickname");
                String comment = comment_editText.getText().toString();
                realComment = realComment+" : "+comment;
                if (comment.isEmpty()) Toast.makeText(this,"评论不能为空",Toast.LENGTH_SHORT).show();
                else {
                    uploadComment(realComment,bean.getObjectId(),bean.getCommentsList());
                    finish();
                    Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    private void uploadComment(String comment,String id,List<String> comments){
        AVObject todo = AVObject.createWithoutData("Story",id);
        comments.add(comment);
        todo.put("commentsList",comments);
        todo.saveInBackground();
    }
}
