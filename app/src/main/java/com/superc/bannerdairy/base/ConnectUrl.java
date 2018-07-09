package com.superc.bannerdairy.base;

/**
 * Created by Amorr on 2017/11/21.
 */

public class ConnectUrl {
//    public static String REQUESTURL = "http://qizhi.linghangnc.com";//测试地址
    public static String REQUESTURL = "http://www.qizhinaifen.com";//正式地址
//    public static String REQUESTURL = "http://qizhitest.linghangnc.com";/*另一个测试地址*/

    public static String LOGIN_MIMA = REQUESTURL + "/admin/App_Login/applogin"; // 密码登陆
    public static String SENDCODE = REQUESTURL + "/admin/App_Login/authCode"; // 发送验证码（product   验证码类型1：注册，2：支付）
    public static String FORGET_MIMA = REQUESTURL + "/admin/App_Login/changePassword"; // 忘记密码


    public static String LIST_GOODS = REQUESTURL + "/admin/Goods/get"; // 宝贝推荐接口
    public static String LIST_ARTICLE = REQUESTURL + "/admin/Article/get"; // 文章列表接口
    public static String ZAN = REQUESTURL + "/admin/Article_Comment/post"; // 文章点赞接口（article_comment_type 0（收藏）1(点赞)）
    public static String DELECTZAN = REQUESTURL + "/admin/Article_Comment/delete"; // 文章取消点赞接口（article_comment_type 0（收藏）1(点赞)）
    public static String USERINFO = REQUESTURL + "/admin/Users/put"; // 修改个人资料接口
    public static String LOOKUSERINFO = REQUESTURL + "/admin/Users/get"; // 查看个人资料接口
    public static String GETADDRESS = REQUESTURL + "/admin/User_Address/get"; // 获取收货地址接口
    public static String FIXADDRESS = REQUESTURL + "/admin/User_Address/put"; // 编辑收货地址接口
    public static String DELETEADDRESS = REQUESTURL + "/admin/User_Address/delete"; //  删除收货地址接口
    public static String SETADDRESS = REQUESTURL + "/admin/User_Address/defaul"; //  设置默认收货地址接口

    public static String POSTADDRESS = REQUESTURL + "/admin/User_Address/post"; // 添加收货地址接口
    public static String SYSTEM = REQUESTURL + "/admin/System/get"; // 系统公告查询接口
    public static String FIXSYSTEM = REQUESTURL + "/admin/System/put"; // 系统公告修改接口
    public static String ADDCART = REQUESTURL + "/admin/Cart/post"; // 添加购物车接口
    public static String LISTCAR = REQUESTURL + "/admin/Cart/get"; // 购物车列表接口
    public static String FIRMORDER = REQUESTURL + "/admin/Order/post"; // l立即下单接口
    public static String RELEASE = REQUESTURL + "/admin/Article/appPost"; // 发布买家秀接口
    public static String DELECTCAR = REQUESTURL + "/admin/Cart/delete"; // 删除购物车接口
    public static String ORDERPRICE = REQUESTURL + "/admin/Pay/order_price"; // 获取支付价格接口
    public static String BALANCE = REQUESTURL + "/admin/Pay/balance"; // 余额支付接口
    public static String ORDERLIST = REQUESTURL + "/admin/Order/get"; // 获取我的订单列表接口
    public static String NOTICELIST = REQUESTURL + "/admin/Notice/get"; // 获取我的消息列表接口
    public static String DELETEONENOTICE = REQUESTURL + "/admin/Notice/delete"; //删除某一条消息
    public static String DELETEALLNOTICE = REQUESTURL + "/admin/Notice/moredelete"; //删除所有的消息
    public static String READALLNOTICE = REQUESTURL + "/admin/Notice/moreput"; //已读所有的消息
    public static String REGINSERORDER = REQUESTURL + "/admin/RegiserPay/RegisterOrder"; //  注册下单接口

