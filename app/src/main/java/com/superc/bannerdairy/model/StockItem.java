package com.superc.bannerdairy.model;

import android.databinding.BaseObservable;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 我的库存实体类
 */

public class StockItem {

    /**
     * success : true
     * show : false
     * msg :
     * data : [{"category_id":"1","category_name":"奶粉","category_image":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUAAAAEsCAYAAACypmqLAAAgAElEQVR4Xuy9CZgdZ3nn+/tqO3vvrbW1S5ZseZNl4w0wxivGCcQxCZAQSIizkD1ASO6dhAnJhJC5mSeTmefOhCz3QkhuCEtYbAIBL4CNd8uyJVmy9rVb3er9bLXf5/2qTqslS7Ysy1JbXcfPcbdO16lT9VZ9//Mu//f/qjiOY7JHZoHMApkFZqEFV","data":[{"order_goods_stock_id":"6","user_id":"58364","goods_id":"1","spec_id":"2","order_goods_stock_number":"20","goods_name":"【单听】旗帜奶粉  一段","spec_name":""}]},{"category_id":"2","category_name":"菌粉","category_image":"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUAAAAEsCAYAAACypmqLAAAgAElEQVR4Xuy9CZgdZ3nn+/tqO3vvrbW1S5ZseZNl4w0wxivGCcQxCZAQSIizkD1ASO6dhAnJhJC5mSeTmefOhCz3QkhuCEtYbAIBL4CNd8uyJVmy9rVb3er9bLXf5/2qTqslS7Ysy1JbXcfPcbdO16lT9VZ9//Mu//f/qjiOY7JHZoHMApkFZqEFV","data":[{"order_goods_stock_id":"7","user_id":"58364","goods_id":"4","spec_id":"2","order_goods_stock_number":"10","goods_name":"共和国的","spec_name":""}]}]
     */

    @SerializedName("success")
    private boolean success;
    @SerializedName("show")
    private boolean show;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        /**
         * category_id : 1
         * category_name : 奶粉
         * category_image : data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUAAAAEsCAYAAACypmqLAAAgAElEQVR4Xuy9CZgdZ3nn+/tqO3vvrbW1S5ZseZNl4w0wxivGCcQxCZAQSIizkD1ASO6dhAnJhJC5mSeTmefOhCz3QkhuCEtYbAIBL4CNd8uyJVmy9rVb3er9bLXf5/2qTqslS7Ysy1JbXcfPcbdO16lT9VZ9//Mu//f/qjiOY7JHZoHMApkFZqEFV
         * data : [{"order_goods_stock_id":"6","user_id":"58364","goods_id":"1","spec_id":"2","order_goods_stock_number":"20","goods_name":"【单听】旗帜奶粉  一段","spec_name":""}]
         */

        @SerializedName("category_id")
        private String categoryId;
        @SerializedName("category_name")
        private String categoryName;
        @SerializedName("category_image")
        private String categoryImage;
        @SerializedName("data")
        private List<DataBean> data;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean extends BaseObservable implements Serializable{
            /**
             * order_goods_stock_id : 6
             * user_id : 58364
             * goods_id : 1
             * spec_id : 2
             * order_goods_stock_number : 20
             * goods_name : 【单听】旗帜奶粉  一段
             * spec_name :
             */

            @SerializedName("order_goods_stock_id")
            private String orderGoodsStockId;
            @SerializedName("user_id")
            private String userId;
            @SerializedName("goods_id")
            private String goodsId;
            @SerializedName("spec_id")
            private String specId;
            @SerializedName("order_goods_stock_number")
            private String orderGoodsStockNumber;
            @SerializedName("goods_name")
            private String goodsName;
            @SerializedName("spec_name")
            private String specName;
            @SerializedName("goods_cover_image")
            private String goodsCoverImage;


            private boolean isCheck;
            private String num;/*显示后面的进行加减的数值的字段*/
            private String stockNum;/*显示库存的字段*/

            public String getStockNum() {
                return stockNum;
            }

            public void setStockNum(String stockNum) {
                this.stockNum = stockNum;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
                notifyPropertyChanged(BR.num);
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public String getGoodsCoverImage() {
                return goodsCoverImage;
            }

            public void setGoodsCoverImage(String goodsCoverImage) {
                this.goodsCoverImage = goodsCoverImage;
            }
            public String getOrderGoodsStockId() {
                return orderGoodsStockId;
            }

            public void setOrderGoodsStockId(String orderGoodsStockId) {
                this.orderGoodsStockId = orderGoodsStockId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getSpecId() {
                return specId;
            }

            public void setSpecId(String specId) {
                this.specId = specId;
            }

            public String getOrderGoodsStockNumber() {
                return orderGoodsStockNumber;
            }

            public void setOrderGoodsStockNumber(String orderGoodsStockNumber) {
                this.orderGoodsStockNumber = orderGoodsStockNumber;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getSpecName() {
                return specName;
            }

            public void setSpecName(String specName) {
                this.specName = specName;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "orderGoodsStockId='" + orderGoodsStockId + '\'' +
                        ", userId='" + userId + '\'' +
                        ", goodsId='" + goodsId + '\'' +
                        ", specId='" + specId + '\'' +
                        ", orderGoodsStockNumber='" + orderGoodsStockNumber + '\'' +
                        ", goodsName='" + goodsName + '\'' +
                        ", specName='" + specName + '\'' +
                        ", goodsCoverImage='" + goodsCoverImage + '\'' +
                        ", isCheck=" + isCheck +
                        ", num='" + num + '\'' +
                        ", stockNum='" + stockNum + '\'' +
                        '}';
            }
        }
    }
}
