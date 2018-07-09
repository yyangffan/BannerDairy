package com.superc.bannerdairy.model;

/**
 * Created by Amorr on 2017/11/18.
 * 平级推广item实体类
 */

public class LevelItem {
    private String imageUrl;
    private String name;
    private String stage;
    private String price;
    private String buyNum;


    /**新数据
     * "user_id":28,  //用户ID             
     * "nickname":"a",   //昵称            
     *  "mobile":"18031180613",  //手机号            
     *  "user_ico":null,   //头像地址          
     *    "agency_level":5,  //'代理级别 => 0：注册用户，1：特约代理，
     * 2：门店代理，3：区县代理，4：市级代理，5：省级代理',            
     *  "suborCount":1, //直属下级数量             
     * "teamCount":1  //团队数量
     * */
    public String user_id;
    public String nickname;
    public String mobile;
    public String user_ico;
    public String agency_level;
    public String suborCount;
    public String teamCount;
    public String username;

    public LevelItem(String imageUrl, String name, String stage, String price, String buyNum) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.stage = stage;
        this.price = price;
        this.buyNum = buyNum;
    }
    public LevelItem(){

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

    public String getUser_ico() {
        return user_ico;
    }

    public void setUser_ico(String user_ico) {
        this.user_ico = user_ico;
    }

    public String getAgency_level() {
        return agency_level;
    }

    public void setAgency_level(String agency_level) {
        this.agency_level = agency_level;
    }

    public String getSuborCount() {
        return suborCount;
    }

    public void setSuborCount(String suborCount) {
        this.suborCount = suborCount;
    }

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }
}
