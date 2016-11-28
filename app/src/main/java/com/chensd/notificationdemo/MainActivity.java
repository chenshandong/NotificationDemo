package com.chensd.notificationdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 最简单的notification
     */
    public void first(View v) {

        boolean b = askForPermission();
        if (!b)
            return;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("wolaile")
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("最简单的Notification")
                .setContentText("只有小图标，标题，和内容");

        managerCompat.notify(1, builder.build());
    }

    /**
     * 带有PendingIntent的Notification
     */
    public void second(View v) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 10, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("带有Action的notification")
                .setContentText("点击打开MainActivity")
                .setContentIntent(mainPendingIntent);

        NotificationManagerCompat.from(this).notify(2, builder.build());
    }

    /**
     * 设置带有标志的flag的notification
     * 并且不能被手动清除，只能通过，NotificationManager.cancle清除
     */
    public void third(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("带有flag的Notification")
                .setContentText("只能通过cancle清除我");

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        NotificationManagerCompat.from(this).notify(3, notification);
    }

    /**
     * 取消通知
     */
    public void cancle(View v) {
        NotificationManagerCompat.from(this).cancel(3);
    }

    /**
     * 连续发十个通知
     */
    public void sendTen(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        for (int i = 0; i < 10; i++) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("发送了" + i + "次")
                    .setContentText("新消息");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NotificationManagerCompat.from(this).notify(4, builder.build());
        }
    }

    /**
     * 带有remoteview ticker的notification
     */
    public void fourth(View v) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.activity_main);
        builder.setTicker("wolaile", views)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("remoteViews")
                .setContentText("带有远程视图");
        NotificationManagerCompat.from(this).notify(4, builder.build());
    }


    /**
     * 请求用户给予悬浮窗的权限
     */
    public boolean askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, //跳转到悬浮窗权限界面或者设置界面Settings.ACTION_APPLICATION_SETTINGS
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
        return true;
    }

}
