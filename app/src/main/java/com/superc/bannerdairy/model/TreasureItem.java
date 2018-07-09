package com.superc.bannerdairy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Amorr on 2017/11/15.
 *     //首页宝贝实体类

 */

public class TreasureItem implements Parcelable{
    private static final long serialVersionUID = -7620435178023928252L;

    /**
     * goods_id : 32
     * goods_code : null
     * goods_name : 高东
     * goods_category : 2
     * goods_type : 0
     * goods_image_id : null
     * goods_cover_image : /upload/image/20171118/1510988770.png
     * goods_original_price : 160.00
     * goods_sale_price_0 : 260.00
     * goods_sale_price_1 : 250.00
     * goods_sale_price_2 : 210.00
     * goods_sale_price_3 : 190.00
     * goods_sale_price_4 : 180.00
     * goods_sale_price_5 : 156.00
     * goods_remark : 二嘎子
     * goods_details : 什么啊
     * goods_status : 0
     * goods_recommend : 0
     * goods_hot : 0
     * goods_new : 0
     * creator : 0
     * created : 2017-11-18 09:47:38
     * modifier : 0
     * modified : 2017-11-27 03:46:59
     * delete_time : null
     * goods_attribute : [""]
     * spec : [{"spec_id":"1","spec_name":"高东他三大爷","spec_price":"2.00","spec_stock":"200","stock_warning":null},{"spec_id":"2","spec_name":"高东她二媳妇","spec_price":"3.00","spec_stock":"444","stock_warning":null}]
     * images : [{"goods_images_id":"1","goods_image":"/upload/png/image/20171120/1511141004.png","goods_image_name":null,"goods_image_remark":null},{"goods_images_id":"2","goods_image":"/upload/png/image/20171120/1511141004.png","goods_image_name":null,"goods_image_remark":null},{"goods_images_id":"3","goods_image":"/upload/png/image/20171120/1511141004.png","goods_image_name":null,"goods_image_remark":null}]
     * attribute : [{"goods_attribute_id":"1","goods_attribute_name":"属性1","goods_attribute_content":null},{"goods_attribute_id":"2","goods_attribute_name":"属性2","goods_attribute_content":null},{"goods_attribute_id":"3","goods_attribute_name":"属性3","goods_attribute_content":null}]
     */

    public String goods_id;
    public Object goods_code;
    public String goods_name;
    public String goods_category;
    public String goods_type;
    public Object goods_image_id;
    public String goods_cover_image;

    protected TreasureItem(Parcel in) {
        goods_id = in.readString();
        goods_name = in.readString();
        goods_category = in.readString();
        goods_type = in.readString();
        goods_cover_image = in.readString();
        goods_sale_price = in.readString();
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
        creator = in.readString();
        created = in.readString();
        modifier = in.readString();
        modified = in.readString();
        goods_attribute = in.createStringArrayList();
        attribute = in.readString();
        good_scollect_type=in.readString();
    }

    public static final Creator<TreasureItem> CREATOR = new Creator<TreasureItem>() {
        @Override
        public TreasureItem createFromParcel(Parcel in) {
            return new TreasureItem(in);
        }

        @Override
        public TreasureItem[] newArray(int size) {
            return new TreasureItem[size];
        }
    };

    public String getGoods_sale_price() {
        return goods_sale_price;
    }

    public void setGoods_sale_price(String goods_sale_price) {
        this.goods_sale_price = goods_sale_price;
    }

    public String goods_sale_price;
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
    public String creator;
    public String created;
    public String modifier;
    public String modified;
    public Object delete_time;
    public List<String> goods_attribute;
    public List<SpecBean> spec;
    public List<ImagesBean> images;
    public String attribute;
    public String good_scollect_type;
    public TreasureItem() {
    }

