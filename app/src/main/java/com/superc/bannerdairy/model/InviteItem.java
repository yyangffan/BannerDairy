package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018/1/26.
 */

public class InviteItem {

    /**
     * success : true
     * show : false
     * msg :
     * data : [{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=0","invitation_price":"会员：260元/听","invitation_level":"邀请会员"},{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=1","invitation_price":"特约：220元/听","invitation_level":"邀请特约"},{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=2","invitation_price":"门店：198元/听","invitation_level":"邀请门店"},{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=3","invitation_price":"县代：180元/听","invitation_level":"邀请县代"},{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=4","invitation_price":"市代：166元/听","invitation_level":"邀请市代"},{"invitation_url":"qizhi.linghangnc.com?code=18731124090&agency_level=5","invitation_price":"省代：156元/听","invitation_level":"邀请省代"}]
     */

    @SerializedName("success")
    private boolean success;
    @SerializedName("show")
    private boolean show;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * invitation_url : qizhi.linghangnc.com?code=18731124090&agency_level=0
         * invitation_price : 会员：260元/听
         * invitation_level : 邀请会员
         */

        @SerializedName("invitation_url")
        private String invitationUrl;
        @SerializedName("invitation_price")
        private String invitationPrice;
        @SerializedName("invitation_level")
        private String invitationLevel;

        public String getInvitationUrl() {
            return invitationUrl;
        }

        public void setInvitationUrl(String invitationUrl) {
            this.invitationUrl = invitationUrl;
        }

        public String getInvitationPrice() {
            return invitationPrice;
        }

        public void setInvitationPrice(String invitationPrice) {
            this.invitationPrice = invitationPrice;
        }

        public String getInvitationLevel() {
            return invitationLevel;
        }

        public void setInvitationLevel(String invitationLevel) {
            this.invitationLevel = invitationLevel;
        }
    }
}
