package com.sogo.map.socketor.library;

import androidx.annotation.NonNull;

public interface SocketorClientListener {

    /**
     * socket连接失败
     * @param message 失败的原因
     */
    void connectFaild(String message);

    /**
     * 连接成功
     * @param message  服务端返回的数据
     */
    void sendSuccess(@NonNull SocketorMessage message);

}
