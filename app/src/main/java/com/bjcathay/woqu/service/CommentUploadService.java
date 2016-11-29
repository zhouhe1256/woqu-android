package com.bjcathay.woqu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.util.Logger;
import com.bjcathay.woqu.application.WApplication;
import com.bjcathay.woqu.fragment.SunAwardFragment;
import com.bjcathay.woqu.model.TalkListModel;
import com.bjcathay.woqu.model.TalkListImagesModel;
import com.bjcathay.woqu.util.Bimp;
import com.bjcathay.woqu.util.DialogUtil;
import com.bjcathay.woqu.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentUploadService extends Service {
    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                InitData();

            }
        }
    };
    private String phoneName;
    private String content;
    private List<String> imageIds;
    private String address;
    private String ids = "";
    private String imageId;
    private ArrayList<String> paths;
    public CommentUploadService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        content = intent.getStringExtra("content");
        address = intent.getStringExtra("address");
        paths = intent.getStringArrayListExtra("paths");
        imageId = intent.getStringExtra("imageId");
        //bmp = intent.getParcelableArrayExtra("bitmaps");
        LoadImage(paths);
    }

    private void InitData() {
        phoneName = android.os.Build.MODEL;
        for(int i=0;i<imageIds.size();i++){
            if(ids.equals("")){
                ids = imageIds.get(i);
            }else {
                ids = ids + "," + imageIds.get(i);
            }
        }
        TalkListModel.talks(content, phoneName, address, ids).done(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                ids = "";
                TalkListModel talk = arguments.get(0);
                FileUtils.deleteDir();
                Bimp.bmp.clear();
                Bimp.drr.clear();
                Bimp.max = 0;

               // DialogUtil.gravity = Gravity.TOP;
               // DialogUtil.showMessage("上传成功");
                WApplication.getInstance().send = true;
                WApplication.getInstance().send_ = false;
                Intent intent = new Intent(SunAwardFragment.ACTION_NAME);
                sendBroadcast(intent);
                WApplication.getInstance().stopService();

            }
        }).fail(new ICallback() {
            @Override
            public void call(Arguments arguments) {
                Toast.makeText(getApplicationContext(), arguments.get(0).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LoadImage(final List<String> urlList){
        if(imageId!=null){
            if(urlList.size()>0){
                imageIds = new ArrayList<String>();
                imageIds.add(imageId);
                for(int i=0;i<urlList.size();i++){
                    final int finalI = i;
                    TalkListImagesModel.setAvatar(FileUtils.decodeBitmap(urlList.get(i))).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            TalkListImagesModel talksImages = arguments.get(0);
                            imageIds.add(talksImages.getImageId());
                            if(finalI == urlList.size()-1&&talksImages.getSuccess().equals("true")){
                                Message message = handler1.obtainMessage();
                                message.what = 2;
                                handler1.sendMessage(message);
                            }
                        }
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {

                            DialogUtil.showMessage("网络异常");
                            WApplication.getInstance().send = true;
                            WApplication.getInstance().send_ = false;
                        }
                    });
                }
            }else {
                imageIds = new ArrayList<String>();
                imageIds.add(imageId);
                Message message = handler1.obtainMessage();
                message.what = 2;
                handler1.sendMessage(message);
            }
        }else {
            if (urlList.size() > 0) {
                imageIds = new ArrayList<String>();
                for (int i = 0; i < urlList.size(); i++) {
                    final int finalI = i;
                    TalkListImagesModel.setAvatar(FileUtils.decodeBitmap(urlList.get(i))).done(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {
                            TalkListImagesModel talksImages = arguments.get(0);
                            imageIds.add(talksImages.getImageId());
                            if (finalI == urlList.size() - 1 && talksImages.getSuccess().equals("true")) {
                                Message message = handler1.obtainMessage();
                                message.what = 2;
                                handler1.sendMessage(message);
                            }
                        }
                    }).fail(new ICallback() {
                        @Override
                        public void call(Arguments arguments) {

                            DialogUtil.showMessage("网络异常");
                            WApplication.getInstance().send = true;
                            WApplication.getInstance().send_ = false;
                        }
                    });
                }

            } else {

                DialogUtil.showMessage("没有照片可以上传");
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
