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
**EvaluatorFilter 的源码发现：** 求值过滤器，用于判断日志是否符合某个条件。大量日志输出到文件中，
日志文件会非常大，如果性能测试结果也混在其中的话，就很难找到那条日志。所以，使用 EvaluatorFilter 
对日志按照标记进行过滤，并将过滤出的日志单独输出到控制台上。

和 ThresholdFilter 不同的是，LevelFilter 仅仅配置 level 是无法真正起作用的。**由于没有配置 onMatch 和 
onMismatch 属性，所以相当于这个过滤器是无用的，导致 INFO 以上级别的日志都记录了。**

### 使用异步日志改善性能的坑：async
FileAppender 继承自 OutputStreamAppender，查看 OutputStreamAppender 源码
```java
public class OutputStreamAppender<E> extends UnsynchronizedAppenderBase<E> {
  private OutputStream outputStream;
  boolean immediateFlush = true;
  @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }
        subAppend(eventObject);
    }

    protected void subAppend(E event) {
        if (!isStarted()) {
            return;
        }
        try {
            //编码LoggingEvent
            byte[] byteArray = this.encoder.encode(event);
            //写字节流
            writeBytes(byteArray);
        } catch (IOException ioe) {
            //...
        }
    }

    private void writeBytes(byte[] byteArray) throws IOException {
        if(byteArray == null || byteArray.length == 0)
            return;
        
        lock.lock();
        try {
            // 在追加日志的时候，是直接把日志写入 OutputStream 中，属于同步记录日志：
            //这个OutputStream其实是一个ResilientFileOutputStream，其内部使用的是带缓冲的BufferedOutputStream
            this.outputStream.write(byteArray);
            if (immediateFlush) {
                this.outputStream.flush();//刷入OS
            }
        } finally {
            lock.unlock();
        }
    }
}
```
AsyncAppender 异步日志的坑
- 记录异步日志撑爆内存；
- 记录异步日志出现日志丢失；
- 记录异步日志出现阻塞。

```java
public class AsyncAppender extends AsyncAppenderBase<ILoggingEvent> {
    boolean includeCallerData = false;//是否收集调用方数据
    protected boolean isDiscardable(ILoggingEvent event) {
        Level level = event.getLevel();
        return level.toInt() <= Level.INFO_INT;//丢弃<=INFO级别的日志
    }
    protected void preprocess(ILoggingEvent eventObject) {
        eventObject.prepareForDeferredProcessing();
        if (includeCallerData)
            eventObject.getCallerData();
    }
}
public class AsyncAppenderBase<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {

    BlockingQueue<E> blockingQueue;//异步日志的关键，阻塞队列
    public static final int DEFAULT_QUEUE_SIZE = 256;//默认队列大小
    int queueSize = DEFAULT_QUEUE_SIZE;
    static final int UNDEFINED = -1;
    int discardingThreshold = UNDEFINED;
    boolean neverBlock = false;//控制队列满的时候加入数据时是否直接丢弃，不会阻塞等待

    @Override
    public void start() {
         ...
        blockingQueue = new ArrayBlockingQueue<E>(queueSize);
        if (discardingThreshold == UNDEFINED)
            discardingThreshold = queueSize / 5;//默认丢弃阈值是队列剩余量低于队列长度的20%，参见isQueueBelowDiscardingThreshold方法
        ...
    }

    @Override
    protected void append(E eventObject) {
        if (isQueueBelowDiscardingThreshold() && isDiscardable(eventObject)) { //判断是否可以丢数据
            return;
        }
        preprocess(eventObject);
        put(eventObject);
    }

    private boolean isQueueBelowDiscardingThreshold() {
        return (blockingQueue.remainingCapacity() < discardingThreshold);
    }

    private void put(E eventObject) {
        if (neverBlock) { //根据neverBlock决定使用不阻塞的offer还是阻塞的put方法
            blockingQueue.offer(eventObject);
        } else {
            putUninterruptibly(eventObject);
        }
    }
    //以阻塞方式添加数据到队列
    private void putUninterruptibly(E eventObject) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    blockingQueue.put(eventObject);
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
}  
```
- includeCallerData 用于控制是否收集调用方数据，默认是 false，此时方法行号、方法名等信息将不能显示
（源码第 2 行以及 7 到 11 行）。
- queueSize 用于控制阻塞队列大小，使用的 ArrayBlockingQueue 阻塞队列（源码第 15 到 17 行），默认大
小是 256，即内存中最多保存 256 条日志。
- discardingThreshold 是控制丢弃日志的阈值，主要是防止队列满后阻塞。默认情况下，**队列剩余量低于队
列长度的 20%**，就会丢弃 TRACE、DEBUG 和 INFO 级别的日志。
（参见源码第 3 到 6 行、18 到 19 行、26 到 27 行、33 到 34 行、40 到 42 行）
- neverBlock 用于控制队列满的时候，加入的数据是否直接丢弃，不会阻塞等待，默认是 false（源码第 44 
到 68 行）。这里需要注意一下 offer 方法和 put 方法的区别，当队列满的时候 offer 方法不阻塞，而 put 
方法会阻塞；neverBlock 为 true 时，使用 offer 方法。
### 使用日志占位符就不需要进行日志级别判断了：placeholder
SLF4J 的{}占位符语法，到真正记录日志时才会获取实际参数，因此解决了日志数据获取的性能问题?
- 使用{}占位符语法不能通过延迟参数值获取，来解决日志数据获取的性能问题。
- 事先判断日志级别。
- 通过 lambda 表达式进行延迟参数内容获取。但，SLF4J 的 API 还不支持 lambda，因此需要使用 Log4j2 
日志 API，把 Lombok 的 @Slf4j 注解替换为 @Log4j2 注解，这样就可以提供一个 lambda 表达式作为提供
参数数据的方法(真正的日志记录还是走的 Logback 框架)

**日志框架提供的参数化日志记录方式不能完全取代日志级别的判断。如果日志量很大，获取日志参数代价也很
大，就要进行相应日志级别的判断，避免不记录日志也要花费时间获取日志参数的问题。**