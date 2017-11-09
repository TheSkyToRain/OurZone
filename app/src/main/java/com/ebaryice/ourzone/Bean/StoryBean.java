package com.ebaryice.ourzone.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ebaryice on 2017/11/2.
 */

public class StoryBean implements Serializable{
    //头像url
    private String icon;
    //昵称
    private String username;
    //发送时间
    private String time;
    //发送文字内容
    private String content;
    //发送的图片url
    private String picture;
    //点赞人数
    private int num_like;
    //评论人数
    private int num_comment;
    //点过赞的列表
    private List<String> userLiked;
    //评论
    private List<String> commentsList;
    //story ID
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<String> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<String> comments) {
        this.commentsList = comments;
    }

    public List<String> getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(List<String> userLiked) {
        this.userLiked = userLiked;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getNum_like() {
        return num_like;
    }

    public void setNum_like(int num_like) {
        this.num_like = num_like;
    }

    public int getNum_comment() {
        return num_comment;
    }

    public void setNum_comment(int num_comment) {
        this.num_comment = num_comment;
    }
}
