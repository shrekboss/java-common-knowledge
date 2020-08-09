## Spring框架：IoC和AOP是扩展的核心
- 往蛋糕胚子里面加奶油，而不能上面或下面加奶油。这就是连接点（Join point）
    - 对于 Spring AOP 来说，**连接点就是方法执行**。
- 在什么点切开蛋糕加奶油。比如，可以在蛋糕坯子中间加入一层奶油，在中间切一次；也可以在中间加两层
奶油，在 1/3 和 2/3 的地方切两次。这就是切点（Pointcut）
    - Spring AOP 中**默认使用 AspectJ 查询表达式，通过在连接点运行查询表达式来匹配切入点**。
- 最重要的，切开蛋糕后要做什么，也就是加入奶油。这就是增强（Advice），也叫作通知，**定义了切入切点
后增强的方式**，包括前、后、环绕等。
    - Spring AOP 中，**把增强定义为拦截器**。
- 找到蛋糕胚子中要加奶油的地方并加入奶油。为蛋糕做奶油夹层的操作。
    - 对 Spring AOP 来说就是切面（Aspect），也叫作方面。**切面 = 切点 + 增强**。

### 单例的Bean如何解决循环依赖(构造器的循环依赖 && field属性的循环依赖)？：beansingletoncirculardependency
**当 Bean 产生循环依赖问题**
直观解决方法时通过set方法去处理，背后的原理其实是缓存。
主要解决方式：**使用三级缓存**
**singletonObjects**： 一级缓存， Cache of singleton objects: bean name --> bean instance
**earlySingletonObjects**： 二级缓存， Cache of early singleton objects: bean name --> bean instance 提前曝光的BEAN缓存
**singletonFactories**： 三级缓存， Cache of singleton factories: bean name --> ObjectFactory
【解决方案参考】
1. TestA && TestB
2. TestC && TestD

### 单例的Bean如何注入Prototype的Bean？：beanprototype
Spring 创建的 Bean 默认是单例的，但当 Bean 遇到继承的时候，可能会忽略这一点。

开发基类的架构师将基类设计为有状态的，但并不知道子类是怎么使用基类的；而开发子类的同学，没多想就
直接标记了 @Service，让类成为了 Bean，通过 @Autowired 注解来注入这个服务。但这样设置后，**有状态的
基类就可能产生内存泄露或线程安全问题**。
- 正确的方式是，在为类标记上 @Service 注解把类型交由容器管理前，首先评估一下类是否有状态，然后为
Bean 设置合适的 Scope。
`@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)`
- **上线后还是可能出现了内存泄漏，证明修改是无效的**(单例的 Bean 如何注入 Prototype 的 Bean 这个问题)
    - Controller 标记了 @RestController 注解，而 @RestController 注解 =@Controller 注解 +@ResponseBody 
    注解，又因为 @Controller 标记了 @Component 元注解，所以 @RestController 注解其实也是一个 Spring Bean
    - **Bean 默认是单例的，所以单例的 Controller 注入的 Service 也是一次性创建的，即使 Service 本身标
    识了 prototype 的范围也没用**
    - 修复方式有两种：
        - **让 Service 以代理方式注入。**这样虽然 Controller 本身是单例的，但每次都能从代理获取 Service。
        这样一来，prototype 范围的配置才能真正生效
        - 如果不希望走代理的话还有一种方式是，**每次直接从 ApplicationContext 中获取 Bean**。
- **Spring 注入的 XXXService 的 List，要注意顺序问题**。

### 监控切面因为顺序问题导致Spring事务失效：aopmetrics
如果一组相同类型的 Bean 是有顺序的，需要明确使用 @Order 注解来设置顺序。

**修改点**
- MetricsController 手动加上了 @Metrics 注解，设置 logParameters 和 logReturn 为 false；
    - **发现日志量并没有减少**
