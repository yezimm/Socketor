package com.sogo.map.socketor.library;

import androidx.annotation.NonNull;

public interface SocketorServerListener {

    /**
     * 处理客户端传来的 socketor message 对象
     */
    void handleSocketorMessage(@NonNull SocketorMessage message);

}
