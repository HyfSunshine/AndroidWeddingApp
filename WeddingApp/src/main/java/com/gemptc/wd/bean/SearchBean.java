package com.gemptc.wd.bean;

/**
 * Created by zhaozhifei on 2016/6/1.
 */
public class SearchBean {
    private String postTitle;

    public SearchBean() {
    }

    public SearchBean(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
