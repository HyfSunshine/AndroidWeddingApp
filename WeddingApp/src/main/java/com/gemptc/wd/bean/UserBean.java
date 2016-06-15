package com.gemptc.wd.bean;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable{
	/*
	 * 
u_id 			int(11) NOT NULL用户表的主键
u_picname 		varchar(50) NULL图片名字
u_name 			varchar(20) NULL用户名
U_phonenum	 	int(11) NULL手机号码
u_password 		varchar(16) NULL用户密码
u_sex 			varchar(5) NULL性别
u_age 			int(11) NULL年龄
u_marry 		timedate NULL结婚时间
u_follownum 	int(11) NULL关注数
u_fansnum 		int(11) NULL粉丝数
u_address 		varchar(50) NULL地址
u_sign 			varchar(200) NULL个性签名
	 */
	
	private int u_id;
	private String u_picname;
	private String u_name;
	private String u_phonenum;
	private String u_password;
	private String u_sex;
	private int u_age;
	private String u_marrytime;
	private int u_follownum;
	private int u_fansnum;
	private String u_address;
	private String u_sign;
	public int getU_id() {
		return u_id;
	}
	public void setU_id(int u_id) {
		this.u_id = u_id;
	}
	public String getU_picname() {
		return u_picname;
	}
	public void setU_picname(String u_picname) {
		this.u_picname = u_picname;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_phonenum() {
		return u_phonenum;
	}
	public void setU_phonenum(String u_phonenum) {
		this.u_phonenum = u_phonenum;
	}
	public String getU_password() {
		return u_password;
	}
	public void setU_password(String u_password) {
		this.u_password = u_password;
	}
	public String getU_sex() {
		return u_sex;
	}
	public void setU_sex(String u_sex) {
		this.u_sex = u_sex;
	}
	public int getU_age() {
		return u_age;
	}
	public void setU_age(int u_age) {
		this.u_age = u_age;
	}
	public String getU_marrytime() {
		return u_marrytime;
	}
	public void setU_marrytime(String u_marrytime) {
		this.u_marrytime = u_marrytime;
	}
	public int getU_follownum() {
		return u_follownum;
	}
	public void setU_follownum(int u_follownum) {
		this.u_follownum = u_follownum;
	}
	public int getU_fansnum() {
		return u_fansnum;
	}
	public void setU_fansnum(int u_fansnum) {
		this.u_fansnum = u_fansnum;
	}
	public String getU_address() {
		return u_address;
	}
	public void setU_address(String u_address) {
		this.u_address = u_address;
	}
	public String getU_sign() {
		return u_sign;
	}
	public void setU_sign(String u_sign) {
		this.u_sign = u_sign;
	}
	@Override
	public String toString() {
		return "UserBean [u_id=" + u_id + ", u_picname=" + u_picname + ", u_name=" + u_name + ", u_phonenum="
				+ u_phonenum + ", u_password=" + u_password + ", u_sex=" + u_sex + ", u_age=" + u_age + ", u_marrytime="
				+ u_marrytime + ", u_follownum=" + u_follownum + ", u_fansnum=" + u_fansnum + ", u_address=" + u_address
				+ ", u_sign=" + u_sign + "]";
	}
	
}
