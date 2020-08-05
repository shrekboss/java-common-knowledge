## Spring框架：帮我们做了很多工作也带来了复杂度
### Feign AOP切不到的诡异案例：aopfeign
【参考 Feign AOP切不到的诡异案例】

### Spring程序配置的优先级问题：propertysource
Spring 通过环境 Environment 抽象出的 Property 和 Profile：
- **Property**，又抽象出各种 PropertySource 类代表配置源。一个环境下可能有多个配置源，每个配置源
中有诸多配置项。在查询配置信息时，需要按照配置源优先级进行查询。
- **Profile** 定义了场景的概念。通常，会定义类似 dev、test、stage 和 prod 等环境作为不同的 Profile，用于
按照场景对 Bean 进行逻辑归属。同时，Profile 和配置文件也有关系，每个环境都有独立的配置文件，但只会
激活某一个环境来生效特定环境的配置文件。
【参考 Environment、Property 和 Profile关系.png】

**对于非 Web 应用**，Spring 对于 Environment 接口的实现是 **StandardEnvironment** 类。通过 Spring 注入 
StandardEnvironment 后循环 getPropertySources 获得的 PropertySource，来查询所有的 PropertySource 
中 key 的属性值；然后遍历 getPropertySources 方法，获得所有配置源并打印出来。

ConfigurationPropertySourcesPropertySource {name='configurationProperties'} -> yizhe.chen 实际取值：yizhe.chen
PropertiesPropertySource {name='systemProperties'} -> yizhe.chen 实际取值：yizhe.chen
OriginTrackedMapPropertySource {name='applicationConfig: [classpath:/application.properties]'} -> defaultadminname 实际取值：yizhe.chen
ConfigurationPropertySourcesPropertySource {name='configurationProperties'} -> 12345 实际取值：12345
OriginAwareSystemEnvironmentPropertySource {name='systemEnvironment'} -> 12345 实际取值：12345
OriginTrackedMapPropertySource {name='applicationConfig: [classpath:/application.properties]'} -> 45679 实际取值：12345

配置优先级：
**ConfigurationPropertySourcesPropertySource** {name='configurationProperties'} √
StubPropertySource {name='servletConfigInitParams'}
ServletContextPropertySource {name='servletContextInitParams'}
**PropertiesPropertySource** {name='systemProperties'} √ JVM 系统配置
**OriginAwareSystemEnvironmentPropertySource** {name='systemEnvironment'}
RandomValuePropertySource {name='random'}
OriginTrackedMapPropertySource {name='applicationConfig: [classpath:/application.properties]'} √ 配置文件配置
MapPropertySource {name='springCloudClientHostInfo'}
MapPropertySource {name='defaultProperties'}

**值得关注是** 
**ConfigurationPropertySourcesPropertySource
PropertiesPropertySource
OriginAwareSystemEnvironmentPropertySource
application.properties 配置文件**

**StandardEnvironment，继承的是 AbstractEnvironment（图中紫色类）。AbstractEnvironment 的源码如下**：
- MutablePropertySources 类型的字段 propertySources，代表了所有配置源；
- getProperty 方法，通过 PropertySourcesPropertyResolver 类进行查询配置；
- 实例化 PropertySourcesPropertyResolver 的时候，传入了当前的 MutablePropertySources。

**MutablePropertySources 的源码（图中蓝色类）**：
- propertySourceList 字段用来真正保存 PropertySource 的 List，且这个 List 是一个 CopyOnWriteArrayList。
- 类中定义了 addFirst、addLast、addBefore、addAfter 等方法，来精确控制 PropertySource 加入 
propertySourceList 的顺序。**这也说明了顺序的重要性**。

**PropertySourcesPropertyResolver（图中绿色类）的源码，真正查询配置的方法 getProperty**：
遍历的 propertySources 是 PropertySourcesPropertyResolver 构造方法传入的，再结合 AbstractEnvironment 
的源码可以发现，这个 propertySources 正是 AbstractEnvironment 中的 MutablePropertySources 对象。
遍历时，如果发现配置源中有对应的 Key 值，则使用这个值。**因此，MutablePropertySources 中配置源的次
序尤为重要**。

**处在第一位的是 ConfigurationPropertySourcesPropertySource，这是什么呢？**
- 其实，它不是一个实际存在的配置源，扮演的是一个代理的角色。但通过调试会发现，**获取的值竟然是由它
提供并且返回的**，且没有循环遍历后面的 PropertySource。
- ConfigurationPropertySourcesPropertySource 是所有配置源中的第一个，实现了对 
PropertySourcesPropertyResolver 中遍历逻辑的“劫持”，并且知道了其遍历逻辑。

**查看 ConfigurationPropertySourcesPropertySource（图中红色类）的源码**：
- getProperty 方法其实是通过 findConfigurationProperty 方法查询配置的，其实还是在遍历所有的配置源。
- 循环遍历（getSource() 的结果）的配置源，其实是 SpringConfigurationPropertySources（图中黄色类），
其中包含的配置源列表就是之前看到的 9 个配置源，而第一个就是 
ConfigurationPropertySourcesPropertySource。
- 同时观察 configurationProperty 可以看到，这个 ConfigurationProperty 其实类似代理的角色，实际配置
是从系统属性中获得的
- 查看 SpringConfigurationPropertySources 可以发现，它返回的迭代器是内部类 SourcesIterator，在 
fetchNext 方法获取下一个项时，通过 isIgnored 方法排除了 ConfigurationPropertySourcesPropertySource

**ConfigurationPropertySourcesPropertySource 如何让自己成为第一个配置源呢？**
- ConfigurationPropertySourcesPropertySource 类是由 ConfigurationPropertySources 的 attach 方法实例
化的。查阅源码可以发现，这个方法的确从环境中获得了原始的 MutablePropertySources，把自己加入成为
一个元素。
- 而这个 attach 方法，是 Spring 应用程序启动时准备环境的时候调用的。在 SpringApplication 的 run 方
法中调用了 prepareEnvironment 方法，然后又调用了 ConfigurationPropertySources.attach 方法。

### （补充）替换配置属性中占位符的例子：custompropertysource