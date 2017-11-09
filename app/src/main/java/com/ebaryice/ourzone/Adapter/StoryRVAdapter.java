package com.ebaryice.ourzone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.ebaryice.ourzone.Activities.DetailActivity;
import com.ebaryice.ourzone.Bean.StoryBean;
import com.ebaryice.ourzone.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ebaryice on 2017/10/31.
 */

public class StoryRVAdapter extends RecyclerView.Adapter<StoryRVAdapter.MyViewHolder>{

    private Context context;
    private AVUser user;
    private List<StoryBean> beans;

    public StoryRVAdapter(List<StoryBean> storyBeans, Context context, AVUser user){
        this.beans = storyBeans;
        this.context = context;
        this.user = user;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.username.setText(beans.get(position).getUsername());
        holder.contentText.setText(beans.get(position).getContent());
        holder.time.setText(beans.get(position).getTime());
        Glide.with(context).load(beans.get(position).getIcon()).asBitmap().into(holder.icon);
        Glide.with(context).load(beans.get(position).getPicture()).into(holder.contentImg);
        holder.like_num.setText(beans.get(position).getNum_like()+"");
        holder.comment_num.setText(beans.get(position).getNum_comment()+"");
        //判断是否已经点赞过
        if (user != null){
            if (beans.get(position).getUserLiked().contains(user.getObjectId())){
                Glide.with(context).load(R.drawable.like).into(holder.likes);
            }else{
                Glide.with(context).load(R.drawable.likes).into(holder.likes);
            }
        }
        final StoryBean bean = beans.get(position);
        //点击评论跳转
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("story_data",bean);
                context.startActivity(intent);
            }
        });
        //点击文字跳转
        holder.contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("story_data",bean);
                context.startActivity(intent);
            }
        });
        //点击图片跳转
        holder.contentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("story_data",bean);
                context.startActivity(intent);
            }
        });
        //点赞
        if (user == null){
            Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
        }else{
            holder.likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.getUserLiked().contains(user.getObjectId())){
                        Toast.makeText(context,"你已经点过赞啦~",Toast.LENGTH_SHORT).show();
                    }else{
                        bean.getUserLiked().add(user.getObjectId());
                        notifyDataSetChanged();
                        Glide.with(context).load(R.drawable.like).into(holder.likes);
                        Log.d("likelike",bean.getUserLiked().size()+"个");
                        holder.like_num.setText(String.format(context.getResources().getString(R.string.likes),bean.getUserLiked().size()));
                        //网络后台操作
                        updateCloud(bean.getObjectId(),bean.getNum_like()+1,bean.getUserLiked());
                    }
                }
            });
        }
    }

    private void updateCloud(String id,int likes,List<String> userLiked){
        AVObject todo = AVObject.createWithoutData("Story",id);
        todo.put("likes",likes);
        todo.put("userLiked",userLiked);
        todo.saveInBackground();
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_icon)
        RoundedImageView icon;
        @BindView(R.id.user_name)
        TextView username;
        @BindView(R.id.send_time)
        TextView time;
        @BindView(R.id.content_text)
        TextView contentText;
        @BindView(R.id.content_image)
        ImageView contentImg;
        @BindView(R.id.likes)
        ImageView likes;
        @BindView(R.id.like_num)
        TextView like_num;
        @BindView(R.id.comment)
        ImageView comment;
        @BindView(R.id.comment_num)
        TextView comment_num;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}