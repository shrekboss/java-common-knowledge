## 别以为“自动挡”就不可能出现OOM
### 太多份相同的对象导致OOM：usernameautocomplete
### 使用WeakHashMap不等于不会OOM：weakhashmapoom
Java 中引用类型和垃圾回收的关系：
- **垃圾回收器不会回收有强引用的对象；**
- **在内存充足时，垃圾回收器不会回收具有软引用的对象；**
- **垃圾回收器只要扫描到了具有弱引用的对象就会回收，WeakHashMap 就是利用了这个特点。**

分析一下 WeakHashMap 的源码，你会发现 WeakHashMap 和 HashMap 的最大区别，是 Entry 对象的实现。
```
private static class Entry<K,V> extends WeakReference<Object> ...
/**
 * Creates new entry.
 */
Entry(Object key, V value,
      ReferenceQueue<Object> queue, 
      int hash, Entry<K,V> next) {
    super(key, queue);
    this.value = value;
    this.hash  = hash;
    this.next  = next;
}
```
**queue 是一个 ReferenceQueue，被 GC 的对象会被丢进这个 queue 里面。**

被丢进 queue 后是如何被销毁的：
```
public V get(Object key) {
    Object k = maskNull(key);
    int h = hash(k);
    Entry<K,V>[] tab = getTable();
    int index = indexFor(h, tab.length);
    Entry<K,V> e = tab[index];
    while (e != null) {
        if (e.hash == h && eq(k, e.get()))
            return e.value;
        e = e.next;
    }
    return null;
}

private Entry<K,V>[] getTable() {
    expungeStaleEntries();
    return table;
}

/**
 * Expunges stale entries from the table.
 */
private void expungeStaleEntries() {
    for (Object x; (x = queue.poll()) != null; ) {
        synchronized (queue) {
            @SuppressWarnings("unchecked")
                Entry<K,V> e = (Entry<K,V>) x;
            int i = indexFor(e.hash, table.length);

            Entry<K,V> prev = table[i];
            Entry<K,V> p = prev;
            while (p != null) {
                Entry<K,V> next = p.next;
                if (p == e) {
                    if (prev == e)
                        table[i] = next;
                    else
                        prev.next = next;
                    // Must not null out e.next;
                    // stale entries may be in use by a HashIterator
                    e.value = null; // Help GC
                    size--;
                    break;
                }
                prev = p;
    ``            p = next;
            }
        }
    }
}
```
可以看到，每次调用 get、put、size 等方法时，都会从 queue 里拿出所有已经被 GC 掉的 key 并删除对应的 Entry 对象。

**ConcurrentReferenceHashMap**
Spring 提供的ConcurrentReferenceHashMap类可以使用弱引用、软引用做缓存，Key 和 Value 同时被软引
用或弱引用包装，也能解决相互引用导致的数据不能释放问题。与 WeakHashMap 相比，
ConcurrentReferenceHashMap 不但性能更好，还可以确保线程安全。

### Tomcat参数配置不合理导致OOM：impropermaxheadersize

建议生产系统的程序配置 JVM 参数启用详细的 GC 日志，方便观察垃圾收集器的行为，并开启 
HeapDumpOnOutOfMemoryError，以便在出现 OOM 时能自动 Dump 留下第一问题现场。
对于 JDK8，你可以这么设置：
`XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=. -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M`