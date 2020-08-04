## 当反射、注解和泛型遇到OOP时，会有哪些坑？
### 反射调用方法不是以传参决定重载：reflectionissue
**使用反射时的误区是，认为反射调用方法还是根据入参确定方法重载**

### 泛型经过类型擦除多出桥接方法的坑：genericandinheritance
使用反射查询类方法清单时，要注意两点：
- getMethods 和 getDeclaredMethods 是有区别的，前者可以查询到父类方法，后者只能查询到当前类。
- **反射进行方法调用要注意过滤桥接方法。**

### 注解可以继承吗？：annotationinheritance
### （补充）内部类的例子：innerclass
