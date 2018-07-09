package com.superc.bannerdairy.model;

/**
 * Created by Amorr on 2017/11/22.
 * 我的邀请item
 */

public class MyInviteItem {
    private String imageUrl;
    private String name;
    private String stage;
    private String price;
    private String buyNum;

    /**
     *  "user_id":20,
     * //用户ID             "nickname":"111",
     * //昵称             "mobile":"18031180111",
     * //手机号             "user_ico":null,
     * //头像地址           
     *   "agency_level":3, //0：注册用户，1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理',  
     *            "created":"2017-12-01 14:12:46" //加入时间
     * */
    public String user_id;
    public String nickname;
    public String mobile;
    public String agency_level;
    public String created;
    public String user_ico;
    public String username;


    public MyInviteItem(String imageUrl, String name, String stage, String price, String buyNum) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.stage = stage;
        this.price = price;
        this.buyNum = buyNum;
    }
    public MyInviteItem(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAgency_level() {
        return agency_level;
    }

    public void setAgency_level(String agency_level) {
        this.agency_level = agency_level;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser_ico() {
        return user_ico;
    }

    public void setUser_ico(String user_ico) {
        this.user_ico = user_ico;
    }
}