    public static String GETLOWEL = REQUESTURL + "/admin/Users/myTeamSubordinate"; // 获取下级推广列表接口
    public static String GETFLAT = REQUESTURL + "/admin/Users/myTeamSameLevel"; // 获取平级推广列表接口
    public static String GETLOWELLIST = REQUESTURL + "/admin/Users/subordinateList"; // 获取下级推广，每个等级下的列表接口
    public static String GETTEAMUSER = REQUESTURL + "/admin/Users/userInfo"; // 获取我的团队 用户信息接口
    public static String DIFFERENCEPRICE = REQUESTURL + "/admin/Users/differencePriceList"; // 获取我的团队 上个月差价收益接口
    public static String TEAMPERFORMANCE = REQUESTURL + "/admin/Users/teamPerformance"; // 获取我的团队 团队业绩接口
    public static String INVITE = REQUESTURL + "/admin/Users/myInvite"; // 获取我的邀请
    public static String BANNER = REQUESTURL + "/admin/Banner/get"; // 轮播图
    public static String WELCOME = REQUESTURL + "/admin/Welcome/get"; // 启动页图片

    public static String MYCOLLECT = REQUESTURL + "/admin/Goods_Collect/get"; //收藏商品列表
    public static String WENZHANGCOL = REQUESTURL + "/admin/Article/collect"; //文章收藏列表
    public static String GOODSCANCLECOLLECT = REQUESTURL + "/admin/Goods_Collect/delete"; //取消收藏商品列表
    public static String COOLECTGOOD = REQUESTURL + "/admin/Goods_Collect/post"; //收藏商品
    public static String COOLECTDELTE = REQUESTURL + "/admin/Goods_Collect/delete"; //取消收藏商品
    public static String SUBORINATE = REQUESTURL + "/admin/Users/subordinateSameCount"; //我的团队标题
    public static String INVITEAGENT = REQUESTURL + "/admin/Users/invitingAgent"; //我的邀请链接
    public static String CARTDELETE = REQUESTURL + "/admin/Cart/delete"; //删除购物车
    public static String SETPAYWORD = REQUESTURL + "/admin/Pay/pay_password"; //设置支付密码
    public static String COLUMNLIST = REQUESTURL + "/admin/menu?act=app"; //首页栏目
    public static String WXAPPAY = REQUESTURL + "/admin/Pay/WxAppPay"; //微信支付
    public static String ALIPAY = REQUESTURL + "/admin/Pay/alipayAppPay"; //支付宝支付
    public static String LIAOTIAN = REQUESTURL + "/index/im?user_id="; //客服
    public static String USERMONEYRECORD = REQUESTURL + "/admin/User_Money/get"; //交易记录
    public static String WXRECHARGE = REQUESTURL + "/admin/Pay/WxRecharge"; //微信在线充值
    public static String ALIRECHARGE = REQUESTURL + "/admin/Pay_Return/aliRecharge"; //支付宝在线充值
    public static String ZHIFUBAOTIXIAN = REQUESTURL + "/admin/Pay_Return/aliPay"; //支付宝提现
    public static String SHOPADDORCAN = REQUESTURL + "/admin/Cart/put"; //购物车那里的加减
    public static String STOCKLIST = REQUESTURL + "/admin/Order_Goods_Stock/get"; //我的库存列表展示
    public static String ORDERSTOCK = REQUESTURL + "/admin/Order_Goods_Stock/post"; //库存的确认订单
    public static String ORDERPAY = REQUESTURL + "/admin/Order_Goods_Stock/stock_pay"; //库存支付邮费
    public static String RETURNORDER = REQUESTURL + "/admin/Order_Import/returnOrder"; //取消订单
    public static String ZFBBINDING = REQUESTURL + "/admin/Login/aliBinDing"; //绑定支付宝
    public static String ISBANGDING = REQUESTURL + "/admin/Pay_Return/bangDing"; //验证是否绑定支付宝
    public static String WXBINDING = REQUESTURL + "/admin/Login/wxBinDing"; //绑定微信
    public static String ISZKC = REQUESTURL + "/admin/Invitation_Set/invenTory"; //判断是否转库存接口
    public static String KUCUNLIUSHUI = REQUESTURL + "/admin/Order_Goods_Stock/InvenToryFlow"; //库存流水
    public static String UNREADMSG = REQUESTURL + "/admin/Notice/unRead"; //未读消息数量
    public static String CHANGEPWD = REQUESTURL + "/admin/App_Login/changePasd"; //修改密码
    public static String UPDATEAPP = REQUESTURL + "/Admin/System/version"; //升级app
}
