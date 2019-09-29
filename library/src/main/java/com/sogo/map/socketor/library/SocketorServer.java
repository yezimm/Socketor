package com.sogo.map.socketor.library;

import android.os.AsyncTask;

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
import static com.sogo.map.socketor.library.SocketorConfig.STATUS_FAILED;
import static com.sogo.map.socketor.library.SocketorConfig.STATUS_OK;

final public class SocketorServer extends SocketotBase {

    private boolean flag;
    private ServerSocket socServer;
    private SocketorServerListener listener;
    private ExecutorService executorService;

    SocketorServer() {
        flag = true;
        create();
    }

    /**
     * 创建 SocketServer，搭建server的循环等待
     * 如果disconnect，或者Socket创建失败可以调用create重新创建
     */
    public void create() {
        if (socServer == null) {
            try {
                socServer = new ServerSocket(SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                if(listener != null){
                    listener.disconnect("Server socket create fail");
                }
                return;
            }
        }
        if (executorService == null) {
            // 启用一个核心线程
            executorService = Executors.newSingleThreadExecutor();
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socServer != null)
                        while (flag) {
                            Socket socClient = socServer.accept();
                            ServerAsyncTask serverAsyncTask = new ServerAsyncTask(listener);
                            serverAsyncTask.execute(socClient);
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.disconnect("Server socket disconnect");
                    }
                }
            }
        });
    }

    public SocketorServer setServerListener(SocketorServerListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 不需要时，删除socket server
     */
    public void onStop() {
        flag = false;
        if(executorService != null)
            executorService.shutdownNow();
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
            String result;
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
                return SocketorMessage.obtain("IO 异常").setStatus(STATUS_FAILED);
            }
            return SocketorMessage.parseMessage(result).setStatus(STATUS_OK);
        }

        @Override
        protected void onPostExecute(SocketorMessage message) {
            if (listener != null && message != null) {
                if (STATUS_FAILED == message.getStatus())
                    listener.onException(message.getMessage());
                else
                    listener.handleSocketorMessage(message);
            }

        }
    }

}