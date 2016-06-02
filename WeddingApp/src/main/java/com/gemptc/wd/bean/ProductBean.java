package com.gemptc.wd.bean;

import java.io.Serializable;

public class ProductBean implements Serializable{
	/**
	 * 案例的javaBean类
	 */
	private static final long serialVersionUID = 1L;
	private int productId;
	//这个类别是推荐的商品/普通的商品
	private int productType;
	private int sellerId;
	//不在数据库中
	private String sellerName;
	//这个类别是商品属于哪六大类中的一类
	private String productClass;
	private String productName;
	private String prBgroundName;
	private String prListPicName;
	private String prDescription;
	//案例的粉丝数，不在数据库中
	private String prFansNum;
	public ProductBean() {
		// TODO Auto-generated constructor stub
	}
	public ProductBean(int productId, int productType, int sellerId, String sellerName, String productClass,
			String productName, String prBgroundName, String prListPicName, String prDescription, String prFansNum) {
		super();
		this.productId = productId;
		this.productType = productType;
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.productClass = productClass;
		this.productName = productName;
		this.prBgroundName = prBgroundName;
		this.prListPicName = prListPicName;
		this.prDescription = prDescription;
		this.prFansNum = prFansNum;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductType() {
		return productType;
	}
	public void setProductType(int productType) {
		this.productType = productType;
	}
	public int getSellerId() {
		return sellerId;
	}
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getProductClass() {
		return productClass;
	}
	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPrBgroundName() {
		return prBgroundName;
	}
	public void setPrBgroundName(String prBgroundName) {
		this.prBgroundName = prBgroundName;
	}
	public String getPrListPicName() {
		return prListPicName;
	}
	public void setPrListPicName(String prListPicName) {
		this.prListPicName = prListPicName;
	}
	public String getPrDescription() {
		return prDescription;
	}
	public void setPrDescription(String prDescription) {
		this.prDescription = prDescription;
	}
	public String getPrFansNum() {
		return prFansNum;
	}
	public void setPrFansNum(String prFansNum) {
		this.prFansNum = prFansNum;
	}
}
