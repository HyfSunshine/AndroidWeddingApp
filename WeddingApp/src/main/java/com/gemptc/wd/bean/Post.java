package com.gemptc.wd.bean;

import java.io.Serializable;

/**
 * Created by zhaozhifei on 2016/5/20.
 */
public class Post implements Serializable{
    //private int postID;//帖子id
    //private String section;//帖子版块
    //private String userID;//用户id
    private int userPicName;//用户头像
    private  String userName;//发帖人姓名
    private String postTime;//发帖时间
    //private String postContent;//发帖内容
    private String postTitle;//帖子标题

   // private int postType;//帖子类型：普通/置顶

    private boolean zhiding;//用一个标志位来判断是否置顶

    public Post(int userPicName, String userName, String postTime, String postTitle, boolean zhiding) {
        this.userPicName = userPicName;
        this.userName = userName;
        this.postTime = postTime;
        this.postTitle = postTitle;
        this.zhiding = zhiding;
    }

    public int getUserPicName() {
        return userPicName;
    }

    public void setUserPicName(int userPicName) {
        this.userPicName = userPicName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public boolean getZhiding() {
        return zhiding;
    }

    public void setZhiding(boolean zhiding) {
        this.zhiding = zhiding;
    }
}
