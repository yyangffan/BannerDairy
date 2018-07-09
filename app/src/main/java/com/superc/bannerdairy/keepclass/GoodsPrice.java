package com.superc.bannerdairy.keepclass;

/**
 * Created by user on 2017/12/21.
 */

public class GoodsPrice {
    String num;
    String price;
    String gds_id;

    public GoodsPrice(String num, String price,String gds_id) {
        this.num = num;
        this.price = price;
        this.gds_id=gds_id;
    }

    public int getChildAllPrice() {
        return (int) (Integer.parseInt(num) * Double.parseDouble(price));
    }

}
