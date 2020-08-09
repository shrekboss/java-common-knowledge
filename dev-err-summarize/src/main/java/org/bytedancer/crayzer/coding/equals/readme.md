## 判等问题：程序里如何确定你就是你？
### 注意equals和==的区别：intandstringequal
### 实现一个equals没有这么简单：equalitymethod
### hashCode和equals要配对实现：equalitymethod
### 注意compareTo和equals的逻辑一致性：compareto
- 对于自定义的类型，如果要实现 Comparable，请记得 equals、hashCode、compareTo 三者逻辑一致。
### 小心Lombok生成代码的“坑”：lombokequals

### 补充）不同类加载器加载相同类的坑：differentclassloaderequals
> equals比较的对象除了所谓的相等外，还有一个非常重要的因素，就是该对象的类加载器也必须是同一个，
> 不然equals返回的肯定是false；之前遇到过一个坑：重启后，两个对象相等，结果是true，但是修改了某些
> 东西后，热加载（不用重启即可生效）后，再次执行equals，返回就是false，因为热加载使用的类加载器和
> 程序正常启动的类加载器不同。
>
> 关于类加载器部分，JDK 9 之前的 Java 应用都是由「启动类加载器」、「扩展类加载器」、
>「应用程序类加载器」这三种类加载器互相配合来完成加载的，如果有需要还可以加入自定义的类加载器来
> 进行拓展；JDK 9 为了模块化的支持，对双亲委派模式做了一些改动：扩展类加载器被平台类加载
> 器（Platform ClassLoader）取代。平台类加载器和应用程序类加载器都不再继承自 
> java.net.URLClassLoader，而是继承于 jdk.internal.loader.BuiltinClassLoader。具体细节可以自行搜索。
