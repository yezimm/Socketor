package com.sogo.map.socketor.library;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sogo.map.socketor.library.SocketorConfig.SERVER_NAME;
import static com.sogo.map.socketor.library.SocketorConfig.SERVER_PORT;

final public class SocketorServer extends SocketotBase {

    private boolean flag = true;
    private ServerSocket socServer = null;
    private SocketorServerListener listener;

    SocketorServer() {
        try {
            socServer = new ServerSocket(SERVER_PORT);
            // 启用一个核心线程
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (flag) {
                            Socket socClient = socServer.accept();
                            ServerAsyncTask serverAsyncTask = new ServerAsyncTask(listener);
                            serverAsyncTask.execute(socClient);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketorServer setServerListener(SocketorServerListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * AsyncTask which handles the communication with clients
     */
    private static class ServerAsyncTask extends AsyncTask<Socket, Void, SocketorMessage> {

        SocketorServerListener listener;

        ServerAsyncTask(SocketorServerListener callback) {
            this.listener = callback;
        }

        @Override
        protected SocketorMessage doInBackground(Socket... params) {
            String result = null;
            Socket mySocket = params[0];
            try {

                InputStream is = mySocket.getInputStream();
                PrintWriter out = new PrintWriter(mySocket.getOutputStream(),
                        true);

                out.println(SocketorMessage.obtain("Welcome to " + SERVER_NAME + " Server").toJson());

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));

                result = br.readLine();

                //mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return SocketorMessage.parseMessage(result);
        }

        @Override
        protected void onPostExecute(SocketorMessage message) {
            if (listener != null) {
                listener.handleSocketorMessage(message);
            }

        }
    }

}
