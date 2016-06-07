package com.gemptc.wd.bean;

import java.io.Serializable;

/**
 * Created by C5-0 on 2016/6/4.
 */
public class SellerData implements Serializable {
    private String preUtils;
    private String sellerOp;

    public SellerData(String preUtils, String sellerOp) {
        this.preUtils = preUtils;
        this.sellerOp = sellerOp;
    }

    public String getPreUtils() {
        return preUtils;
    }

    public void setPreUtils(String preUtils) {
        this.preUtils = preUtils;
    }

    public String getSellerOp() {
        return sellerOp;
    }

    public void setSellerOp(String sellerOp) {
        this.sellerOp = sellerOp;
    }
}
