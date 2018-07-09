package com.superc.bannerdairy.model;

/**
 * Created by Amorr on 2017/12/12.
 *  {             "banner_id":1,   //id            
 *  "banner_image":"/upload/image/image/20171205/1512463336.png", 	//图片地址         }
 */

public class Banner  {
    public String banner_id;
    public String banner_image;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}
