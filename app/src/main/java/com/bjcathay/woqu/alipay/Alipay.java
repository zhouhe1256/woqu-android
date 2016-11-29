
package com.bjcathay.woqu.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.PayModel;
import com.bjcathay.woqu.util.DialogUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by dengt on 15-5-29.
 */
public class Alipay {
    /*
     * 合作者身份(PID):2088812280316304 安全校验码(Key):默认加密：
     * xg9cpc2dkfi82kmxogh6blf5w8yr6lex
     * http://api.7tiegolf.com/api/alipay_notify_url
     */
    public interface PaySucessOrNot {
        void payStatus(String status, boolean isPay);
    }

    // 商户PID
    public static final String PARTNER = "2088812280316304";
    // 商户收款账号
    public static final String SELLER = "chenrg@bjcathay.com";
    // 商户私钥，pkcs8格式
    public static final String private_key = "qwe";
    public static String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOMO0BFTNip2aWj2\n"
            +
            "+X0mZ8wPQQQPIyQIBtZX1tH3ZN25M2wPISUTyQ97FDKd62mshudPTVIEZqTOkXap\n" +
            "nVAxulutyd+GsdSGVGxgaJmZAI/OU3Tm7hoDeegRDxzvykCRzV/wDro0ZFgD+o8D\n" +
            "a0F0PpLyS5vNKZ5qaAfRqs9zTbFxAgMBAAECgYEApVN4ztSfAQYdNI94E9cuBtgo\n" +
            "h/MZrDen+tQUl+eh6wvZ1Fmj0aJ5aKs+hqiT6+ryg2QrsYeA0YmTQyq3X9gFjzIo\n" +
            "A5h4QHKgduCpxQ7/SJKK223wpCvLuQCBjZH/KhkDudx8gkSMZ89O4/v2xKbxVJx2\n" +
            "KGpwbk9W5G6ynWgZWb0CQQD78NIZNGXnGlpRRhJQorwaTwwUgwcKDZoy6vKnjLTw\n" +
            "I9oBEywAw25D+ydFEofrVSZu1PLlLBRudkSo01p7SmJ/AkEA5rdbnF4tdmbSC96g\n" +
            "4QzvMstgh3Y0kkJ7U8raQj/aDWMZ1TgbzbziBru+W6h9YE0DDfoaF/kzRQ3Q14Gt\n" +
            "9cAUDwJBAK4QWSf+0rTTuTjTv82kUW+f04nMaS2h7jplpxbpmQ0cx/7cHxe77k/b\n" +
            "kuse/XczEmWajOKXDNqwrYky1R2/a8MCQAe9yUYi5tWdN5kMXEuQ2onSxydkcPkj\n" +
            "X+381Xv5loByfdBotI4fs1nUfNuoeKR9kQesB6ocQ3siroo8oLCpREMCQQDkkZ3Z\n" +
            "KK5TGZ3A02ckQnn5KQnMtRBM+jyzj/W3kQ9QfVdjQ4gzFJReB18hCZrNZSYqaTqk\n" +
            "bnDkbgqciYGUnqEt\n";

    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Activity activity;
    private String orderNo;
    private long consigneeId;
    private long giftId;
    private PaySucessOrNot paySucessOrNot;

    public Alipay(Activity activity, long consigneeId, long giftId, PaySucessOrNot paySucessOrNot) {
        this.activity = activity;
        this.consigneeId = consigneeId;
        this.giftId = giftId;
        this.paySucessOrNot = paySucessOrNot;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        /*
                         * Toast.makeText(activity, "支付成功",
                         * Toast.LENGTH_SHORT).show();
                         */
                        paySucessOrNot.payStatus("sucess", true);
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            /*
                             * Toast.makeText(activity, "支付结果确认中",
                             * Toast.LENGTH_SHORT).show();
                             */
                            paySucessOrNot.payStatus("process", true);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            /*
                             * Toast.makeText(activity, "支付失败",
                             * Toast.LENGTH_SHORT).show();
                             */
                            paySucessOrNot.payStatus("fail", true);
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(activity, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void pay() {
        PayModel.pay(giftId, consigneeId, "alipay").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                if (jsonObject.optBoolean("success")) {
                    final String payInfo = jsonObject.optString("orderInfo");
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(activity);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(payInfo);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    String errorMessage = jsonObject.optString("message");
                    if (!errorMessage.isEmpty())
                        DialogUtil.showMessage(errorMessage);
                }
            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                DialogUtil.showMessage(activity.getString(R.string.empty_net_text));
            }
        });
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(activity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }
}
