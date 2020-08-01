AsyncAppender异步记录日志出现坑
- queueSize 设置得特别大，就可能会导致 OOM。
- queueSize 设置得比较小（默认值就非常小，256），且 discardingThreshold 设置为大于 0 的值（或者为
默认值），队列剩余容量少于 discardingThreshold 的配置就会丢弃 <=INFO 的日志。这里的坑点有两个。
    - 一是，因为 discardingThreshold 的存在，设置 queueSize 时容易踩坑。比如，本例中最大日志并发是 
    1000，即便设置 queueSize 为 1000 同样会导致日志丢失。
    - 二是，discardingThreshold 参数容易有歧义，它不是百分比，而是日志条数。对于总容量 10000 的队列，
    如果希望队列剩余容量少于 1000 条的时候丢弃，需要配置为 1000。
- neverBlock 默认为 false，意味着总可能会出现阻塞。如果 discardingThreshold 为 0，那么队列满时再有
日志写入就会阻塞；如果 discardingThreshold 不为 0，也只会丢弃 <=INFO 级别的日志，那么出现大量错误
日志时，还是会阻塞程序。可以看出 queueSize、discardingThreshold 和 neverBlock 这三个参数息息相关，
务必按需进行设置和取舍，到底是性能为先，还是数据不丢为先：
    - 如果考虑绝对性能为先，那就设置 neverBlock 为 true，永不阻塞。
    - 如果考虑绝对不丢数据为先，那就设置 discardingThreshold 为 0，即使是 <=INFO 的级别日志也不会
    丢，但最好把 queueSize 设置大一点，毕竟默认的 queueSize 显然太小，太容易阻塞。
    - 如果希望兼顾两者，可以丢弃不重要的日志，把 queueSize 设置大一点，再设置一个合理的 discardingThreshold。