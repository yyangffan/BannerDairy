package com.superc.bannerdairy.constant;

import java.util.HashMap;

/**
 * 创建日期：2017/10/30 on 16:58
 * 描述：运行数据临时储存
 * 作者：郭士超
 * QQ：1169380200
 */

public class RunTime {
    //存的首页传播专区文章id
    public static final int KNOWLEDGEID= 10000;
    //区分传播文章和买家秀文章
    public static final int FINSHION= 10001;
    //首页宝贝列表详情
    public static final int ALLGOODS= 10002;
    //立即下单的商品
    public static final int FIRMGOODS= 10003;
    // 数据的key
    public static final int ZZZ_ID = 10000;
    public static final int NOKNOW = 10004;

    // 用来保存运行时临时数据
    private static HashMap<Integer, Object> sHashMap = new HashMap<>();

    public static Object getRunTime(Integer key) {
        return sHashMap.get(key);
    }

    public static void setData(Integer key, Object valus) {
        sHashMap.put(key, valus);
    }


}
