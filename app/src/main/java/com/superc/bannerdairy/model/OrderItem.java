package com.superc.bannerdairy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Amorr on 2017/12/5.
 */

public class OrderItem implements Parcelable{
    /**
     * order_id : 1
     * address : 上海市上海市卢湾区222-2222-1111
     * contact : 周润发
     * mobile : null
     * level : 0
     * order_total : 1318.00
     * order_discount_total : null
     * order_status : 0
     * payment_status : 0
     * delivery_status : 0
     * return_status : 0
     * express_id : null
     * express_code : null
     * order_code : 15124604706893
     * transaction_code : null 交易号
     * transaction_type : 0
     * payment_time : null
     * deliver_goods : null 发货时间
     * creator : 23
     * created : 2017-12-05 15:54:30
     * modifier : null
     * modified : 2017-12-05 15:54:30
     * delete_time : null
     * nickname : null
     * user_id : null
     * express_name : null
     * order_goods : [{"order_goods_id":"1","goods_id":"39","spec_id":"2","goods_code":null,"goods_name":"奶粉1","goods_category":"测试分类","goods_spec":"规格6","goods_cover_image":"/upload/image/image/20171128/1511840769.png","goods_original_price":"659.00","goods_sale_price_0":"263.00","goods_sale_price_1":"243.00","goods_sale_price_2":"223.00","goods_sale_price_3":"203.00","goods_sale_price_4":"183.00","goods_sale_price_5":"163.00","goods_remark":"对付对付","goods_details":"<p>大概的复合弓返回键<\/p>","order_goods_number":"2","order_goods_price":"659.00","order_goods_total":"1318.00","order_goods_discount_total":"0.00","creator":"23","created":"2017-12-05 15:54:30","modifier":null,"modified":"2017-12-05 15:54:30","delete_time":null}]
     */

