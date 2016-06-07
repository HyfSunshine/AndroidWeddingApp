package com.gemptc.wd.bean;

import java.io.Serializable;

public class PostBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6407223882612571932L;
	/**
	 * CREATE TABLE `bbspost` (
	 * `post_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帖子主键',
	 * `post_type` int(11) DEFAULT NULL COMMENT '普通/置顶/推荐',
	 * `post_section` varchar(20) DEFAULT NULL COMMENT '版块',
	 * `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
	 * `user_name` varchar(20) DEFAULT NULL COMMENT '用户名',
	 * `post_title` varchar(50) DEFAULT NULL COMMENT '帖子标题',
	 * `post_content` tinytext COMMENT '帖子内容',
	 * `post_time` varchar(50) DEFAULT NULL COMMENT '发帖时间',
	 * PRIMARY KEY (`post_id`)
	 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8
	 */
	
	private int postID;//帖子主键
	private int postType;//普通/置顶/推荐
	private String section;//版块
	private int userID;//用户ID
	private String userName;//用户名
	private String postTitle;//帖子标题
	private String postContent;//帖子内容
	private String postTime;//发帖时间
	//不在数据库中
	private int replyNum;//
	
	
	public int getPostID() {
		return postID;
	}
	public void setPostID(int postID) {
		this.postID = postID;
	}
	public int getPostType() {
		return postType;
	}
	public void setPostType(int postType) {
		this.postType = postType;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public int getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}
	
}