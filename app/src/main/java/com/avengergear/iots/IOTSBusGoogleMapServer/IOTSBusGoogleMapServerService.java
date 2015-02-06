package com.avengergear.iots.IOTSBusGoogleMapServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.avengergear.iots.IOTSAndroidClientLibrary.IOTS;
import com.avengergear.iots.IOTSAndroidClientLibrary.IOTSException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class IOTSBusGoogleMapServerService extends Service {
    public static final String TAG = IOTSBusGoogleMapServerService.class.getName();

    private IOTS iotsClient;

    private OutputStream outputStream;

    //private Timer publishingTimer;
    BackGroundTimerManager backGroundTimerManager;

    private void socketHandlerStart() {
        ServerServiceSocket serverServiceSocket = new ServerServiceSocket();
        serverServiceSocket.completion = new ServerServiceSocketOnComplete() {
            @Override
            public void onComplete(Socket socket, JSONObject message) {

                packetHandler(message);

                try {
                    outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print("OK");
                    printStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //message += "Something wrong! " + e.toString() + "\n";
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                socketHandlerStart();
            }
        };
        serverServiceSocket.execute();
    }

    @Override
    public void onCreate() {
        try {
            iotsClient = new IOTS(getBaseContext(), "bd365ca0-a5ce-11e4-a612-f384a4355284", "432948f145287eb92ed9e251fcf9cf4f8c9c57496ad3d4ed774d54b7d3e3b03d", "tcp://192.168.2.1:1883");
            iotsClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOTSException e) {
            e.printStackTrace();
        }

        socketHandlerStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 6000;
        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                Log.d(TAG, "I'm waiting here: " + serverSocket.getLocalPort());

                while (true) {
                    Socket socket = serverSocket.accept();

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    jsonMessage = in.readLine();

                    //IOTSBusGoogleMapServerServiceHandler messageHandler = new IOTSBusGoogleMapServerServiceHandler(getBaseContext(), socket, jsonMessage);
                    //messageHandler.execute();
                    IOTSBusGoogleMapServerServiceHandler replyThread = new IOTSBusGoogleMapServerServiceHandler(getBaseContext(), socket, jsonMessage);
                    replyThread.run();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }*/

    void packetHandler(JSONObject message) {
        Log.d(TAG, "packetHandler");
        int message_type = -1;
        String Data = "";
        String ClientTopicID = "";
        int RefreshFreq = 0;

        try {
            message_type = Integer.valueOf(message.getString("com.avengergear.iots.IOTSBusGoogleMapClient.PacketType"));
            Data = message.getString("com.avengergear.iots.IOTSBusGoogleMapClient.Data");
            ClientTopicID = message.getString("com.avengergear.iots.IOTSBusGoogleMapClient.ClientTopicID");
            RefreshFreq = Integer.valueOf(message.getString("com.avengergear.iots.IOTSBusGoogleMapClient.RefreshFreq"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (message_type) {
            /* Subscribe */
            case EnumType.SUBSCRIBE:
                Timer subscribeTimer = backGroundTimerManager.getTimer(Data);
                subscribeTimer.schedule(new BackgroundTask(ClientTopicID), 0, RefreshFreq * 1000);
                break;
            /* UnSubscribe */
            case EnumType.UNSUBSCRIBE:
                Timer unsubscribetimer = backGroundTimerManager.getTimer(Data);
                if (unsubscribetimer != null) {
                    unsubscribetimer.cancel();
                    unsubscribetimer.purge();
                }
                backGroundTimerManager.removeTimer(Data);
                break;
            default:
                break;
        }
    }

    class BackgroundTask extends TimerTask {
        String mClientTopicID;

        public BackgroundTask(String clientTopicID) {
            this.mClientTopicID = clientTopicID;
        }

        @Override
        public void run() {
            iotsClient.publish(mClientTopicID, mClientTopicID);
        }
    }
}
