package com.sogo.map.socketor.library;

import android.content.Context;

public class Socketor {

    private final static class SocketHolder {

        private final static Socketor instance = new Socketor();

    }

    private Socketor() {}

    public static Socketor getInstance() {
        return SocketHolder.instance;
    }

    /**
     *
     * @return Socket server
     */
    public static SocketorServer newSocketorServer() {
        return new SocketorServer();
    }

    /**
     *
     * @param context 上下文对象
     * @return Socket client
     */
    public static SocketorClient newSocketorClient(Context context) {
        return new SocketorClient(context);
    }

}
