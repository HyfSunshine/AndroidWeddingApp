package com.gemptc.wd.bean;

import java.io.Serializable;

/*
 * 这是商家的一个JavaBean
 */
public class SellerBean implements Serializable{
	private int id;
	private String sellerType;
	private int typeId;
	private String sellerPicName;
	private String sellerName;
	private int sellerFansNum;
	private String sellerSign;
	private String sellerAddress;
	private String sellerPhone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSellerType() {
		return sellerType;
	}
	public void setSellerType(String sellerType) {
		this.sellerType = sellerType;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getSellerPicName() {
		return sellerPicName;
	}
	public void setSellerPicName(String sellerPicName) {
		this.sellerPicName = sellerPicName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public int getSellerFansNum() {
		return sellerFansNum;
	}
	public void setSellerFansNum(int sellerFansNum) {
		this.sellerFansNum = sellerFansNum;
	}
	public String getSellerSign() {
		return sellerSign;
	}
	public void setSellerSign(String sellerSign) {
		this.sellerSign = sellerSign;
	}
	public String getSellerAddress() {
		return sellerAddress;
	}
	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}
	public String getSellerPhone() {
		return sellerPhone;
	}
	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}
}
