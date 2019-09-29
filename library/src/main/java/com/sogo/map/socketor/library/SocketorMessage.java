package com.sogo.map.socketor.library;

import com.google.gson.Gson;

public class SocketorMessage {

    private int status;
    private int switchType;
    private String message;
    // 预留字段，方便使用
    public int args1 = -1;
    public int args2 = -1;

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

    /**
     * 获取switch类型
     * @return switch类型
     */
    public int getSwitchType() {
        return switchType;
    }
}
