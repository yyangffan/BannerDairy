package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018/3/29.
 */

public class StockLiushuiBean {

    /**
     * success : true
     * show : false
     * msg :
     * data : [{"inventory_id":"0","inventory_mobile":"0","creator":"58494","creator_mobile":"1","order_id":"3","created":"2018-03-28 18:32:25","goods_name":"旗帜奶粉","order_goods_number":"+2"},{"inventory_id":"0","inventory_mobile":"0","creator":"58494","creator_mobile":"1","order_id":"4","created":"2018-03-28 18:46:35","goods_name":"旗帜奶粉","order_goods_number":"+2"},{"inventory_id":"0","inventory_mobile":"0","creator":"58494","creator_mobile":"1","order_id":"5","created":"2018-03-28 18:53:49","goods_name":"旗帜奶粉","order_goods_number":"+2"},{"inventory_id":"0","inventory_mobile":"0","creator":"58494","creator_mobile":"1","order_id":"5","created":"2018-03-28 18:53:49","goods_name":"","order_goods_number":"+"}]
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
         * inventory_id : 0
         * inventory_mobile : 0
         * creator : 58494
         * creator_mobile : 1
         * order_id : 3
         * created : 2018-03-28 18:32:25
         * goods_name : 旗帜奶粉
         * order_goods_number : +2
         */

        @SerializedName("inventory_id")
        private String inventoryId;
        @SerializedName("inventory_mobile")
        private String inventoryMobile;
        @SerializedName("creator")
        private String creator;
        @SerializedName("creator_mobile")
        private String creatorMobile;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("created")
        private String created;
        @SerializedName("goods_name")
        private String goodsName;
        @SerializedName("order_goods_number")
        private String orderGoodsNumber;

        public String getInventoryId() {
            return inventoryId;
        }

        public void setInventoryId(String inventoryId) {
            this.inventoryId = inventoryId;
        }

        public String getInventoryMobile() {
            return inventoryMobile;
        }

        public void setInventoryMobile(String inventoryMobile) {
            this.inventoryMobile = inventoryMobile;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreatorMobile() {
            return creatorMobile;
        }

        public void setCreatorMobile(String creatorMobile) {
            this.creatorMobile = creatorMobile;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getOrderGoodsNumber() {
            return orderGoodsNumber;
        }

        public void setOrderGoodsNumber(String orderGoodsNumber) {
            this.orderGoodsNumber = orderGoodsNumber;
        }
    }
}
