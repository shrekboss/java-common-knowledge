Spring AOP 支持10种切点指示符：execution、within、this、target、args、@target、@args、@within、
@annotation、bean下面做下简记(没有写@Pointcut(),请注意)：

**execution: 用来匹配执行方法的连接点的指示符。**
用法相对复杂，格式如下:execution(权限访问符 返回值类型 方法所属的类名包路径.方法名(形参类型) 异常类型)
e.g. execution(public String com.darren.hellxz.test.Test.access(String,String))

权限修饰符和异常类型可省略，返回类型支持通配符，类名、方法名支持*通配，方法形参支持..通配
within: 用来限定连接点属于某个确定类型的类。
within(com.darren.hellxz.test.Test)
within(com.darren.hellxz.test.) //包下类
within(com.darren.hellxz.test..) //包下及子包下

**this和target: this用于没有实现接口的Cglib代理类型，target用于实现了接口的JDK代理目标类型**
举例：this(com.darren.hellxz.test.Foo) //Foo没有实现接口，使用Cglib代理，用this
实现了个接口public class Foo implements Bar{...}
target(com.darren.hellxz.test.Test) //Foo实现了接口的情况

args: 对连接点的参数类型进行限制，要求参数类型是指定类型的实例。
args(Long)

@target: 用于匹配类头有指定注解的连接点
@target(org.springframework.stereotype.Repository)

@args: 用来匹配连接点的参数的，@args指出连接点在运行时传过来的参数的类必须要有指定的注解

@Pointcut("@args(org.springframework.web.bind.annotation.RequestBody)")
public void methodsAcceptingEntities() {}

@within: 指定匹配必须包括某个注解的的类里的所有连接点
@within(org.springframework.stereotype.Repository)

@annotation: 匹配那些有指定注解的连接点
@annotation(org.springframework.stereotype.Repository)

bean: 用于匹配指定Bean实例内的连接点，传入bean的id或name,支持使用*通配符

切点表达式组合
使用&&、||、!、三种运算符来组合切点表达式，表示与或非的关系