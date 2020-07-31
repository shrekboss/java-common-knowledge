## 判等问题：程序里如何确定你就是你？
### 注意equals和==的区别：intandstringequal
### 实现一个equals没有这么简单：equalitymethod
### hashCode和equals要配对实现：equalitymethod
### 注意compareTo和equals的逻辑一致性：compareto
- 对于自定义的类型，如果要实现 Comparable，请记得 equals、hashCode、compareTo 三者逻辑一致。
### 小心Lombok生成代码的“坑”：lombokequals
### 补充）不同类加载器加载相同类的坑：differentclassloaderequals
