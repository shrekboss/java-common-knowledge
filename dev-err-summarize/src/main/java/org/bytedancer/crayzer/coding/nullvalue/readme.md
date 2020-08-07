## 空值处理：分不清楚的null和恼人的空指针
### 修复和定位恼人的空指针问题：avoidnullpointerexception
[Arthas Java诊断工具](https://alibaba.github.io/arthas/commands.html)
**- 场景**
    - 参数值是 Integer 等包装类型，使用时因为自动拆箱出现了空指针异常；
    - 字符串比较出现空指针异常；
    - 诸如 ConcurrentHashMap 这样的容器不支持 Key 和 Value 为 null，强行 put null 的 Key 或 Value 会出
    现空指针异常；
    - A 对象包含了 B，在通过 A 对象的字段获得 B 之后，没有对字段判空就级联调用 B 的方法出现空指针异常；
    - 方法或远程服务返回的 List 不是空而是 null，没有进行判空就直接调用 List 的方法出现空指针异常。
    
**解决方案**
- 对于 Integer 的判空，可以使用 Optional.ofNullable 来构造一个 Optional，然后使用 orElse(0) 把 null 
替换为默认值再进行 +1 操作。
- 对于 String 和字面量的比较，可以把字面量放在前面，比如"OK".equals(s)，这样即使 s 是 null 也不会出
现空指针异常；
- 而对于两个可能为 null 的字符串变量的 equals 比较，可以使用 Objects.equals，它会做判空处理。
- 对于 ConcurrentHashMap，既然其 Key 和 Value 都不支持 null，修复方式就是不要把 null 存进去。
HashMap 的 Key 和 Value 可以存入 null，而 ConcurrentHashMap 看似是 HashMap 的线程安全版本，却不
支持 null 值的 Key 和 Value，这是容易产生误区的一个地方。
- 对于类似 fooService.getBarService().bar().equals(“OK”) 的级联调用，需要判空的地方有很多，包括 
fooService、getBarService() 方法的返回值，以及 bar 方法返回的字符串。如果使用 if-else 来判空的话可能
需要好几行代码，但使用 Optional 的话一行代码就够了。
- 对于 rightMethod 返回的 List，由于不能确认其是否为 null，所以在调用 size 方法获得列表大小之前，同
样可以使用 Optional.ofNullable 包装一下返回值，然后通过.orElse(Collections.emptyList()) 实现在 List 为 
null 的时候获得一个空的 List，最后再调用 size 方法。

### POJO中属性的null到底代表了什么？：pojonull
null 就是指针没有任何指向，而结合业务逻辑情况就复杂得多，我们需要考虑：
- DTO 中字段的 null 到底意味着什么？是客户端没有传给我们这个信息吗？
- 既然空指针问题很讨厌，那么 DTO 中的字段要设置默认值么？
- 如果数据库实体中的字段有 null，那么通过数据访问框架保存数据是否会覆盖数据库中的既有数据？
### 小心数据库NULL字段的三个坑：dbnull
