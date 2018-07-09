package com.superc.bannerdairy.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.superc.bannerdairy.BR;

import java.util.List;


/**
 * Created by user on 2017/12/21.
 */

public class BaseCarGoodsItem extends BaseObservable  {


    /**
     * category_id : 1
     * category_name : 一段奶粉
     * category_image : null
     * data : [{"goods_id":"1","goods_code":"2017121404","goods_name":"旗帜奶粉1","goods_category":"1","goods_type":"1","goods_image_id":null,"goods_cover_image":"/upload/image/image/20171214/1513240627.png","goods_original_price":"260.00","goods_sale_price_0":"280.00","goods_sale_price_1":"300.00","goods_sale_price_2":"340.00","goods_sale_price_3":"380.00","goods_sale_price_4":"390.00","goods_sale_price_5":"400.00","goods_remark":"旗帜奶粉7个月宝宝食用啊","goods_details":"<p>旗帜奶粉7个月宝宝食用旗帜奶粉7个月宝宝食用旗帜奶粉7个月宝宝食用旗帜奶粉7个月宝宝食用<\/p><p style=\"text-align: center;\"><img src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/afd36c7d2e5c9683c3feeb5d0651e43a.jpg\" _src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/afd36c7d2e5c9683c3feeb5d0651e43a.jpg\"/><br/><img src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/eb28ff920bd8f4132b3865f409f6fdef.jpg\" _src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/eb28ff920bd8f4132b3865f409f6fdef.jpg\"/><br/><img src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/36c6aea31a874e65fae4c4c4a12c2fe8.jpg\" _src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/36c6aea31a874e65fae4c4c4a12c2fe8.jpg\"/><br/><img src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/d7201fa015e303ea6dffdb0411ac3255.jpg\" _src=\"http://qizhi.linghangnc.com/upload/user_ico/20171214/d7201fa015e303ea6dffdb0411ac3255.jpg\"/><\/p>","goods_status":"0","goods_recommend":"0","goods_hot":"0","goods_new":"0","creator":"23","created":"2017-12-14 16:37:07","modifier":"23","modified":"2017-12-16 18:05:47","delete_time":null,"cart_id":"28","gds_id":"1","cart_time":"1513676115","spec_name":"规格1","goods_category_id":"1","category_name":"一段奶粉","goods_number":"13","goods_sale_price":"400.00"}]
     */

    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("category_image")
    private Object categoryImage;
    @SerializedName("data")
    private List<CarGoodsItem> data;
    private boolean isChekc = false;
    private String allParentPrice;

    public String getAllParentPrice() {
        return allParentPrice;
    }

