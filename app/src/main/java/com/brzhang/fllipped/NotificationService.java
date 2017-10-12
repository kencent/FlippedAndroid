package com.brzhang.fllipped;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.brzhang.fllipped.api.FllippedService;
import com.brzhang.fllipped.api.RetrofitClient;
import com.brzhang.fllipped.model.SignResponse;
import com.google.gson.Gson;
import com.tencent.TIMCallBack;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElemType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUser;

import java.nio.charset.Charset;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationService extends Service {

    public static boolean sMsdkLogined = false;
    private static final String TAG = "NotificationService";

    public NotificationService() {
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        getSign();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getSign() {
        RetrofitClient.INSTANCE.newInstance("", "").create(FllippedService.class)
                .getLvbSig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SignResponse signResponse) {
                        loginToSdk(signResponse);
                    }
                });

    }

    private void loginToSdk(SignResponse signResponse) {
        //发起登录请求
        TIMManager.getInstance().init(this);
        TIMUser user = new TIMUser();
        user.setIdentifier(signResponse.getQcloud_uid());
        TIMManager.getInstance().login(
                1400038614,                   //sdkAppId，由腾讯分配
                user,
                signResponse.getSig(),                    //用户帐号签名，由私钥加密获得，具体请参考文档
                new TIMCallBack() {//回调接口

                    @Override
                    public void onSuccess() {//登录成功
                        Log.e(TAG, "onSuccess() called with: " + "");
                        addMessageListener();
                        sMsdkLogined = true;
                    }

                    @Override
                    public void onError(int code, String desc) {//登录失败

                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        Log.e(TAG, "login failed. code: " + code + " errmsg: " + desc);
                    }
                });
    }

    private void addMessageListener() {
        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器

            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                Log.e(TAG, "onNewMessages() called with: " + "list = [" + list + "]");
                parseNotifyMessage(list);
                return true;
            }
        });
    }

    private void parseNotifyMessage(List<TIMMessage> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(0).getElement(0).getType() == TIMElemType.Custom) {
                    String content = ((TIMCustomElem) list.get(0).getElement(0)).getDesc();
                    String ext = new String(((TIMCustomElem) list.get(0).getElement(0)).getData(), Charset.defaultCharset());
                    NotifyExtModel model = new Gson().fromJson(ext, NotifyExtModel.class);
                    if (model != null) {
                        // TODO: 2017/10/12 使用后台配置的url进行跳转
                    }
                    SomeNotification.notify(this, content, 99);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
