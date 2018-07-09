package com.superc.bannerdairy.model;

/**
 * 创建日期：2017/11/9 on 17:47
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class GoodsItem {

    private String imageUrl;
    private String name;
    private String stage;
    private String price;
    private String buyNum;

    public GoodsItem(String imageUrl, String name, String stage, String price, String buyNum) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.stage = stage;
        this.price = price;
        this.buyNum = buyNum;
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
}
