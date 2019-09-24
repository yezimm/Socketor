package com.sogo.map.socketor.library;

import androidx.annotation.IntDef;

import com.google.gson.Gson;

final public class SocketorConfig {

    public final static int SERVER_PORT = 8080;
    public final static String SERVER_NAME = "SocketorServer";
    public final static String CLIENT_NAME = "SocketorClient";

    public static final int NONE = 0;
    public static final int SWITCH_NORMAL_TO_AR = 1;
    public static final int SWITCH_AR_TO_NORMAL = 2;

    @IntDef({NONE, SWITCH_AR_TO_NORMAL, SWITCH_NORMAL_TO_AR})
    public @interface SwitchType { }

    public static final int STATUS_OK = 0;
    public static final int STATUS_FAILED = 1;
    public @interface StatusType { }

    /**
     * 根据int型的路网地址解析成ip地址
     * @param ip int型数据
     * @return "xxx.xxx.xxx.xxx"
     */
    static String parseIPByInt(int ip) {
        return ((ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF)
                + "." + ((ip >> 24) & 0xFF));
    }

}