    public String order_id;
    public String address;
    public String postage;
    public String contact;
    public String mobile;
    public String level;
    public String order_total;
    public Object order_discount_total;
    public String order_status;
    public String payment_status;
    public String delivery_status;
    public String return_status;
    public Object express_id;
    public Object express_code;
    public String order_code;
    public String transaction_code;
    public String transaction_type;
    public String payment_time;
    public String deliver_goods;
    public String creator;
    public String created;
    public Object modifier;
    public String modified;
    public Object delete_time;
    public Object nickname;
    public Object user_id;
    public Object express_name;
    public String order_type;
    public boolean isCheck;
    public List<OrderGoodsBean> order_goods ;
    private int flag=1;

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    protected OrderItem(Parcel in) {
        order_id = in.readString();
        address = in.readString();
        postage = in.readString();
        contact = in.readString();
        level = in.readString();
        order_total = in.readString();
        order_status = in.readString();
        payment_status = in.readString();
        delivery_status = in.readString();
        return_status = in.readString();
        order_code = in.readString();
        order_type = in.readString();
        transaction_type = in.readString();
        creator = in.readString();
        created = in.readString();
        modified = in.readString();
        if(order_goods!=null){
            in.readTypedList(order_goods, OrderGoodsBean.CREATOR);
        }
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public Object getOrder_discount_total() {
        return order_discount_total;
    }

    public void setOrder_discount_total(Object order_discount_total) {
        this.order_discount_total = order_discount_total;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public Object getExpress_id() {
        return express_id;
    }

    public void setExpress_id(Object express_id) {
        this.express_id = express_id;
    }

    public Object getExpress_code() {
        return express_code;
    }

    public void setExpress_code(Object express_code) {
        this.express_code = express_code;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTransaction_code() {
        return transaction_code;
    }

    public void setTransaction_code(String transaction_code) {
        this.transaction_code = transaction_code;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getDeliver_goods() {
        return deliver_goods;
    }

    public void setDeliver_goods(String deliver_goods) {
        this.deliver_goods = deliver_goods;
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

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public Object getUser_id() {
        return user_id;
    }

    public void setUser_id(Object user_id) {
        this.user_id = user_id;
    }

    public Object getExpress_name() {
        return express_name;
    }

    public void setExpress_name(Object express_name) {
        this.express_name = express_name;
    }

    public List<OrderGoodsBean> getOrder_goods() {
        return order_goods;
    }

    public void setOrder_goods(List<OrderGoodsBean> order_goods) {
        this.order_goods = order_goods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(order_id);
        parcel.writeString(address);
        parcel.writeString(postage);
        parcel.writeString(contact);
        parcel.writeString(level);
        parcel.writeString(order_total);
        parcel.writeString(order_status);
        parcel.writeString(payment_status);
        parcel.writeString(delivery_status);
        parcel.writeString(return_status);
        parcel.writeString(order_code);
        parcel.writeString(order_type);
        parcel.writeString(transaction_type);
        parcel.writeString(creator);
        parcel.writeString(created);
        parcel.writeString(modified);
//        parcel.writeArray(new List[]{order_goods});
        parcel.writeTypedList(order_goods);

    }

    public static class OrderGoodsBean implements Parcelable {
        /**
         * order_goods_id : 1
         * goods_id : 39
         * spec_id : 2
         * goods_code : null
         * goods_name : 奶粉1
         * goods_category : 测试分类
         * goods_spec : 规格6
         * goods_cover_image : /upload/image/image/20171128/1511840769.png
         * goods_original_price : 659.00
         * goods_sale_price_0 : 263.00
         * goods_sale_price_1 : 243.00
         * goods_sale_price_2 : 223.00
         * goods_sale_price_3 : 203.00
         * goods_sale_price_4 : 183.00
         * goods_sale_price_5 : 163.00
         * goods_remark : 对付对付
         * goods_details : <p>大概的复合弓返回键</p>
         * order_goods_number : 2
         * order_goods_price : 659.00
         * order_goods_total : 1318.00
         * order_goods_discount_total : 0.00
         * creator : 23
         * created : 2017-12-05 15:54:30
         * modifier : null
         * modified : 2017-12-05 15:54:30
         * delete_time : null
         */

        public String order_goods_id;
        public String goods_id;
        public String spec_id;
        public Object goods_code;
        public String goods_name;
        public String goods_category;
        public String goods_spec;
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
        public String order_goods_number;
        public String order_goods_price;
        public String order_goods_total;
        public String order_goods_discount_total;
        public String creator;
        public String created;
        public Object modifier;
        public String modified;
        public Object delete_time;

        public OrderGoodsBean() {
        }

        protected OrderGoodsBean(Parcel in) {
            order_goods_id = in.readString();
            goods_id = in.readString();
            spec_id = in.readString();
            goods_name = in.readString();
            goods_category = in.readString();
            goods_spec = in.readString();
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
            order_goods_number = in.readString();
            order_goods_price = in.readString();
            order_goods_total = in.readString();
            order_goods_discount_total = in.readString();
            creator = in.readString();
            created = in.readString();
            modified = in.readString();
        }

        public static final Creator<OrderGoodsBean> CREATOR = new Creator<OrderGoodsBean>() {
            @Override
            public OrderGoodsBean createFromParcel(Parcel in) {
                return new OrderGoodsBean(in);
            }

            @Override
            public OrderGoodsBean[] newArray(int size) {
                return new OrderGoodsBean[size];
            }
        };

        public String getOrder_goods_id() {
            return order_goods_id;
        }

        public void setOrder_goods_id(String order_goods_id) {
            this.order_goods_id = order_goods_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getSpec_id() {
            return spec_id;
        }

        public void setSpec_id(String spec_id) {
            this.spec_id = spec_id;
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

        public String getGoods_spec() {
            return goods_spec;
        }

        public void setGoods_spec(String goods_spec) {
            this.goods_spec = goods_spec;
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

        public String getOrder_goods_number() {
            return order_goods_number;
        }

        public void setOrder_goods_number(String order_goods_number) {
            this.order_goods_number = order_goods_number;
        }

        public String getOrder_goods_price() {
            return order_goods_price;
        }

        public void setOrder_goods_price(String order_goods_price) {
            this.order_goods_price = order_goods_price;
        }

        public String getOrder_goods_total() {
            return order_goods_total;
        }

        public void setOrder_goods_total(String order_goods_total) {
            this.order_goods_total = order_goods_total;
        }

        public String getOrder_goods_discount_total() {
            return order_goods_discount_total;
        }

        public void setOrder_goods_discount_total(String order_goods_discount_total) {
            this.order_goods_discount_total = order_goods_discount_total;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(order_goods_id);
            parcel.writeString(goods_id);
            parcel.writeString(spec_id);
            parcel.writeString(goods_name);
            parcel.writeString(goods_category);
            parcel.writeString(goods_spec);
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
            parcel.writeString(order_goods_number);
            parcel.writeString(order_goods_price);
            parcel.writeString(order_goods_total);
            parcel.writeString(order_goods_discount_total);
            parcel.writeString(creator);
            parcel.writeString(created);
            parcel.writeString(modified);
        }
    }
}
