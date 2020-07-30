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
### 并发限制了爬虫的抓取能力：routelimit
### （补充）Feign方法级别设置超时的例子：feignpermethodtimeout