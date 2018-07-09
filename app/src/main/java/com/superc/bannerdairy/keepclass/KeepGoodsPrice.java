package com.superc.bannerdairy.keepclass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/12/21.
 */

public class KeepGoodsPrice {
    private List<GoodsPrice> mGoodsPrices;

    private KeepGoodsPrice() {

    }

    public static KeepGoodsPrice getInstance() {
        return SingetonHolder.instance;
    }

    private static class SingetonHolder {
        private static KeepGoodsPrice instance = new KeepGoodsPrice();
    }

    public List<GoodsPrice> getGoodsPrices() {
        if(mGoodsPrices==null){
            mGoodsPrices=new ArrayList<>();
        }
        return mGoodsPrices;
    }

    public void setGoodsPrices(List<GoodsPrice> goodsPrices) {
        mGoodsPrices = goodsPrices;
    }

    /*获取所有的价格*/
    public String getAllPrice() {
        int allprice = 0;
        for (GoodsPrice gp : mGoodsPrices) {
            allprice += gp.getChildAllPrice();
        }
        return allprice + "";
    }

}
