package com.wwb.paotui.JPush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.payencai.library.util.LogUtil;
import com.wwb.paotui.R;
import com.wwb.paotui.activity.MainActivity;
import com.wwb.paotui.tools.NotifyUtil;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class JpushPresenter implements JpushContract {
    private Context mContext;
    public static final String TAG="JpushPresenter";
    public JpushPresenter() {
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * 接收到自定义的消息，调用自定义的通知显示出来
     */

    @Override
    public void doProcessPushMessage(Bundle bundle) {
        Notification.Builder builder =new Notification.Builder(mContext);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        NotifyUtil.showNotify(mContext,message,extras);
    }

    /**
     * 发送通知
     * @param bundle
     */
    @Override
    public void doProcessPusNotify(Bundle bundle) {
        showNotify(bundle);
    }
    /**
     * 使用Jpush内置的样式构建通知
     */

    public void showNotify(Bundle bundle){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
        builder.statusBarDrawable = R.mipmap.app_icon;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        LogUtil.e(TAG,"=====doProcessPusNotify=======");
    }
    /**
     * 点击通知之后的操作在这里
     * @param bundle
     */
    @Override
    public void doOpenPusNotify(Bundle bundle) {

        LogUtil.e(TAG,"=====doOpenPusNotify=======");
    }



}
