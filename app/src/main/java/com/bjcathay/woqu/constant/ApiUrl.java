
package com.bjcathay.woqu.constant;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.util.SystemUtil;

/**
 * Created by jiangm on 15-10-8.
 */
public class ApiUrl {

    public static final String VERSION = SystemUtil.getCurrentVersionName(WApplication
            .getInstance());
    public static final String HOST_URL = WApplication.getInstance().getResources()
            .getString(R.string.host);
    public static final String IMG_HOST_URL = WApplication.getInstance().getResources()
            .getString(R.string.img_host);

    // 192.168.1.54 192.168.1.22:8085
    public static final String OS = SystemUtil.getVersion();
    public static final String REGISTER = "/api/user/register";// 注册用户
    public static final String USER_LOGIN = "/api/user/login";// 用户登陆
    public static final String SEND_CAPTCHA = "/api/user/send_captcha";// 发送验证码
    public static final String CAPTCHA_LOGIN = "/api/user/captcha/login";// 验证码登陆
    public static final String BIND_PLATFORM = "/api/user/bind/platform";// 通过第三方平台登陆
    public static final String BIND_PHONE = "/api/user/bind/phone";// 第三方平台登陆用户绑定手机号
    public static final String CHANGE_PASSWORD = "/api/user/change_password";// 修改密码
    public static final String RESET_PASSWORD = "/api/user/reset_password";// 重置密码
    public static final String USER_INFO = "/api/user";// 获取用户信息
    public static final String USER_INFO_UPDATE = "/api/user";// 更新用户信息
    public static final String USER_SET_AVATAR = "/api/user/set_avatar";// 设置用户头像
    public static final String USER_FEEDBACK = "/api/user/feedback";// 用户反馈
    public static final String CONSIGINESS_NEW = "/api/consignees/new";// 添加收货人
    public static final String CONSIGINESS_lIST = "/api/consignees";// 收货人列表
    public static final String PROVINCE = "/api/provinces";// 获取省份
    public static final String CITIES = "/api/cities";// 获取城市
    public static final String COUNTIES = "/api/counties";// 获取区县
    public static final String UPDATE = "/api/update";// 检查更新
    public static final String MESSAGE = "/api/user/message";// 我的消息
    public static final String MESSAGE_STATE = "/api/user/message";// 消息状态改为已读
    public static final String MESSAGE_DELETE = "/api/user/message";// 删除消息
    public static final String MESSAGE_EMPTY = "/api/user/empty_message";// 清空消息
    public static final String UPLOAD_IMAGE = "/api/upload";// 上传图片
    public static final String TALKS_LIST = "/api/talks";// 晒列表
    public static final String TALKS_DETAILS = "/api/talks/:id";// 晒详情
    public static final String TALKS_DELETE = "/api/talks/:id";// 删除晒
    public static final String TALKS_LIKE_DELETE = "/api/talks/:id/like";// 取消晒点赞
    public static final String TALKS_COMMENT_DELETE = "/api/talks/:tid/comment/:cid";// 删除晒评论
    public static final String TALKS_COMMENT_ALL = "/api/talks/:id/comment";// 获取晒所有评论
    public static final String TALKS_LIKE_ALL = "/api/talks/:id/like";// 获取晒所有评论
    public static final String TALKS = "/api/talks";// 发布晒
    public static final String CAMPAIGNS = "/api/campaigns"; //获取竞选活动
    public static final String  ACTIVITYS = "/api/activities";
    public static final String MY_GIFTS = "/api/user/gifts";// 我的礼物
    public static final String MY_ORDERS="/api/user/order";//我的订单
    public static final String MY_INVOLVED="/api/user/activities";//我参与的
    public  static final String HOME_BANNER = "/api/banners"; //首页轮番(GET /api/banners)
    public  static final String SPLASH_IMG = "/api/splash"; //首页闪屏(GET /api/splash)

    //获取礼物详情(GET /api/user/gifts/:id)
    public static String getgift(Long id) {
        return "/api/user/gifts/" + id;    }
    // 修改收货人(PUT /api/consignees/:id)
    public static String modifyConsignee(Long id) {
        return "/api/consignees/" + id;
    }

    // 设置为默认收货人(PUT /api/consignees/:id/default)
    public static String defaultConsignee(Long id) {
        return "/api/consignees/" + id + "/default";
    }

    // 删除收货人(DELETE /api/consignees/:id)
    public static String deleteConsignee(Long id) {
        return "/api/consignees/" + id;
    }

    //获取活动详情
    public static String getActivitys(String id){
        return "/api/activities/"+id;
    }


    //分享活动
    public static String getShare(String id){
        return "/api/activities/"+id+"/share";
    }

    //分享竞选活动
    public static String getJingXuanShare(String id){
        return "/api/campaigns/"+id+"/share";
    }


    //晒点赞
    public static String getTalksLike(String id){
        return "/api/talks/"+id+"/like";
    }

    //获取竞选活动详情
    public static String getWishDetails(String id) {
        return "/api/campaigns/" + id;
    }
    //去支付(GET /api/user/gifts/:id/pay)
    public static String pay(Long id){
        return "/api/user/gifts/"+id+"/pay";
    }
    //去支付(GET /api/user/gifts/:id/pay)
    public static String textpay(Long id){
        return "/api/user/gifts/"+id+"/pay";
    }

    //晒评论
    public static String talksComment(String id){
        return "/api/talks/"+id+"/comment";
    }
    //晒详情
    public static String talks(String id){
        return "/api/talks/"+id;
    }


    //我的订单详情(GET /api/user/order/:id)
    public static String getOrderDetails(Long id){
        return "/api/user/order/"+id;
    }

    //活动提醒(POST /api/activities/:id/remind)
    public static String getRemind(String id){
        return "/api/activities/"+id+"/remind";
    }

    //参加活动(POST /api/activities/:id)
    public static String getParticipate(String id){
        return "/api/activities/"+id;
    }

    //获取活动中奖名单(GET /api/activities/:id/winners)
    public static String getWinner(String id){
        return "/api/activities/"+id+"/winners";
    }
}