    public TreasureItem(String goods_id, Object goods_code, String goods_name, String goods_category, String goods_type, Object goods_image_id, String goods_cover_image, String goods_original_price, String goods_sale_price_0, String goods_sale_price_1, String goods_sale_price_2, String goods_sale_price_3, String goods_sale_price_4, String goods_sale_price_5, String goods_remark, String goods_details, String goods_status, String goods_recommend, String goods_hot, String goods_new, String creator, String created, String modifier, String modified, Object delete_time, List<String> goods_attribute, List<SpecBean> spec, List<ImagesBean> images, String attribute,String good_scollect_type) {
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
        this.goods_attribute = goods_attribute;
        this.spec = spec;
        this.images = images;
        this.attribute = attribute;
        this.good_scollect_type=good_scollect_type;
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

    public String getGood_scollect_type() {
        return good_scollect_type;
    }

    public void setGood_scollect_type(String good_scollect_type) {
        this.good_scollect_type = good_scollect_type;
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

    public List<String> getGoods_attribute() {
        return goods_attribute;
    }

    public void setGoods_attribute(List<String> goods_attribute) {
        this.goods_attribute = goods_attribute;
    }

    public List<SpecBean> getSpec() {
        return spec;
    }

    public void setSpec(List<SpecBean> spec) {
        this.spec = spec;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(goods_id);
        parcel.writeString(goods_name);
        parcel.writeString(goods_category);
        parcel.writeString(goods_type);
        parcel.writeString(goods_cover_image);
        parcel.writeString(goods_sale_price);
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
        parcel.writeString(creator);
        parcel.writeString(created);
        parcel.writeString(modifier);
        parcel.writeString(modified);
        parcel.writeStringList(goods_attribute);
        parcel.writeString(attribute);
        parcel.writeString(good_scollect_type);
    }

    public static class SpecBean {
        /**
         * spec_id : 1
         * spec_name : 高东他三大爷
         * spec_price : 2.00
         * spec_stock : 200
         * stock_warning : null
         */

        public String spec_id;
        public String spec_name;
        public String spec_price;
        public String spec_stock;
        public Object stock_warning;

        public String getSpec_id() {
            return spec_id;
        }

        public void setSpec_id(String spec_id) {
            this.spec_id = spec_id;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public String getSpec_price() {
            return spec_price;
        }

        public void setSpec_price(String spec_price) {
            this.spec_price = spec_price;
        }

        public String getSpec_stock() {
            return spec_stock;
        }

        public void setSpec_stock(String spec_stock) {
            this.spec_stock = spec_stock;
        }

        public Object getStock_warning() {
            return stock_warning;
        }

        public void setStock_warning(Object stock_warning) {
            this.stock_warning = stock_warning;
        }
    }

    public static class ImagesBean {
        /**
         * goods_images_id : 1
         * goods_image : /upload/png/image/20171120/1511141004.png
         * goods_image_name : null
         * goods_image_remark : null
         */

        public String goods_images_id;
        public String goods_image;
        public Object goods_image_name;
        public Object goods_image_remark;

        public String getGoods_images_id() {
            return goods_images_id;
        }

        public void setGoods_images_id(String goods_images_id) {
            this.goods_images_id = goods_images_id;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public Object getGoods_image_name() {
            return goods_image_name;
        }

        public void setGoods_image_name(Object goods_image_name) {
            this.goods_image_name = goods_image_name;
        }

        public Object getGoods_image_remark() {
            return goods_image_remark;
        }

        public void setGoods_image_remark(Object goods_image_remark) {
            this.goods_image_remark = goods_image_remark;
        }
    }

    public static class AttributeBean {
        /**
         * goods_attribute_id : 1
         * goods_attribute_name : 属性1
         * goods_attribute_content : null
         */

        public String goods_attribute_id;
        public String goods_attribute_name;
        public Object goods_attribute_content;

        public String getGoods_attribute_id() {
            return goods_attribute_id;
        }

        public void setGoods_attribute_id(String goods_attribute_id) {
            this.goods_attribute_id = goods_attribute_id;
        }

        public String getGoods_attribute_name() {
            return goods_attribute_name;
        }

        public void setGoods_attribute_name(String goods_attribute_name) {
            this.goods_attribute_name = goods_attribute_name;
        }

        public Object getGoods_attribute_content() {
            return goods_attribute_content;
        }

        public void setGoods_attribute_content(Object goods_attribute_content) {
            this.goods_attribute_content = goods_attribute_content;
        }
    }
}
