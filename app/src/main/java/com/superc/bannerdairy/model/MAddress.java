package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amorr on 2017/11/29.
 */

public class MAddress {
    /**
     * user_address_id : 2
     * province_id : 6
     * city_id : 345
     * county_id : 456
     * area_id : 456
     * street_id : 456
     * address : 456
     * contact : 456
     * mobile : 12222222222
     * default : 1
     * creator : 0
     * created : 2017-11-22 15:56:56
     * modifier : 0
     * modified : 2017-11-23 10:44:03
     * delete_time : null
     */
    public Boolean isSelect;
    public String user_address_id;
    public String province_id;
    public String city_id;
    public String county_id;
    public String area_id;
    public String street_id;
    public String address;
    public String contact;
    public String mobile;
    @SerializedName("default")
    public String defaultX;
    public String creator;
    public String created;
    public String modifier;
    public String modified;
    public Object delete_time;

    public String province;
    public String city;
    public String area;
//    public String express_fee;/*邮费*/
    /**
     * express_fee : {"express_fee":"","express_num_fee":""}
     */

    @SerializedName("express_fee")
    private ExpressFeeBean expressFee;


    public MAddress() {
    }

    public MAddress(String user_address_id, String province_id, String city_id, String county_id, String area_id, String street_id, String address, String contact, String mobile, String defaultX, String creator, String created, String modifier, String modified, Object delete_time, String province, String city, String area) {
        this.user_address_id = user_address_id;
        this.province_id = province_id;
        this.city_id = city_id;
        this.county_id = county_id;
        this.area_id = area_id;
        this.street_id = street_id;
        this.address = address;
        this.contact = contact;
        this.mobile = mobile;
        this.defaultX = defaultX;
        this.creator = creator;
        this.created = created;
        this.modifier = modifier;
        this.modified = modified;
        this.delete_time = delete_time;
        this.province = province;
        this.city = city;
        this.area = area;
    }

    public String getUser_address_id() {
        return user_address_id;
    }

    public void setUser_address_id(String user_address_id) {
        this.user_address_id = user_address_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCounty_id() {
        return county_id;
    }

    public void setCounty_id(String county_id) {
        this.county_id = county_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(String defaultX) {
        this.defaultX = defaultX;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Object getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(Object delete_time) {
        this.delete_time = delete_time;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
    public ExpressFeeBean getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(ExpressFeeBean expressFee) {
        this.expressFee = expressFee;
    }


    public static class ExpressFeeBean {
        /**
         * express_fee :
         * express_num_fee :
         */

        @SerializedName("express_fee")
        private String expressFee;
        @SerializedName("express_num_fee")
        private String expressNumFee;

        public String getExpressFee() {
            return expressFee==null||expressFee.equals("")?"0":expressFee;
        }

        public void setExpressFee(String expressFee) {
            this.expressFee = expressFee;
        }

        public String getExpressNumFee() {
            return expressNumFee==null||expressNumFee.equals("")?"0":expressNumFee;
        }

        public void setExpressNumFee(String expressNumFee) {
            this.expressNumFee = expressNumFee;
        }
    }
}
