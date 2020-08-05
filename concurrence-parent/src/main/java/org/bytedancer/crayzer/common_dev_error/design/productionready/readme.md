## 业务代码写完，就意味着生产就绪了？
所谓生产就绪（Production-ready），是指应用开发完成要投入生产环境，开发层面需要额外做的一些工作。

生产就绪，以下三方面的工作最重要
- 第一，**提供健康检测接口**。传统采用 ping 的方式对应用进行探活检测并不准确。有的时候，应用的关键内
部或外部依赖已经离线，导致其根本无法正常工作，但其对外的 Web 端口或管理端口是可以 ping 通的。应
该提供一个专有的监控检测接口，并尽可能触达一些内部组件。
- 第二，**暴露应用内部信息**。应用内部诸如线程池、内存队列等组件，往往在应用内部扮演了重要的角色，
如果应用或应用框架可以对外暴露这些重要信息，并加以监控，那么就有可能在诸如 OOM 等重大问题暴露之
前发现蛛丝马迹，避免出现更大的问题。
- 第三，**建立应用指标 Metrics 监控**。Metrics 可以翻译为度量或者指标，指的是对于一些关键信息以可聚合
的、数值的形式做定期统计，并绘制出各种趋势图表。这里的指标监控，包括两个方面：
    - 一是，**应用内部重要组件的指标监控**，比如 JVM 的一些指标、接口的 QPS 等；
    - 二是，**应用的业务数据的监控**，比如电商订单量、游戏在线人数等。

### 准备工作：配置Spring Boot Actuator：heal
Actuator 模块封装了诸如健康检测、应用内部信息、Metrics 指标等生产就绪的功能。

spring Boot 管理工具[Spring Boot Admin](https://github.com/codecentric/spring-boot-admin)
[端点的功能](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/actuator-api//html/)

### 健康检测需要触达关键组件：health
如果程序依赖一个很重要的三方服务，希望这个服务无法访问的时候，应用本身的健康状态也是 DOWN，实
现这个服务是否正确响应和程序整体的健康状态挂钩的话，只需实现 HealthIndicator 接口即可，参考: 
**health/UserServiceHealthIndicator**
**health/ThreadPoolHealthIndicator**

### 对外暴露应用内部重要组件的状态：info
除了可以把线程池的状态作为整个应用程序是否健康的依据外，还可以通过 Actuator 的 InfoContributor 功
能，对外暴露程序内部重要组件的状态数据。参考：
**info/ThreadPoolInfoContributor**

如果设置开启 JMX 的话：`spring.jmx.enabled=true`，通过 jconsole 工具，在 
org.springframework.boot.Endpoint 中找到 Info 这个 MBean，然后执行 info 操作可以看到。

**对于查看和操作 MBean**，除了使用 jconsole 之外，你可以使用 jolokia 把 JMX 转换为 HTTP 协议，引入依赖：
```
<dependency>
    <groupId>org.jolokia</groupId>
    <artifactId>jolokia-core</artifactId>
</dependency>
```
`http://localhost:45679/admin/jolokia/exec/org.springframework.boot:type=Endpoint,name=Info/info`

### 指标Metrics是快速定位问题的“金钥匙”：metrics
使用 Micrometer 框架实现指标的收集，它也是 Spring Boot Actuator 选用的指标框架。它实现了各种指标
的抽象，常用的有三种：
- gauge（红色），它反映的是指标当前的值，是多少就是多少，不能累计，比如本例中的下单总数量指标，
又比如游戏的在线人数、JVM 当前线程数都可以认为是 gauge。
- counter（绿色），每次调用一次方法值增加 1，是可以累计的，比如本例中的下单请求指标。举一个例子，
如果 5 秒内调用了 10 次方法，Micrometer 也是每隔 5 秒把指标发送给后端存储系统一次，那么它可以只发
送一次值，其值为 10。
- timer（蓝色），类似 counter，只不过除了记录次数，还记录耗时，比如本例中的下单成功和下单失败两个
指标。
```
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-influx</artifactId>
</dependency>
```