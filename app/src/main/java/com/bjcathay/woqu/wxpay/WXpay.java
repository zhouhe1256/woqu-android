
package com.bjcathay.woqu.wxpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Xml;

import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.PayModel;
import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by dengt on 15-6-4.
 */
public class WXpay {
    private Activity activity;
    private long consigneeId;
    private long giftId;

    PayReq req = new PayReq();
    final IWXAPI msgApi;

    public WXpay(Activity activity, long consigneeId, long id) {
        this.activity = activity;
        this.consigneeId = consigneeId;
        this.giftId = id;
        msgApi = WXAPIFactory.createWXAPI(activity, Constants.APP_ID);
        msgApi.registerApp(Constants.APP_ID);
    }

    private static final String TAG = "MicroMsg.SDKSample.PayActivity";

    public void wxpay() {
        if (!msgApi.isWXAppInstalled()) {
            DialogUtil.showMessage("尚未安装微信");
            return;
        }
        PayModel.pay(giftId, consigneeId, "wx").done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                JSONObject jsonObject = arguments.get(0);
                if (jsonObject.optBoolean("success")) {
                    Map<String, String> xml = decodeXml(jsonObject.optString("orderInfo"));
                    req.appId = xml.get("appid");
                    req.partnerId = xml.get("partnerid");
                    req.prepayId = xml.get("prepayid");
                    req.packageValue = xml.get("package");// packageValue
                    req.nonceStr = xml.get("noncestr");
                    req.timeStamp = xml.get("timestamp");
                    req.sign = xml.get("sign");
                    sendPayReq();
                }
                else {
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

    private void sendPayReq() {
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Logger.e("orion", e.toString());
        }
        return null;

    }
}
