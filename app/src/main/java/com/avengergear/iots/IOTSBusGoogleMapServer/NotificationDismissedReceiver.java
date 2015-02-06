package com.avengergear.iots.IOTSBusGoogleMapServer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /* Your code to handle the event here */
        if(intent.getAction().equals("com.avengergear.iots.IOTSBusGoogleMapServer.startService")) {
            Log.d("IOTS", "Start Service");
            Intent startIntent = new Intent(context, IOTSBusGoogleMapServerService.class);
            context.startService(startIntent);
        }
        else if(intent.getAction().equals("com.avengergear.iots.IOTSBusGoogleMapServer.dismissService")) {
            Log.d("IOTS", "Kill Service");
            Intent dismissIntent = new Intent(context, IOTSBusGoogleMapServerService.class);
            context.stopService(dismissIntent);
        }
    }

}
