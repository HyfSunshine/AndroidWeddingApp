package com.gemptc.wd.bean;

import java.io.Serializable;

/**
 * Created by C5-0 on 2016/6/1.
 */
//商家类
public class Seller implements Serializable {
    private int id;
    private String sellerAddress;//地址
    private int sellerFansNum;//粉丝数量
    private String sellerName;//商家名字
    private String sellerPhone;//商家电话
    private String sellerPicName;//商家头像图片名字
    private String sellerSign;//商家说明
    private String sellerType;//商家类型，如婚纱摄影，戒指，酒店，等
    private int typeId;//商家类型id

    public Seller() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public int getSellerFansNum() {
        return sellerFansNum;
    }

    public void setSellerFansNum(int sellerFansNum) {
        this.sellerFansNum = sellerFansNum;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerPicName() {
        return sellerPicName;
    }

    public void setSellerPicName(String sellerPicName) {
        this.sellerPicName = sellerPicName;
    }

    public String getSellerSign() {
        return sellerSign;
    }

    public void setSellerSign(String sellerSign) {
        this.sellerSign = sellerSign;
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

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", sellerAddress='" + sellerAddress + '\'' +
                ", sellerFansNum=" + sellerFansNum +
                ", sellerName='" + sellerName + '\'' +
                ", sellerPhone='" + sellerPhone + '\'' +
                ", sellerPicName='" + sellerPicName + '\'' +
                ", sellerSign='" + sellerSign + '\'' +
                ", sellerType='" + sellerType + '\'' +
                ", typeId=" + typeId +
                '}';
    }
}
