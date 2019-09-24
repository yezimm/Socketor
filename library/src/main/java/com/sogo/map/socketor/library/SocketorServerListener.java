package com.sogo.map.socketor.library;

import androidx.annotation.NonNull;

public interface SocketorServerListener {

    /**
     * socket server 异常退出，断开连接
     */
    void disconnect(String message);

    /**
     * IO异常
     */
    void onException(String message);

    /**
     * 处理客户端传来的 socketor message 对象
     */
    void handleSocketorMessage(@NonNull SocketorMessage message);

}
