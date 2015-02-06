package com.avengergear.iots.IOTSBusGoogleMapServer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.avengergear.iots.IOTSAndroidClientLibrary.IOTS;

public class IOTSBusGoogleMapServerMainActivity extends Activity
{
    /**
     * This activity loads a service.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent dismissIntent = new Intent(this, NotificationDismissedReceiver.class);
        dismissIntent.setAction("com.avengergear.iots.IOTSBusGoogleMapServer.dismissService");
        PendingIntent dismissPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(),
                        0, dismissIntent, 0);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, IOTSBusGoogleMapServerService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(IOTSBusGoogleMapServerService.class.getName())
                .setContentText("Service on background")
                .setContentIntent(contentIntent)
                .setDeleteIntent(dismissPendingIntent)
                .setAutoCancel(true);

        //notify.flags = Notification.FLAG_AUTO_CANCEL;
        //notify.setLatestEventInfo(this, IOTSBusGoogleMapServerService.class.getName(), "Service on background", contentIntent);
        mNotificationManager.notify(1883, mBuilder.build());

        Intent intent = new Intent(IOTSBusGoogleMapServerMainActivity.this, IOTSBusGoogleMapServerService.class);
        startService(intent);

        finish();
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        intent.setAction("com.avengergear.iots.IOTSBusGoogleMapServer.dismissService");

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                       0, intent, 0);

        return pendingIntent;
    }
}


