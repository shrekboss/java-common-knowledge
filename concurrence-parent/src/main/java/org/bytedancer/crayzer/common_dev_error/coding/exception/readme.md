## 异常处理：别让自己在出问题的时候变为瞎子
### 捕获和处理异常容易犯的错：handleexception
1. **不在业务代码层面考虑异常处理，仅在框架层面粗犷捕获和处理异常。**
- 每层架构的工作性质不同，且从业务性质上异常可能分为业务异常和系统异常两大类，这就决定了很难进行
统一的异常处理。(**不建议在框架层面进行异常的自动、统一处理，尤其不要随意捕获异常。但，框架可以做
兜底工作。**)
    - Repository 层出现异常或许可以忽略，或许可以降级，或许需要转化为一个友好的异常。如果一律捕获
    异常仅记录日志，很可能业务逻辑已经出错，而用户和程序本身完全感知不到。
    - Service 层往往涉及数据库事务，出现异常同样不适合捕获，否则事务无法自动回滚。此外 Service 层
    涉及业务逻辑，有些业务逻辑执行中遇到业务异常，可能需要在异常后转入分支业务流程。如果业务异常
    都被框架捕获了，业务功能就会不正常。
    - 如果下层异常上升到 Controller 层还是无法处理的话，Controller 层往往会给予用户友好提示，或是根
    据每一个 API 的异常表返回指定的异常类型，同样无法对所有异常一视同仁。
2. **捕获了异常后直接生吞。**
3. **丢弃异常的原始信息。**
4. **抛出异常时不指定任何消息。**

如果你捕获了异常打算处理的话，除了通过日志正确记录异常原始信息外，通常还有三种处理模式：
- **转换**，即转换新的异常抛出。对于新抛出的异常，最好具有特定的分类和明确的异常消息，而不是随便抛一
个无关或没有任何信息的异常，并最好通过 cause 关联老异常。
- **重试**，即重试之前的操作。比如远程调用服务端过载超时的情况，盲目重试会让问题更严重，需要考虑当前
情况是否适合重试。
- **恢复**，即尝试进行降级处理，或使用默认值来替代原始数据。

### 小心finally中的异常：finallyissue
**虽然 try 中的逻辑出现了异常，但却被 finally 中的异常覆盖了**（为什么被覆盖，原因也很简单，因为一
个方法无法出现两个异常。）
- finally 代码块自己负责异常捕获和处理
- 把 try 中的异常作为主异常抛出，使用 addSuppressed 方法把 finally 中的异常附加到主异常上(正是 
try-with-resources 语句的做法)

对于实现了 AutoCloseable 接口的资源，建议使用 try-with-resources 来释放资源，否则也可能会产生刚才
提到的，释放资源时出现的异常覆盖主异常的问题

### 千万别把异常定义为静态变量：predefinedexception
**异常定义为了静态变量，导致异常栈信息错乱**

### 提交线程池的任务出了异常会怎么样？：threadpoolandexception
**execute 方式**
以 execute 方法提交到线程池的异步任务，最好在任务内部做好异常处理；
`new ThreadFactoryBuilder()
  .setNameFormat(prefix+"%d")
  .setUncaughtExceptionHandler((thread, throwable)-> log.error("ThreadPool {} got exception", thread, throwable))
  .get()`
设置自定义的异常处理程序作为保底，比如在声明线程池时自定义线程池的未捕获异常处理程序：
`
static {
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable)-> log.error("Thread {} got exception", thread, throwable));
}`

**submit 方式**
明线程没退出，异常也没记录被生吞了

查看 FutureTask 源码可以发现，在执行任务出现异常之后，异常存到了一个 outcome 字段中，只有在调用 
get 方法获取 FutureTask 结果的时候，才会以 ExecutionException 的形式重新抛出异常：
`
public void run() {
...
    try {
        Callable<V> c = callable;
        if (c != null && state == NEW) {
            V result;
            boolean ran;
            try {
                result = c.call();
                ran = true;
            } catch (Throwable ex) {
                result = null;
                ran = false;
                setException(ex);
            }
...
}

protected void setException(Throwable t) {
    if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {
        outcome = t;
        UNSAFE.putOrderedInt(this, stateOffset, EXCEPTIONAL); // final state
        finishCompletion();
    }
}

public V get() throws InterruptedException, ExecutionException {
    int s = state;
    if (s <= COMPLETING)
        s = awaitDone(false, 0L);
    return report(s);
}

private V report(int s) throws ExecutionException {
    Object x = outcome;
    if (s == NORMAL)
        return (V)x;
    if (s >= CANCELLED)
        throw new CancellationException();
    throw new ExecutionException((Throwable)x);
}`