## MyLog
> About: 从零实现一个日志框架

### 输出内容 - LoggingEvent

提到日志框架，最容易想到的核心功能，那就是输出日志了。那么对于一行日志内容来说，应该至少包含以下几个信息：

- 日志时间戳
- 线程信息
- 日志名称（一般是全类名）
- 日志级别
- 日志主体（需要输出的内容，比如info（str））

### 输出组件 - Appender

> 输出方式：标准输出/控制台（Standard Output/Console）、文件（File）、邮件（Email）、甚至是消息队列（MQ）和数据库

### 日志级别设计 - Level

> 日志框架还应该提供日志级别的功能，程序在使用时可以打印不同级别的日志，还可以根据日志级别来调整那些日志可以显示，一般日志级别会定义为以下几种，级别从左到右排序，只有大于等于某级别的LoggingEvent才会进行输出
> ERROR > WARN > INFO > DEBUG > TRACE

### 日志打印入口 - Logger

- 提供 error/warn/info/debug/trace 几个打印的方法
- 定义一个 name 属性，用于区分不同的 Logger
- 调用 appender 输出日志
- 拥有自己的专属级别(比如自身级别为 INFO，那么只有 /INFO/WARN/ERROR 才可以输出)

### 日志层级 - Hierarchy

> 一般在使用日志框架时，有一个很基本的需求：不同包名的日志使用不同的输出方式，
> 或者不同包名下类的日志使用不同的日志级别，比如想让框架相关的DEBUG日志输出，
> 便于调试，其他默认用INFO级别。
>
> - 直接使用一个全局的 Logger 配置，同时还支持特殊配置的 Logger，且这个配置需要让程序中创建
    > Logger 时无感(比如 LoggerFactory.getLogger(XXX.class)) 查看 层级结构.jpg。
    >
- 每一个Logger拥有一个Parent Logger，在 filterAndLog 时优先使用自己的Appender，如果自己没
  > 有 Appender，那么就向上调用父类的appender，有点反向“双亲委派（parents delegate）”的意思。
> - 为每个包名的配置单独定义一个全局Logger，在解析包名配置时直接为不同的包名。

### 日志上下文 - LoggerContext

> 考虑到有一些全局的Logger，和Root Logger需要被各种Logger引用，所以得设计一个Logger容器，用来存储这些Logger

### 日志创建 - LoggerFactory

> 为了方便的构建Logger的层级结构，每次new可不太友好，现在创建一个LoggerFactory接口
> 默认的实现类 StaticLoggerFactory.java

- 创建Logger对象
- 匹配 logger name，拆分类名后和已创建（包括配置的）的Logger进行匹配
- 比如当前name为com.aaa.bbb.ccc.XXService，那么name为com/com.aaa/com.aaa.bbb/com.aaa.bbb.ccc
的logger都可以作为parent logger，不过这里需要顺序拆分，优先匹配“最近的”
- 在这个例子里就会优先匹配com.aaa.bbb.ccc这个logger，作为自己的parent
- 如果没有任何一个logger匹配，那么就使用root logger作为自己的parent

### 配置文件设置

> 配置文件需至少需要有以下几个配置功能：
> - 配置 Appender
> - 配置 ILogger
> - 配置 Root Logger

- 最小配置实例
    ```xml
    <configuration>
    
        <appender name="std_plain" class="org.tool.collection.org.tool.collection.mylog.appender.ConsoleAppender">
        </appender>
        <logger name="corg.tool.collection.mylog">
            <appender-ref ref="std_plain"/>
        </logger>
        <root level="trace">
            <appender-ref ref="std_pattern"/>
        </root>
    </configuration>
    ```

- 除了XML之外，还可以考虑 YAML/Properties等形式的配置文件(XMLConfigurator | YAMLConfigurator)
- 配置解析器
    - 解析时，装配LoggerContext，将配置中的Logger/Root Logger/Appender等信息构建完成，填充至
      传入的 LoggerContext
    - 初始化的入口，用于加载/解析配置文件，提供加载/解析后的全局 LoggerContext(ContextInitializer.java)
    - 加载配置文件的方法嵌入 LoggerFactory，让 LoggerFactory.getLogger 的时候自动初始化









































