## HTTP调用：你考虑到超时、重试、并发了吗？
### 配置连接超时和读取超时参数的学问：clientreadtimeout
### Feign和Ribbon配合使用，你知道怎么配置超时吗？：feignandribbontimeout
> Feign是一款Java语言编写的HttpClient绑定器，在Spring Cloud微服务中用于实现微服务之间的声明式调用。
> Ribbon是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法，将Netflix的中间层服务连接在一起。

- 结论一，默认情况下 Feign 的读取超时是 1 秒，如此短的读取超时算是坑点一。
`org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration {
    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 1000;
}`
- 结论二，如果要配置 Feign 的读取超时，就必须同时配置连接超时，才能生效。
`org.springframework.cloud.openfeign.FeignClientFactoryBean#configureUsingProperties
feign.client.config.default.readTimeout=3000
feign.client.config.default.connectTimeout=3000`
- 结论三，单独的超时可以覆盖全局超时，这符合预期
`feign.client.config.default.readTimeout=3000
feign.client.config.default.connectTimeout=3000
feign.client.config.clientsdk.readTimeout=2000 √
feign.client.config.clientsdk.connectTimeout=2000 √`
- 结论四，除了可以配置 Feign，也可以配置 Ribbon 组件的参数来修改两个超时时间。
但是参数首字母要大写，和 Feign 的配置不同。
`ribbon.ReadTimeout=4000
ribbon.ConnectTimeout=4000`
- 结论五，同时配置 Feign 和 Ribbon 的超时，以 Feign 为准
`org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient.getClientConfig
clientsdk.ribbon.listOfServers=localhost:45678
feign.client.config.default.readTimeout=3000
feign.client.config.default.connectTimeout=3000
ribbon.ReadTimeout=4000
ribbon.ConnectTimeout=4000`

### 你是否知道Ribbon会自动重试请求呢：ribbonretry
#### 实验一：ribbonretry/CommonMistakesApplication
- 解决办法
    - 接口从 Get 改为 Post
    - 将 MaxAutoRetriesNextServer 参数配置为 0
    
这说明客户端自作主张进行了一次重试，导致短信重复发送。
一是，把发短信接口从 Get 改为 Post。
client: port 45678
[02:00:43.060] [INFO ] [RibbonRetryIssueClientController:22  ] - client is called
[02:00:44.083] [INFO ] [RibbonRetryIssueServerController:17  ] - http://localhost:45678/ribbonretryissueserver/wrong is called, 13600000000=>c71df0f0-040b-4195-a77f-fa6aecac7ef5
[02:00:45.077] [ERROR] [RibbonRetryIssueClientController:26  ] - send sms failed : Read timed out executing GET http://SmsClient/ribbonretryissueserver/wrong?mobile=13600000000&message=c71df0f0-040b-4195-a77f-fa6aecac7ef5
server: port 45679
[02:00:43.074] [http-nio-45679-exec-2] [INFO ] [o.b.c.c.h.r.RibbonRetryIssueServerController:17  ] - http://localhost:45679/ribbonretryissueserver/wrong is called, 13600000000=>c71df0f0-040b-4195-a77f-fa6aecac7ef5

```
// MaxAutoRetriesNextServer 参数默认为 1，也就是 Get 请求在某个服务端节点出现问题（比如读取超时）时，Ribbon 会自动重试一次

// DefaultClientConfigImpl
public static final int DEFAULT_MAX_AUTO_RETRIES_NEXT_SERVER = 1;
public static final int DEFAULT_MAX_AUTO_RETRIES = 0;

// RibbonLoadBalancedRetryPolicy
public boolean canRetry(LoadBalancedRetryContext context) {
   HttpMethod method = context.getRequest().getMethod();
   return HttpMethod.GET == method || lbContext.isOkToRetryOnAllOperations();
}

@Override
public boolean canRetrySameServer(LoadBalancedRetryContext context) {
   return sameServerCount < lbContext.getRetryHandler().getMaxRetriesOnSameServer()
         && canRetry(context);
}

@Override
public boolean canRetryNextServer(LoadBalancedRetryContext context) {
   // this will be called after a failure occurs and we increment the counter
   // so we check that the count is less than or equals to too make sure
   // we try the next server the right number of times
   return nextServerCount <= lbContext.getRetryHandler().getMaxRetriesOnNextServer()
         && canRetry(context);
}
```

### 并发限制了爬虫的抓取能力：routelimit
```java
public class PoolingHttpClientConnectionManager
    implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable {
    // 
    public PoolingHttpClientConnectionManager(
        final HttpClientConnectionOperator httpClientConnectionOperator,
        final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
        final long timeToLive, final TimeUnit timeUnit) {
        super();
        this.configData = new ConfigData();
        this.pool = new CPool(new InternalConnectionFactory(
                this.configData, connFactory), 2, 20, timeToLive, timeUnit);
        this.pool.setValidateAfterInactivity(2000);
        this.connectionOperator = Args.notNull(httpClientConnectionOperator, "HttpClientConnectionOperator");
        this.isShutDown = new AtomicBoolean(false);
    }
}
```
- defaultMaxPerRoute=2，也就是同一个主机 / 域名的最大并发请求数为 2。我们的爬虫需要 10 个并发，显然是默认值太小限制了爬虫的
效率。
- maxTotal=20，也就是所有主机整体最大并发为 20，这也是 HttpClient 整体的并发度。目前，我们请求数是 10 最大并发是 10，20 不会
成为瓶颈。举一个例子，使用同一个 HttpClient 访问 10 个域名，defaultMaxPerRoute 设置为 10，为确保每一个域名都能达到 10 并发，
需要把 maxTotal 设置为 100。
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