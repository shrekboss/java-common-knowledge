> Per default this implementation will create no more than than 2 concurrent connections per given 
> route and no more 20 connections in total. For many real-world applications
> these limits may prove too constraining, especially if they use HTTP
> as a transport protocol for their services. Connection limits, however,
> can be adjusted using {@link ConnPoolControl} methods.
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
- **defaultMaxPerRoute**=2，也就是同一个主机 / 域名的最大并发请求数为 2。我们的爬虫需要 10 个并发，
显然是默认值太小限制了爬虫的效率。
- **maxTotal**=20，也就是所有主机整体最大并发为 20，这也是 HttpClient 整体的并发度。目前，请求数是 10 
最大并发是 10，20 不会成为瓶颈。举一个例子，使用同一个 HttpClient 访问 10 个域名，defaultMaxPerRoute 
设置为 10，为确保每一个域名都能达到 10 并发，需要把 maxTotal 设置为 100。