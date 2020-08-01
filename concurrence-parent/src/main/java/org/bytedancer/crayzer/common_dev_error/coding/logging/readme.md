## 日志：日志记录真没你想象的那么简单
SLF4J 实现了三种功能：
- 一是提供了统一的日志门面 API，即图中紫色部分，实现了中立的日志记录 API。
- 二是桥接功能，即图中蓝色部分，用来把各种日志框架的 API（图中绿色部分）桥接到 SLF4J API。这样
一来，即便你的程序中使用了各种日志 API 记录日志，最终都可以桥接到 SLF4J 门面 API。
- 三是适配功能，即图中红色部分，可以实现 SLF4J API 和实际日志框架（图中灰色部分）的绑定。SLF4J 
只是日志标准，我们还是需要一个实际的日志框架。日志框架本身没有实现 SLF4J API，所以需要有一个前
置转换。Logback 就是按照 SLF4J API 标准实现的，因此不需要绑定模块做转换。

**需要理清楚的是，** 虽然我们可以使用 log4j-over-slf4j 来实现 Log4j 桥接到 SLF4J，也可以使用 
slf4j-log4j12 实现 SLF4J 适配到 Log4j，也把它们画到了一列，但是它不能同时使用它们，否则就会产生死
循环。jcl 和 jul 也是同样的道理。

### 为什么我的日志会重复记录？：duplicate
- Appender 不要挂载到了两个 Logger 上(其中自定义的 <logger> 继承自 <root>)。
- 错误配置 LevelFilter 造成日志重复

**ThresholdFilter 的源码发现 ：** 当日志级别大于等于配置的级别时返回 NEUTRAL，继续调用过滤器链上的下
一个过滤器；否则，返回 DENY 直接拒绝记录日志：
**LevelFilter 的源码发现：** 用来比较日志级别，然后进行相应处理：如果匹配就调用 onMatch 定义的处理方式，
默认是交给下一个过滤器处理（AbstractMatcherFilter 基类中定义的默认值）；否则，调用 onMismatch 定
义的处理方式，默认也是交给下一个过滤器处理。

和 ThresholdFilter 不同的是，LevelFilter 仅仅配置 level 是无法真正起作用的。**由于没有配置 onMatch 和 
onMismatch 属性，所以相当于这个过滤器是无用的，导致 INFO 以上级别的日志都记录了。**

### 使用异步日志改善性能的坑：async
### 使用日志占位符就不需要进行日志级别判断了：placeholder
