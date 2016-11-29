
package com.bjcathay.woqu.util;

import android.content.Context;

import com.bjcathay.woqu.R;
import com.bjcathay.woqu.model.ShareModel;
import com.bjcathay.woqu.recyle.WoQuTextActivityFragment;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by dengt on 15-10-19.
 */
public class ShareUtil {
    // 调用示例 ShareUtil.showShare(context);
    public static void showShare(Context context,ShareModel shareModel,String id,String type,String giftId) {
        ShareSDK.initSDK(context);
        String result = shareModel.getResult();
        String url = null;
        String title = null;
        String content = null;
        try {
            JSONObject js = new JSONObject(result);
            url = js.getString("url");
            title = js.getString("title");
            content = js.getString("content");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(WoQuTextActivityFragment.TEST_IMAGE);// 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // onComplete、onError和onCancel等方法。启动快捷分享的时候将OneKeyShareCallback的类名传递进去，
        // 快捷分享自己会尝试创建其实例，如果创建失败或者没有传递callback字段，
        // 则使用默认的callback，如果成功，则以后会将分享结果给这个类处理。
        oks.setCallback(new OneKeyShareCallbackImpl(context,id,type,giftId));
        // 为不同的平台添加不同分享内容的方法
         oks.setShareContentCustomizeCallback(new
        ShareContentCustomizeImplement(context,"woqu",content+""+url));
        // 启动分享GUI
        oks.show(context);



        // oks.setImagePath(shareModel.getPicture());
        // oks.setImageUrl(shareModel.getImageUrl());

    }
}
