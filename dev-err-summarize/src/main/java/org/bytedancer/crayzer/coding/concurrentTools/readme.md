## 使用了并发工具类库，线程安全就高枕无忧了吗？
### 没有意识到线程重用导致用户信息错乱的Bug：threadlocal
### 使用了线程安全的并发工具，并不代表解决了所有线程安全问题：concurrenthashmapmisuse
### 没有充分了解并发工具的特性，从而无法发挥其威力：concurrenthashmapperformance
### 没有认清并发工具的使用场景，因而导致性能问题：copyonwritelistmisuse
### （补充）putIfAbsent vs computeIfAbsent的一些特性比对：ciavspia
### （补充）异步执行多个子任务等待所有任务结果汇总处理的例子：multiasynctasks

**Question 1**：ThreadLocalRandom，是否可以把它的实例设置到静态变量中，在多线程情况下重用呢？
ThreadLocalRandom#current() 的时候初始化一个初始化种子到线程，每次 nextseed 再使用之前的种子生成
新的种子：UNSAFE.putLong(t = Thread.currentThread(), SEED, r = UNSAFE.getLong(t, SEED) + GAMMA);

如果通过主线程调用一次 current 生成一个 ThreadLocalRandom 的实例保存起来，那么其它线程来获取种子
的时候必然取不到初始种子，必须是每一个线程自己用的时候初始化一个种子到线程。**可以在 nextSeed 方
法设置一个断点来测试：UNSAFE.getLong(Thread.currentThread(),SEED);**

**Question 2**：computeIfAbsent 和 putIfAbsent 方法的区别？
1. 当 Key 存在的时候，如果 Value 的获取比较昂贵的话，**putIfAbsent** 方法就会白白浪费时间在获取这
个昂贵的 Value 上（这个点特别注意），而 **computeIfAbsent** 则会因为传入的是 Lambda 表达式而不
是实际值不会有这个问题。

2. **Key 不存在的时候**，putIfAbsent 会返回 null，这时候要小心空指针；而 computeIfAbsent 会返回计
算后的值，不存在空指针的问题。

3. **当 Key 不存在的时候**，putIfAbsent 允许 put null 进去，而 computeIfAbsent 不能（当然了，此条
针对 HashMap，ConcurrentHashMap 不允许 put null value 进去）。