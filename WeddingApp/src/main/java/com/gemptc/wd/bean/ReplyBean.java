package com.gemptc.wd.bean;

import java.io.Serializable;

public class ReplyBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6062871733077124233L;
	
	private int replyID;
	private int postID;
	private int userID;
	private String replyContent;
	private String replyTime;
	
	//不在数据库中，但是要显示
	private String userName;
	private String userPicName;
	
	
	
	
	public ReplyBean(int replyID, int postID, int userID, String replyContent, String replyTime, String userName,
			String userPicName) {
		super();
		this.replyID = replyID;
		this.postID = postID;
		this.userID = userID;
		this.replyContent = replyContent;
		this.replyTime = replyTime;
		this.userName = userName;
		this.userPicName = userPicName;
	}
	public int getReplyID() {
		return replyID;
	}
	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}
	public int getPostID() {
		return postID;
	}
	public void setPostID(int postID) {
		this.postID = postID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPicName() {
		return userPicName;
	}
	public void setUserPicName(String userPicName) {
		this.userPicName = userPicName;
	}
	
}
