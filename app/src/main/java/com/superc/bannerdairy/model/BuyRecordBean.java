package com.superc.bannerdairy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 2018/1/15.
 */

public class BuyRecordBean {


    /**
     * success : true
     * show : false
     * msg :
     * data : {"total":7,"per_page":"10","current_page":1,"last_page":1,"data":[{"user_money_id":"1","money":"12748.32","source":"5","created":"2018-01-11 22:33:47"},{"user_money_id":"2016","money":"-109.20","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2025","money":"18545.28","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2312","money":"-468.00","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2330","money":"-2957.76","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2342","money":"-2845.44","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2344","money":"-10046.40","source":"3","created":"2018-01-11 22:33:49"}]}
     */

    @SerializedName("success")
    private boolean success;
    @SerializedName("show")
    private boolean show;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * total : 7
         * per_page : 10
         * current_page : 1
         * last_page : 1
         * data : [{"user_money_id":"1","money":"12748.32","source":"5","created":"2018-01-11 22:33:47"},{"user_money_id":"2016","money":"-109.20","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2025","money":"18545.28","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2312","money":"-468.00","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2330","money":"-2957.76","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2342","money":"-2845.44","source":"3","created":"2018-01-11 22:33:49"},{"user_money_id":"2344","money":"-10046.40","source":"3","created":"2018-01-11 22:33:49"}]
         */

        @SerializedName("total")
        private int total;
        @SerializedName("per_page")
        private String perPage;
        @SerializedName("current_page")
        private int currentPage;
        @SerializedName("last_page")
        private int lastPage;
        @SerializedName("data")
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getPerPage() {
            return perPage;
        }

        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * user_money_id : 1
             * money : 12748.32
             * source : 5
             * created : 2018-01-11 22:33:47
             * username 用户名
             */

            @SerializedName("user_money_id")
            private String userMoneyId;
            @SerializedName("money")
            private String money;
            @SerializedName("source")
            private String source;
            @SerializedName("created")
            private String created;
            @SerializedName("username")
            private String username;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUserMoneyId() {
                return userMoneyId;
            }

            public void setUserMoneyId(String userMoneyId) {
                this.userMoneyId = userMoneyId;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }
        }
    }
}
