package com.ebaryice.ourzone.activities;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ebaryice.ourzone.basics.BaseActivity;
import com.ebaryice.ourzone.bean.StoryBean;
import com.ebaryice.ourzone.R;
import com.rengwuxian.materialedittext.MaterialEditText;

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
    ProgressDialog progressDialog;
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
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("正在发布，请稍等");
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    uploadComment(realComment,bean.getObjectId(),bean.getCommentsList());
                    Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
                    finish();
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
        progressDialog.dismiss();
    }
}
