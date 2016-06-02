package com.gemptc.wd.bean;

import java.io.Serializable;

/**
 * Created by C5-0 on 2016/6/1.
 */
public class ProductBean implements Serializable {
    /**
     * 案例的javaBean类
     */
    private static final long serialVersionUID = 1L;
    private String prBgroundName;
    private String prDescription;
    private String prFansNum;
    //案例的粉丝数，不在数据库中
    private String prListPicName;
    private String productClass;
    private int productId;
    private String productName;
    //这个类别是推荐的商品/普通的商品
    private int productType;
    private int sellerId;
    //不在数据库中
    private String sellerName;
    //这个类别是商品属于哪六大类中的一类
    public ProductBean() {
    }

    public String getPrBgroundName() {
        return prBgroundName;
    }

    public void setPrBgroundName(String prBgroundName) {
        this.prBgroundName = prBgroundName;
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

    public String getPrListPicName() {
        return prListPicName;
    }

    public void setPrListPicName(String prListPicName) {
        this.prListPicName = prListPicName;
    }

    public String getProductClass() {
        return productClass;
    }

    public void setProductClass(String productClass) {
        this.productClass = productClass;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    @Override
    public String toString() {
        return "ProductBean{" +
                "prBgroundName='" + prBgroundName + '\'' +
                ", prDescription='" + prDescription + '\'' +
                ", prFansNum='" + prFansNum + '\'' +
                ", prListPicName='" + prListPicName + '\'' +
                ", productClass='" + productClass + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productType=" + productType +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}
