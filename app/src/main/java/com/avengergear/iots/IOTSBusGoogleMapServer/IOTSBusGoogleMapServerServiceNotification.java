package com.avengergear.iots.IOTSBusGoogleMapServer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class IOTSBusGoogleMapServerServiceNotification extends Notification {

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public IOTSBusGoogleMapServerServiceNotification(Context ctx) {
        super();
        this.ctx = ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "Shortcuts";
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);
        @SuppressWarnings("deprecation")
        Notification notification = builder.getNotification();
        notification.when = when;
        notification.tickerText = tickerText;
        notification.icon = R.drawable.ic_launcher;

        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.messageview);

        //set the button listeners
        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        mNotificationManager.notify(548853, notification);
    }

    public void setListeners(RemoteViews view) {
        //radio listener
        Intent radio = new Intent(ctx, IOTSBusGoogleMapServerMainActivity.class);
        radio.putExtra("DO", "radio");
        PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, radio, 0);
        view.setOnClickPendingIntent(R.id.btn1, pRadio);

        //volume listener
        Intent volume = new Intent(ctx, IOTSBusGoogleMapServerMainActivity.class);
        volume.putExtra("DO", "volume");
        PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, volume, 0);
        view.setOnClickPendingIntent(R.id.btn1, pVolume);

        //reboot listener
        Intent reboot = new Intent(ctx, IOTSBusGoogleMapServerMainActivity.class);
        reboot.putExtra("DO", "reboot");
        PendingIntent pReboot = PendingIntent.getActivity(ctx, 5, reboot, 0);
        view.setOnClickPendingIntent(R.id.btn1, pReboot);

        //top listener
        Intent top = new Intent(ctx, IOTSBusGoogleMapServerMainActivity.class);
        top.putExtra("DO", "top");
        PendingIntent pTop = PendingIntent.getActivity(ctx, 3, top, 0);
        view.setOnClickPendingIntent(R.id.btn1, pTop);

        //app listener
        Intent app = new Intent(ctx, null);
        app.putExtra("DO", "app");
        PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
        view.setOnClickPendingIntent(R.id.btn1, pApp);
    }

}
