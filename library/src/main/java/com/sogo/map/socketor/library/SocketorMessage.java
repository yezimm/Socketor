package com.sogo.map.socketor.library;

import com.google.gson.Gson;

public class SocketorMessage {

    @SocketorConfig.SwitchType
    private int switchType;
    private String message;

    private SocketorMessage(int switchType, String message) {
        this.switchType = switchType;
        this.message = message;
    }

    public static SocketorMessage obtain() {
        return obtain(SocketorConfig.NONE, null);
    }

    public static SocketorMessage obtain(String message) {
        return obtain(SocketorConfig.NONE, message);
    }

    public static SocketorMessage obtain(int switchType, String message) {
        return new SocketorMessage(switchType, message);
    }

    public static SocketorMessage parseMessage(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SocketorMessage.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