    public void setAllParentPrice(boolean isChekc) {
        int price = 0;
        for (CarGoodsItem cgi : data) {
            price += Double.parseDouble(cgi.num) * Double.parseDouble(cgi.goods_sale_price + "");
        }
        this.allParentPrice = price + "";
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

    public Object getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(Object categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<CarGoodsItem> getData() {
        return data;
    }

    public void setData(List<CarGoodsItem> data) {
        this.data = data;
    }


    public boolean isChekc() {
        return isChekc;
    }

    public void setChekc(boolean chekc) {
        isChekc = chekc;
        setAllParentPrice(chekc);
    }

    public static class CarGoodsItem extends BaseObservable implements Parcelable {


        /**
         * goods_id : 39
         * goods_code : null
         * goods_name : 奶粉1
         * goods_category : 1
         * goods_type : 8
         * goods_image_id : null
         * goods_cover_image : /upload/image/image/20171128/1511840769.png
         * goods_original_price : 156.00
         * goods_sale_price_0 : 260.00
         * goods_sale_price_1 : 240.00
         * goods_sale_price_2 : 220.00
         * goods_sale_price_3 : 200.00
         * goods_sale_price_4 : 180.00
         * goods_sale_price_5 : 160.00
         * goods_remark : 对付对付
         * goods_details : 大概的复合弓返回键
         * goods_status : 0
         * goods_recommend : 0
         * goods_hot : 0
         * goods_new : 0
         * creator : null
         * created : 1970-01-01 08:00:00
         * modifier : null
         * modified : 1970-01-01 08:00:00
         * delete_time : null
         * cart_id : 17
         * gds_id : 39
         * cart_time : 1511935054
         * spec_name : 规格2
         * goods_category_id : 1
         * category_name : 测试分类
         * goods_number : 1
         * goods_sale_price : 156.00
         */
        //新加 num allprice ischeck
        public Boolean isCheck;
        public String num;
        public String allprice;

        public String goods_id;
        public Object goods_code;
        public String goods_name;
        public String goods_category;
        public String goods_type;
        public Object goods_image_id;
        public String goods_cover_image;
        public String goods_original_price;
        public String goods_sale_price_0;
        public String goods_sale_price_1;
        public String goods_sale_price_2;
        public String goods_sale_price_3;
        public String goods_sale_price_4;
        public String goods_sale_price_5;
        public String goods_remark;
        public String goods_details;
        public String goods_status;
        public String goods_recommend;
        public String goods_hot;
        public String goods_new;
        public Object creator;
        public String created;
        public Object modifier;
        public String modified;
        public Object delete_time;
        public String cart_id;
        public String gds_id;
        public String cart_time;
        public String spec_name;
        public String goods_category_id;
        public String category_name;
        public String goods_number;
        public String goods_sale_price;

        public CarGoodsItem(Boolean isCheck, String num, String allprice, String goods_id, Object goods_code, String goods_name, String goods_category, String goods_type, Object goods_image_id, String goods_cover_image, String goods_original_price, String goods_sale_price_0, String goods_sale_price_1, String goods_sale_price_2, String goods_sale_price_3, String goods_sale_price_4, String goods_sale_price_5, String goods_remark, String goods_details, String goods_status, String goods_recommend, String goods_hot, String goods_new, Object creator, String created, Object modifier, String modified, Object delete_time, String cart_id, String gds_id, String cart_time, String spec_name, String goods_category_id, String category_name, String goods_number, String goods_sale_price) {
            this.isCheck = isCheck;
            this.num = num;
            this.allprice = allprice;
            this.goods_id = goods_id;
            this.goods_code = goods_code;
            this.goods_name = goods_name;
            this.goods_category = goods_category;
            this.goods_type = goods_type;
            this.goods_image_id = goods_image_id;
            this.goods_cover_image = goods_cover_image;
            this.goods_original_price = goods_original_price;
            this.goods_sale_price_0 = goods_sale_price_0;
            this.goods_sale_price_1 = goods_sale_price_1;
            this.goods_sale_price_2 = goods_sale_price_2;
            this.goods_sale_price_3 = goods_sale_price_3;
            this.goods_sale_price_4 = goods_sale_price_4;
            this.goods_sale_price_5 = goods_sale_price_5;
            this.goods_remark = goods_remark;
            this.goods_details = goods_details;
            this.goods_status = goods_status;
            this.goods_recommend = goods_recommend;
            this.goods_hot = goods_hot;
            this.goods_new = goods_new;
            this.creator = creator;
            this.created = created;
            this.modifier = modifier;
            this.modified = modified;
            this.delete_time = delete_time;
            this.cart_id = cart_id;
            this.gds_id = gds_id;
            this.cart_time = cart_time;
            this.spec_name = spec_name;
            this.goods_category_id = goods_category_id;
            this.category_name = category_name;
            this.goods_number = goods_number;
            this.goods_sale_price = goods_sale_price;
            setAllprice(Double.parseDouble(num + "") * Double.parseDouble(goods_original_price + "") + "");
        }

        protected CarGoodsItem(Parcel in) {
            byte tmpIsCheck = in.readByte();
            isCheck = tmpIsCheck == 0 ? null : tmpIsCheck == 1;
            num = in.readString();
            allprice = in.readString();
            goods_id = in.readString();
            goods_name = in.readString();
            goods_category = in.readString();
            goods_type = in.readString();
            goods_cover_image = in.readString();
            goods_original_price = in.readString();
            goods_sale_price_0 = in.readString();
            goods_sale_price_1 = in.readString();
            goods_sale_price_2 = in.readString();
            goods_sale_price_3 = in.readString();
            goods_sale_price_4 = in.readString();
            goods_sale_price_5 = in.readString();
            goods_remark = in.readString();
            goods_details = in.readString();
            goods_status = in.readString();
            goods_recommend = in.readString();
            goods_hot = in.readString();
            goods_new = in.readString();
            created = in.readString();
            modified = in.readString();
            cart_id = in.readString();
            gds_id = in.readString();
            cart_time = in.readString();
            spec_name = in.readString();
            goods_category_id = in.readString();
            category_name = in.readString();
            goods_number = in.readString();
            goods_sale_price = in.readString();
        }

        public static final Creator<CarGoodsItem> CREATOR = new Creator<CarGoodsItem>() {
            @Override
            public CarGoodsItem createFromParcel(Parcel in) {
                return new CarGoodsItem(in);
            }

            @Override
            public CarGoodsItem[] newArray(int size) {
                return new CarGoodsItem[size];
            }
        };

        public Boolean getCheck() {
            return isCheck;
        }

        public void setCheck(Boolean check) {
            isCheck = check;
            setAllprice("");
        }


        public CarGoodsItem() {
        }


        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public Object getGoods_code() {
            return goods_code;
        }

        public void setGoods_code(Object goods_code) {
            this.goods_code = goods_code;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_category() {
            return goods_category;
        }

        public void setGoods_category(String goods_category) {
            this.goods_category = goods_category;
        }

        public String getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(String goods_type) {
            this.goods_type = goods_type;
        }

        public Object getGoods_image_id() {
            return goods_image_id;
        }

        public void setGoods_image_id(Object goods_image_id) {
            this.goods_image_id = goods_image_id;
        }

        public String getGoods_cover_image() {
            return goods_cover_image;
        }

        public void setGoods_cover_image(String goods_cover_image) {
            this.goods_cover_image = goods_cover_image;
        }

        public String getGoods_original_price() {
            return goods_original_price;
        }

        public void setGoods_original_price(String goods_original_price) {
            this.goods_original_price = goods_original_price;
        }

        public String getGoods_sale_price_0() {
            return goods_sale_price_0;
        }

        public void setGoods_sale_price_0(String goods_sale_price_0) {
            this.goods_sale_price_0 = goods_sale_price_0;
        }

        public String getGoods_sale_price_1() {
            return goods_sale_price_1;
        }

        public void setGoods_sale_price_1(String goods_sale_price_1) {
            this.goods_sale_price_1 = goods_sale_price_1;
        }

        public String getGoods_sale_price_2() {
            return goods_sale_price_2;
        }

        public void setGoods_sale_price_2(String goods_sale_price_2) {
            this.goods_sale_price_2 = goods_sale_price_2;
        }

        public String getGoods_sale_price_3() {
            return goods_sale_price_3;
        }

        public void setGoods_sale_price_3(String goods_sale_price_3) {
            this.goods_sale_price_3 = goods_sale_price_3;
        }

        public String getGoods_sale_price_4() {
            return goods_sale_price_4;
        }

        public void setGoods_sale_price_4(String goods_sale_price_4) {
            this.goods_sale_price_4 = goods_sale_price_4;
        }

        public String getGoods_sale_price_5() {
            return goods_sale_price_5;
        }

        public void setGoods_sale_price_5(String goods_sale_price_5) {
            this.goods_sale_price_5 = goods_sale_price_5;
        }

        public String getGoods_remark() {
            return goods_remark;
        }

        public void setGoods_remark(String goods_remark) {
            this.goods_remark = goods_remark;
        }

        public String getGoods_details() {
            return goods_details;
        }

        public void setGoods_details(String goods_details) {
            this.goods_details = goods_details;
        }

        public String getGoods_status() {
            return goods_status;
        }

        public void setGoods_status(String goods_status) {
            this.goods_status = goods_status;
        }

        public String getGoods_recommend() {
            return goods_recommend;
        }

        public void setGoods_recommend(String goods_recommend) {
            this.goods_recommend = goods_recommend;
        }

        public String getGoods_hot() {
            return goods_hot;
        }

        public void setGoods_hot(String goods_hot) {
            this.goods_hot = goods_hot;
        }

        public String getGoods_new() {
            return goods_new;
        }

        public void setGoods_new(String goods_new) {
            this.goods_new = goods_new;
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

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getGds_id() {
            return gds_id;
        }

        public void setGds_id(String gds_id) {
            this.gds_id = gds_id;
        }

        public String getCart_time() {
            return cart_time;
        }

        public void setCart_time(String cart_time) {
            this.cart_time = cart_time;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public String getGoods_category_id() {
            return goods_category_id;
        }

        public void setGoods_category_id(String goods_category_id) {
            this.goods_category_id = goods_category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(String goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_sale_price() {
            return "￥"+goods_sale_price;
        }

        public void setGoods_sale_price(String goods_sale_price) {
            this.goods_sale_price = goods_sale_price;
        }

        @Bindable
        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
            setAllprice(Double.parseDouble(num) * Double.parseDouble(goods_sale_price + "") + "");
            notifyPropertyChanged(BR.num);

        }

        @Bindable
        public String getAllprice() {
            return allprice;
        }

        public void setAllprice(String allprice) {
            this.allprice = (Double.parseDouble(num) * Double.parseDouble(goods_sale_price + "") + "");
            notifyPropertyChanged(BR.allprice);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte((byte) (isCheck == null ? 0 : isCheck ? 1 : 2));
            parcel.writeString(num);
            parcel.writeString(allprice);
            parcel.writeString(goods_id);
            parcel.writeString(goods_name);
            parcel.writeString(goods_category);
            parcel.writeString(goods_type);
            parcel.writeString(goods_cover_image);
            parcel.writeString(goods_original_price);
            parcel.writeString(goods_sale_price_0);
            parcel.writeString(goods_sale_price_1);
            parcel.writeString(goods_sale_price_2);
            parcel.writeString(goods_sale_price_3);
            parcel.writeString(goods_sale_price_4);
            parcel.writeString(goods_sale_price_5);
            parcel.writeString(goods_remark);
            parcel.writeString(goods_details);
            parcel.writeString(goods_status);
            parcel.writeString(goods_recommend);
            parcel.writeString(goods_hot);
            parcel.writeString(goods_new);
            parcel.writeString(created);
            parcel.writeString(modified);
            parcel.writeString(cart_id);
            parcel.writeString(gds_id);
            parcel.writeString(cart_time);
            parcel.writeString(spec_name);
            parcel.writeString(goods_category_id);
            parcel.writeString(category_name);
            parcel.writeString(goods_number);
            parcel.writeString(goods_sale_price);
        }
    }


}
