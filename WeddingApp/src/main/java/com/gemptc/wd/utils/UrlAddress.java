package com.gemptc.wd.utils;

/**
 * Created by Administrator on 2016/5/31.
 */
public class UrlAddress {
    /*
	 * 本地使用
	 */
    public static final String HOST_ADDRESS="http://10.201.1.9:8080/";

    //public static final String HOST_ADDRESS="http://localhost:8080/";
    public static final String FILE_SAVE_ADDRESS="G:/JavaProject/WeddingProject/images/";

	/*
	 * 服务器上使用
	 */
    //public static final String HOST_ADDRESS="http://104.224.132.169:8080/";
    //public static final String FILE_SAVE_ADDRESS="/var/MyProject/images/";

    //工程地址
    public static final String HOST_ADDRESS_PROJECT=HOST_ADDRESS+"WeddingJson/";


    /**
     * 四个Controller
     */
    //用户
    public static final String USER_Controller=HOST_ADDRESS_PROJECT+"UserController";
    //商家
    public static final String SELLER_Controller=HOST_ADDRESS_PROJECT+"SellerController";
    //案例
    public static final String PRODUCT_Controller=HOST_ADDRESS_PROJECT+"ProductController";
    //帖子
    public static final String POST_Controller=HOST_ADDRESS_PROJECT+"PostController";

    public static final String USER_IMAGE_ADDRESS=HOST_ADDRESS+"images/userImages/";
    public static final String SELLER_IMAGE_ADDRESS=HOST_ADDRESS+"images/sellerImages/";
    public static final String PRODUCT_IMAGE_ADDRESS=HOST_ADDRESS+"images/productImages/";
    public static final String POST_IMAGE_ADDRESS=HOST_ADDRESS+"images/postImages/";
    public static final String LOGIN_IMAGE_ADDRESS=HOST_ADDRESS+"images/loginAndRegisterImages/";
}
