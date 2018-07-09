package com.superc.bannerdairy.model;

import android.databinding.BaseObservable;

/**
 * Created by JCY on 2017/7/4.
 * 说明：
 */

public class User extends BaseObservable {
    /**
     * user_id : 23
     * code : null
     * nickname : 普通会员
     * username : null
     * password : 96e79218965eb72c92a549dd5a330112
     * identity_number : null
     * mobile : 18031180612
     * user_ico : null
     * sex : 1
     * birthday : null
     * agency_level : 0
     * money : 0.00
     * activity : 1
     * referee : 0
     * superiors : 22
     * relegation : 0
     * administrator : 0
     * role_id : 0
     * promotion_time : null
     * creator : null
     * created : 1970-01-01 08:00:00
     * modifier : null
     * modified : 1970-01-01 08:00:00
     * delete_time : null
     *
     */
    //新增下单的全局数量，价格，id,规格id
    public int num;
    public  Double AllPirice;
    public String order_id;
    public String spec_id;



    public String user_id;
    public String buyer_logon_id;
    public Object code;
    public String nickname;
    public String username;
    public String password;
    public String identity_number;
    public String mobile;
    public Object user_ico;
    public String sex;
    public Object birthday;
    public String agency_level;
    public String money;
    public String activity;
    public String referee;
    public String superiors;
    public String relegation;
    public String administrator;
    public String role_id;
    public Object promotion_time;
    public Object creator;
    public String created;
    public Object modifier;
    public String modified;
    public Object delete_time;
    public String is_pay_pwd;
    public String spec_name;

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getIs_pay_pwd() {
        return is_pay_pwd;
    }

    public void setIs_pay_pwd(String is_pay_pwd) {
        this.is_pay_pwd = is_pay_pwd;
    }

    public User() {
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getUser_ico() {
        return user_ico;
    }

    public void setUser_ico(Object user_ico) {
        this.user_ico = user_ico;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getAgency_level() {
        return agency_level;
    }

    public void setAgency_level(String agency_level) {
        this.agency_level = agency_level;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getSuperiors() {
        return superiors;
    }

    public void setSuperiors(String superiors) {
        this.superiors = superiors;
    }

    public String getRelegation() {
        return relegation;
    }

    public void setRelegation(String relegation) {
        this.relegation = relegation;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public Object getPromotion_time() {
        return promotion_time;
    }

    public void setPromotion_time(Object promotion_time) {
        this.promotion_time = promotion_time;
    }

    public Object getCreator() {
        return creator;
    }

    public void setCreator(Object creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Object getModifier() {
        return modifier;
    }

    public void setModifier(Object modifier) {
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Double getAllPirice() {
        return AllPirice;
    }

    public void setAllPirice(Double allPirice) {
        AllPirice = allPirice;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }
}
