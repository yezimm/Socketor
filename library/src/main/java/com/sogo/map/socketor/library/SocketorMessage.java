package com.sogo.map.socketor.library;

import com.google.gson.Gson;

public class SocketorMessage {

    @SocketorConfig.StatusType
    private int status;
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

    public SocketorMessage setStatus(int status) {
        this.status = status;
        return this;
    }

    /**
     * 获取本次通信的状态
     * @return 0，成功；1失败
     */
    public int getStatus() {
        return status;
    }

    /**
     * 获取内容
     * @return
     */
    public String getMessage() {
        return message;
    }
}
