package com.notification80;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String id = "chat";
        String name = "聊天消息";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createChannel(id, name, importance);

        id = "subscribe";
        name = "订阅消息";
        importance = NotificationManager.IMPORTANCE_DEFAULT;
        createChannel(id, name, importance);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel(String id, String name, int importance){
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendChatMsg(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = notificationManager.getNotificationChannel("chat");

        if(channel.getImportance() == NotificationManager.IMPORTANCE_NONE){
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "chat");
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }

        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一条聊天消息")
                .setContentText("今天吃什么")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setNumber(2)
                .build();

        notificationManager.notify(1, notification);

    }

    public void sendSubscribeMsg(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("收到一条订阅消息")
                .setContentText("呵呵呵")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(2, notification);
    }
}

