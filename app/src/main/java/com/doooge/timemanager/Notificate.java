package com.doooge.timemanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Notificate extends Service {

    static Timer timer = null;

    // 清除通知
//    public static void cleanAllNotification() {
//        NotificationManager mn = (NotificationManager) MainActivity
//                .getContext().getSystemService(NOTIFICATION_SERVICE);
//        mn.cancelAll();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }



    public void onCreate() {
        Log.e("addNotification", "===========create=======");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
        System.out.println("!!!!!!rusn!!!");
        long period = 24 * 60 * 60 * 1000; // 24小时一个周期
        int delay = intent.getIntExtra("delayTime", 0);
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("!!!!!!run!!!");
                String ID ="channel";//channel的id
                int importance = NotificationManager.IMPORTANCE_LOW;//channel的重要性
                NotificationChannel channel = new NotificationChannel(ID, "channel", importance);//生成channel
                NotificationManager mn = (NotificationManager) Notificate.this
                        .getSystemService(NOTIFICATION_SERVICE);
                mn.createNotificationChannel(channel);//添加channel

                Intent notificationIntent = new Intent(Notificate.this,
                        MainPageSlidesAdapter.class);// 点击跳转位置
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(
                        Notificate.this, 0, notificationIntent, 0);



                Notification builder = new Notification.Builder(Notificate.this,ID)

                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.ic_launcher)// 设置通知图标
                        .setContentText("After 10 minutes: "+intent.getStringExtra("contentText")) // 下拉通知啦内容
                        .setContentTitle(intent.getStringExtra("contentTitle"))// 下拉通知栏标题
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)// 点击弹出的通知后,让通知将自动取消
                .build();
                if(intent.getStringExtra("cancel").equals("true")){
                    mn.cancel(intent.getIntExtra("id",0));
                }else {

                    mn.notify(intent.getIntExtra("id",0),builder);
                }

            }
        }, delay, period);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
