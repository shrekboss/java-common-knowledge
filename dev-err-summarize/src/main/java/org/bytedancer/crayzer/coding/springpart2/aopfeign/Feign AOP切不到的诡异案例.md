## Feign AOP切不到的诡异案例
> 使用 Spring Cloud 做微服务调用，为方便统一处理 Feign，想到了用 AOP 实现，即使用 within 指示器匹
> 配 feign.Client 接口的实现进行 AOP 切入。一开始这个项目使用的是客户端的负载均衡，也就是让 Ribbon 
> 来做负载均衡，代码没啥问题。后来因为后端服务通过 Nginx 实现服务端负载均衡，所以把 @FeignClient 
> 的配置设置了 URL 属性，直接通过一个固定 URL 调用后端服务：
>
> 小技巧：如果你希望知道一个类是怎样调用栈初始化的，可以在构造方法中设置一个断点进行调试。

**FeignClient 的创建过程，也就是分析 FeignClientFactoryBean 类的 getTarget 方法**
- **Feign 指定 URL** 
    - 当 URL 没有内容也就是为空或者不配置时调用 loadBalance 方法，在其内部通过 FeignContext 从容器获取 
    feign.Client 的实例。
    - client 是 LoadBalanceFeignClient，已经是经过代理增强的，明显是一个 Bean
- **Feign 不指定 URL**
    - 当 URL 不为空的时候，client 设置为了 LoadBalanceFeignClient 的 delegate 属性。其原因注释中有
    提到，因为有了 URL 就不需要客户端负载均衡了，但因为 Ribbon 在 classpath 中，所以需要从 
    LoadBalanceFeignClient 提取出真正的 Client。
    -  client 是一个 ApacheHttpClient
    - HttpClientFeignLoadBalancedConfiguration 类实例化的 ApacheHttpClient，LoadBalancerFeignClient 
    这个 Bean 在实例化的时候，**new 出来一个** ApacheHttpClient 作为 delegate 放到了 
    LoadBalancerFeignClient 中，说明 ApacheHttpClient 不是一个 Bean。
    ```
        @Bean
        @ConditionalOnMissingBean(Client.class)
        public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
              SpringClientFactory clientFactory, HttpClient httpClient) {
           ApacheHttpClient delegate = new ApacheHttpClient(httpClient);
           return new LoadBalancerFeignClient(delegate, cachingFactory, clientFactory);
        }
        
        public LoadBalancerFeignClient(Client delegate,
              CachingSpringLoadBalancerFactory lbClientFactory,
              SpringClientFactory clientFactory) {
           this.delegate = delegate;
           this.lbClientFactory = lbClientFactory;
           this.clientFactory = clientFactory;
        }
    ```
    - ApacheHttpClient 其实有机会独立成为 Bean。查看 HttpClientFeignConfiguration 的源码可以发现，
    当没有 ILoadBalancer 类型的时候，自动装配会把 ApacheHttpClient 设置为 Bean。
    
    ```java
        @Configuration
        @ConditionalOnClass(ApacheHttpClient.class)
        // 如果我们不希望做客户端负载均衡的话，应该不会引用 Ribbon 组件的依赖，自然没有 
        // LoadBalancerFeignClient，只有 ApacheHttpClient
        @ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")
        @ConditionalOnMissingBean(CloseableHttpClient.class)
        @ConditionalOnProperty(value = "feign.httpclient.enabled", matchIfMissing = true)
        protected static class HttpClientFeignConfiguration {
          @Bean
          @ConditionalOnMissingBean(Client.class)
          public Client feignClient(HttpClient httpClient) {
            return new ApacheHttpClient(httpClient);
          }
        }
    ```
  
  - pom.xml 中的 ribbon 模块注释之后
  ```
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    </dependency>
  ```
  
Caused by: java.lang.IllegalArgumentException: Cannot subclass final class feign.httpclient.ApacheHttpClient
  at org.springframework.cglib.proxy.Enhancer.generateClass(Enhancer.java:657)
  at org.springframework.cglib.core.DefaultGeneratorStrategy.generate(DefaultGeneratorStrategy.java:25)
Spring 实现动态代理的两种方式：
- JDK 动态代理，通过反射实现，只支持对实现接口的类进行代理；
- CGLIB 动态字节码注入方式，通过继承实现代理，没有这个限制。

Spring Boot 2.x 默认**使用 CGLIB 的方式**，但通过继承实现代理有个问题是，**无法继承 final 的类**。因为，
ApacheHttpClient 类就是定义为了 final.
切换到使用 JDK 动态代理的方式：
`spring.aop.proxy-target-class=false`