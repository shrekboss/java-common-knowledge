## HTTP调用：你考虑到超时、重试、并发了吗？
### 配置连接超时和读取超时参数的学问：clientreadtimeout
### Feign和Ribbon配合使用，你知道怎么配置超时吗？：feignandribbontimeout
### 你是否知道Ribbon会自动重试请求呢：ribbonretry
### 并发限制了爬虫的抓取能力：routelimit
### （补充）Feign方法级别设置超时的例子：feignpermethodtimeout
```java
@FeignClient(name = "clientsdk")
public interface Client {
    @GetMapping("/feignpermethodtimeout/method1")
    String method1(Request.Options options);

    @GetMapping("/feignpermethodtimeout/method2")
    String method2(Request.Options options);
// 使用 
// client.method2(new Request.Options(1000, 3500));
}
```

### 注意连接超时和读取超时参数的配置，大多数的 HTTP 客户端也都有这两个参数。有读就有写，但为什
么很少看到“写入超时”的概念呢？
- 写入操作只是将数据写入 TCP 的发送缓冲区，已经发送到网络的数据依然需要暂存在发送缓冲区中，只有
收到对方的 ack 后，操作系统内核才从缓冲区中清除这一部分数据，为后续发送数据腾出空间。
- 如果接收端从 socket 读取数据的速度太慢，可能会导致**发送端发送缓冲区满，导致写入操作阻塞，产生写
入超时**。但是，因为有**滑动窗口的控制**，通常不太容易发生发送缓冲区满导致写入超时的情况。相反，
读取超时包含了服务端处理数据执行业务逻辑的时间，所以读取超时是比较容易发生的。

### 除了 Ribbon 的 AutoRetriesNextServer 重试机制，Nginx 也有类似的重试功能
- Nginx 的 proxy_next_upstream 配置
`proxy_next_upstream error timeout http_500;`
    - proxy_next_upstream，用于指定在什么情况下 Nginx 会将请求转移到其他服务器上。其**默认值是 
    proxy_next_upstream error timeout**，即发生网络错误以及超时，才会重试其他服务器。也就是说，
    默认情况下，服务返回 500 状态码是不会重试的。 
    - 需要注意的是，proxy_next_upstream 配置中有一个选项 **non_idempotent**，一定要小心开启。通
    常情况下，如果请求使用非等幂方法（POST、PATCH），请求失败后不会再到其他服务器进行重试。但是，
    **加上 non_idempotent 这个选项后，即使是非幂等请求类型（例如 POST 请求），发生错误后也会重试**。