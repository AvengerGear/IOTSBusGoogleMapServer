package com.avengergear.iots.IOTSBusGoogleMapServer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avengergear.iots.IOTSAndroidClientLibrary.ContentType;
import com.avengergear.iots.IOTSAndroidClientLibrary.IOTS;
import com.avengergear.iots.IOTSAndroidClientLibrary.IOTSException;
import com.avengergear.iots.IOTSAndroidClientLibrary.IOTSMessageCallback;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/*public class IOTSBusGoogleMapServerServiceHandler extends AsyncTask<Void, Void, Void> {

    public static final String TAG = IOTSBusGoogleMapServerServiceHandler.class.getName();

    private Context mContext;
    private Socket mSocket;
    private JSONObject message;

    private IOTS iotsClient;

    IOTSBusGoogleMapServerServiceHandler(Context context, Socket socket, String jsonMessage) {
        mContext = context;
        mSocket = socket;
        try {
            message = new JSONObject(jsonMessage);
            Log.d(TAG, "BusNumber = " + message.getString("BusNumber") + " , RefreshFreq = " + message.getString("RefreshFreq"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        // Register to IOTS Server
        try {
            iotsClient = new IOTS(mContext, "b108bda0-a2c3-11e4-8ef2-4fd0d044cdc9", "a60b5f02a7f3cdc09ca88b8faeec94386a857f3ea54ee4fc2982c34e080f519d", "tcp://192.168.2.1:1883");
            iotsClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (IOTSException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        IOTSBusGoogleMapServerServiceReplyThread replyThread = new IOTSBusGoogleMapServerServiceReplyThread(
                mSocket, iotsClient.getEndpointTopic());
        replyThread.run();
        super.onPostExecute(result);
    }
}*/

public class ServerServiceSocket extends AsyncTask<Void, Void, Void> {

    public static final String TAG = ServerServiceSocket.class.getName();

    private static final int SocketServerPORT = 6000;

    private ServerSocket serverSocket;

    private JSONObject message;

    private Socket socket;

    public ServerServiceSocketOnComplete completion;

    ServerServiceSocket() {

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            serverSocket = new ServerSocket(SocketServerPORT);
            Log.d(TAG, "Server Listening on port " + serverSocket.getLocalPort());

            //while (true) {
            socket = serverSocket.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            message = new JSONObject(in.readLine());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        completion.onComplete(socket, message);
        super.onPostExecute(result);
    }
}

/*public class IOTSBusGoogleMapServerServiceHandler extends Thread {
        public static final String TAG = IOTSBusGoogleMapServerServiceHandler.class.getName();

        private Context mContext;
        private Socket mSocket;
        private JSONObject message;

        private IOTS iotsClient;


    IOTSBusGoogleMapServerServiceHandler(Context context, Socket socket, String jsonMessage) {
            mContext = context;
            mSocket = socket;
            try {
                message = new JSONObject(jsonMessage);
                Log.d(TAG, "BusNumber = " + message.getString("BusNumber") + " , RefreshFreq = " + message.getString("RefreshFreq"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            OutputStream outputStream;

            // Register to IOTS Server
            try {
                iotsClient = new IOTS(mContext, "b108bda0-a2c3-11e4-8ef2-4fd0d044cdc9", "a60b5f02a7f3cdc09ca88b8faeec94386a857f3ea54ee4fc2982c34e080f519d", "tcp://192.168.2.1:1883");
                iotsClient.connect();
                iotsClient.createTopic(iotsClient.getEndpointTopic() + "/" + message.getString("BusNumber"));
                iotsClient.addTopicCallback(iotsClient.getEndpointTopic(), new IOTSMessageCallback(){
                    @Override
                    public void onMessage(String topic, String threadId,
                                          String source, ContentType type, Object content,
                                          int status) {
                        Log.d("IOTSTest", "Message Received:" + content.toString());
                        //notifyUser("Avengergear IOTSClient", "Message Received", content.toString());
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (IOTSException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                outputStream = mSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(iotsClient.getEndpointTopic() + "/" + message.getString("BusNumber"));
                printStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //message += "Something wrong! " + e.toString() + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }*/
