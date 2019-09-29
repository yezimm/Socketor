package com.sogo.map.socketor.library;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.sogo.map.socketor.library.SocketorConfig.SERVER_PORT;
import static com.sogo.map.socketor.library.SocketorConfig.STATUS_FAILED;
import static com.sogo.map.socketor.library.SocketorConfig.STATUS_OK;
import static com.sogo.map.socketor.library.SocketorConfig.parseIPByInt;

final public class SocketorClient extends SocketotBase {

    private static final String TAG = "SocketorClient";

    private Context context;
    private SocketorClientListener listener;
    /**
     *
     * @param context 上下文对象
     */
    SocketorClient(Context context) {
        this.context = context;
    }

    public SocketorClient setClientListener(SocketorClientListener listener) {
        this.listener = listener;
        return this;
    }

    public SocketorClient sendMessage(SocketorMessage socketorMessage) {
        final WifiManager myWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (myWifiManager == null) {
            Log.e(TAG, "Wifi Manager 获取不到");
            if (listener != null) {
                listener.connectFaild("Wifi Manager 获取不到");
            }
            return this;
        }
        ClientAsyncTask clientAST = new ClientAsyncTask(listener);
        clientAST.execute(parseIPByInt(myWifiManager.getDhcpInfo().gateway), String.valueOf(SERVER_PORT),
                socketorMessage.toJson());
        return this;
    }

    /**
     * AsyncTask which handles the communication with the server
     */
    static class ClientAsyncTask extends AsyncTask<String, Void, SocketorMessage> {

        SocketorClientListener listener;

        ClientAsyncTask(SocketorClientListener callback) {
            this.listener = callback;
        }

        @Override
        protected SocketorMessage doInBackground(String... params) {
            String result = null;
            try {

                Socket socket = new Socket(params[0],
                        Integer.parseInt(params[1]));

                InputStream is = socket.getInputStream();

                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);

                out.println(params[2]);

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));

                result = br.readLine();

                socket.close();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
                return SocketorMessage.obtain("Socket connect failed").setStatus(STATUS_FAILED);
            }
            return SocketorMessage.parseMessage(result).setStatus(STATUS_OK);
        }

        @Override
        protected void onPostExecute(SocketorMessage s) {
            if (listener != null && s != null) {
                if (s.getStatus() == STATUS_OK)
                    listener.sendSuccess(s);
                else
                    listener.connectFaild(s.getMessage());
            }
        }
    }

}