- 然后为 Service 中的 createUser 方法的 @Metrics 注解，设置了 ignoreException 属性为 true
    - Spring 的事务管理是基于 AOP 的，**默认情况下优先级最低也就是会先执行出操作**，但是自定义切
    面 MetricsAspect 也同样是最低优先级，这个时候就可能出现问题：**如果出操作先执行捕获了异常，那么 
    Spring 的事务处理就会因为无法捕获到异常导致无法回滚事务**。
    - Spring 通过 TransactionAspectSupport 类实现事务。在 invokeWithinTransaction 方法中设置断点可以
    发现，在执行 Service 的 createUser 方法时，TransactionAspectSupport 并没有捕获到异常，所以自
    然**无法回滚事务**。原因就是，异常被 MetricsAspect 吃掉了。

- Spring 对不同切面增强的执行顺序是由 Bean 优先级决定的，具体规则是：
    - **入操作**（Around（连接点执行前）、Before），**切面优先级越高，越先执行**。一个切面的入操
    作执行完，才轮到下一切面，所有切面入操作执行完，才开始执行连接点（方法）。
    - **出操作**（Around（连接点执行后）、After、AfterReturning、AfterThrowing），**切面优先级越低，越先
    执行**。一个切面的出操作执行完，才轮到下一切面，直到返回到调用点。
    - **同一切面的 Around 比 After、Before 先执行**。

对于 Bean 可以通过 @Order 注解来设置优先级，查看 @Order 注解和 Ordered 接口源码可以发现，默认情
况下 Bean 的优先级为最低优先级，其值是 Integer 的最大值。其实，**值越大优先级反而越低**，这点比较反
直觉：【**参考 增强执行顺序符合切面执行顺序的三个规则.png**】

- **切入的连接点是方法，注解定义在类上是无法直接从方法上获取到注解的。修复方式是，改为优先从方法获
取，如果获取不到再从类获取，如果还是获取不到再使用默认的注解。**
```
Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
if (metrics == null) {
    metrics = signature.getMethod().getDeclaringClass().getAnnotation(Metrics.class);
}

String name = String.format("【%s】【%s】", signature.getDeclaringType().toString(), signature.toLongString());
// 因为需要默认对所有@RestController标记的Web控制器实现 @Metrics注解的功能，在这种情况下
// 方法上必然是没有 @Metrics注解的，需要获取一个默认注解。
// 虽然可以手动实例化一个@Metrics注解的实例出来，但为了节省代码行数，通过在一个内部类上定
// @Metrics注解方式，然后通过反射获取注解的小技巧，来获得一个默认的@Metrics注解的实例义
if (metrics == null) {
    @Metrics
    final class c {
    }
    metrics = c.class.getAnnotation(Metrics.class);
}
```

### @Autowired 注入 Bean 外，还可以使用 @Inject 或 @Resource 来注入 Bean。三种方式的区别
**@Autowired**
1、@Autowired是spring自带的注解，通过‘AutowiredAnnotationBeanPostProcessor’ 类实现的依赖注入；
2、@Autowired是根据类型进行自动装配的，如果需要按名称进行装配，则需要配合@Qualifier；
3、@Autowired有个属性为required，可以配置为false，如果配置为false之后，当没有找到相应bean的时候，
系统不会抛错；
4、@Autowired可以作用在变量、setter方法、构造函数上。

@Inject
1、@Inject是JSR330 (Dependency Injection for Java)中的规范，需要导入javax.inject.Inject;实现注入。
2、@Inject是根据类型进行自动装配的，如果需要按名称进行装配，则需要配合@Named；
3、@Inject可以作用在变量、setter方法、构造函数上。

@Resource
1、@Resource是JSR250规范的实现，需要导入javax.annotation实现注入。
2、@Resource是根据名称进行自动装配的，一般会指定一个name属性
3、@Resource可以作用在变量、setter方法上。

总结：
1、@Autowired是spring自带的，@Inject是JSR330规范实现的，@Resource是JSR250规范实现的，需要导入
不同的包
2、@Autowired、@Inject用法基本一样，不同的是@Autowired有一个request属性
3、@Autowired、@Inject是默认按照类型匹配的，@Resource是按照名称匹配的
4、@Autowired如果需要按照名称匹配需要和@Qualifier一起使用，@Inject和@Name一起使用











