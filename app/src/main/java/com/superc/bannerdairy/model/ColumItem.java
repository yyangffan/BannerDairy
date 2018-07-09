package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2017/12/23.
 */

public class ColumItem {

    /**
     * menu_ico : /upload/image/image/20171223/15140210546176.png
     * menu_name : 商品管理
     * menu_type : goods
     * category_id :
     */

    @SerializedName("menu_ico")
    private String menuIco;
    @SerializedName("menu_name")
    private String menuName;
    @SerializedName("menu_type")
    private String menuType;
    @SerializedName("category_id")
    private String categoryId;

    public String getMenuIco() {
        return menuIco;
    }

    public void setMenuIco(String menuIco) {
        this.menuIco = menuIco;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
