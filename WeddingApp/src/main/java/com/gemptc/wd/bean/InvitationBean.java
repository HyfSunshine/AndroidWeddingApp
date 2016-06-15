package com.gemptc.wd.bean;

import java.io.Serializable;

public class InvitationBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3269931845760684275L;
	/*Invite_id	请帖表的ID
	User_id	用户的ID
	Invite_name1	新郎姓名
	Invite_name2	新娘姓名
	Invite_time	婚礼时间
	Invate_address	地址
	Invate_content	致宾客*/
    private int invite_id;
    private int user_id;
    private String invite_name1;
    private String invite_name2;
    private String invite_time;
    private String invate_address;
    private String invate_content;
    private String invate_imageName;
    
    public String getInvate_imageName() {
		return invate_imageName;
	}

	public void setInvate_imageName(String invate_imageName) {
		this.invate_imageName = invate_imageName;
	}

	public InvitationBean() {
		// TODO Auto-generated constructor stub
	}

	public InvitationBean(int invite_id, int user_id, String invite_name1, String invite_name2, String invite_time,
			String invate_address, String invate_content) {
		super();
		this.invite_id = invite_id;
		this.user_id = user_id;
		this.invite_name1 = invite_name1;
		this.invite_name2 = invite_name2;
		this.invite_time = invite_time;
		this.invate_address = invate_address;
		this.invate_content = invate_content;
	}

	public int getInvite_id() {
		return invite_id;
	}

	public void setInvite_id(int invite_id) {
		this.invite_id = invite_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getInvite_name1() {
		return invite_name1;
	}

	public void setInvite_name1(String invite_name1) {
		this.invite_name1 = invite_name1;
	}

	public String getInvite_name2() {
		return invite_name2;
	}

	public void setInvite_name2(String invite_name2) {
		this.invite_name2 = invite_name2;
	}

	public String getInvite_time() {
		return invite_time;
	}

	public void setInvite_time(String invite_time) {
		this.invite_time = invite_time;
	}

	public String getInvate_address() {
		return invate_address;
	}

	public void setInvate_address(String invate_address) {
		this.invate_address = invate_address;
	}

	public String getInvate_content() {
		return invate_content;
	}

	public void setInvate_content(String invate_content) {
		this.invate_content = invate_content;
	}

	@Override
	public String toString() {
		return "InvitationBean{" +
				"invate_address='" + invate_address + '\'' +
				", invite_id=" + invite_id +
				", user_id=" + user_id +
				", invite_name1='" + invite_name1 + '\'' +
				", invite_name2='" + invite_name2 + '\'' +
				", invite_time='" + invite_time + '\'' +
				", invate_content='" + invate_content + '\'' +
				", invate_imageName='" + invate_imageName + '\'' +
				'}';
	}
}
