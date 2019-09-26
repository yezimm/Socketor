# Socketor

用于wifi socket 通信

主要是为了两个设备在统一局域网下通信方便。

先在项目的root目录下的build.gradle文件加入

```java
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

然后添加依赖

```java
    dependencies {
		implementation 'com.github.yezimm:Socketor:0.1.2.1'
    }
```


其中一个设备开启热点，并设置为服务端，使用 `SocketorServer`

```java
SocketorServerListener listener = new SocketorServerListener() {

    @Override
    public void disconnect(String message) {
        // server socket 线程退出，断开连接
        Log.i(TAG, "disconnect:" + message);
    }
    
    @Override
    public void onException(String message) {
        // IO 异常
        Log.i(TAG, "onException:" + message);
    }

    @Override
    public void handleSocketorMessage(@NonNull SocketorMessage message) {
        // 处理客户端发送来的消息
        Log.i(TAG, "handleSocketorMessage:" + message.toJson());

    }
};
SocketorServer socketorServer = Socketor.newSocketorServer().setServerListener(listener);
```

另一个或者其他设备连接热点，并设置为客户端，使用 `SocketorClient`


```java
// 需要自己初始化
SocketorMessage socketorMessage = null;
// 获取一个新的SocketorClient
Socketor.newSocketorClient(getApplicationContext())
        .setClientListener(new SocketorClientListener() {
    @Override
    public void connectFaild(String message) {
        // 连接失败
        showTips(message);
    }

    @Override
    public void sendSuccess(@NonNull SocketorMessage message) {
        // 成功拿到返回数据
        showTips(message.toJson());
    }
}).sendMessage(socketorMessage);
```

