## 序列化：一来一回，你还是原来的你吗？
使用 RedisTemplate 来操作 Redis 进行是数据缓存，相比于 Jedis ，除了无需考虑连接池、更方便外，还可
以与 Spring Cache 等其他组件无缝整合。如果使用 Spring Boot 的话，无需任何配置就可以直接使用。

### 序列化和反序列化需要确保算法一致：redistemplate
RedisTemplate#afterPropertiesSet，默认情况下 RedisTemplate 针对 Key 和 Value 使用了 JDK 序列化。
StringRedisTemplate 对于 Key 和 Value，使用的是 String 序列化方式

Spring 提供的 4 种 RedisSerializer（Redis 序列化器）：
- 默认情况下，RedisTemplate 使用 JdkSerializationRedisSerializer，也就是 JDK 序列化，容易产生 Redis
 中保存了乱码的错觉。
 - 通常考虑到易读性，可以设置 Key 的序列化器为 StringRedisSerializer。但直接使用 RedisSerializer.string()，
 相当于使用了 UTF_8 编码的 StringRedisSerializer，需要注意字符集问题。
 - 如果希望 Value 也是使用 JSON 序列化的话，可以把 Value 序列化器设置为 Jackson2JsonRedisSerializer。
 **默认情况下，不会把类型信息保存在 Value 中，即使我们定义 RedisTemplate 的 Value 泛型为实际类型，
 查询出的 Value 也只能是 LinkedHashMap 类型。** 
    - 如果希望直接获取真实的数据类型，你可以启用 **Jackson ObjectMapper 的 activateDefaultTyping** 
    方法，把类型信息一起序列化保存在 Value 中。
    - 如果希望 Value 以 JSON 保存并带上类型信息，更简单的方式是，直接使用 **RedisSerializer.json()** 
    快捷方法来获取序列化器。

### 注意Jackson JSON反序列化对额外字段的处理：jsonignoreproperties
通过设置 JSON 序列化工具 Jackson 的 activateDefaultTyping 方法，可以在序列化数据时写入对象类型

在开发 Spring Web 应用程序时，如果自定义了 ObjectMapper，并把它注册成了 Bean，那很可能会导致 
Spring Web 使用的 ObjectMapper 也被替换，导致 Bug。

案例分析：参考【自定义 ObjectMapper.md】，**使用 Jackson 反序列化时，要注意忽略额外字段的问题**

### 反序列化时要小心类的构造方法：deserializationconstructor
**使用 Jackson 反序列化时，小心类的构造方法**
默认情况下，在反序列化的时候，Jackson 框架只会调用无参构造方法创建对象。如果走自定义的构造方法创
建对象，需要通过 @JsonCreator 来指定构造方法，并通过 @JsonProperty 设置构造方法中参数对应的 
JSON 属性名

### 枚举作为API接口参数或返回值的两个大坑：enumusedinapi
**enumusedinapi/getOrderStatusClient**
JSON parse error: Cannot deserialize value of type `...StatusEnumClient` from String "CANCELED": not 
one of the values accepted for Enum class: [CREATED, FINISHED, DELIVERED, PAID];
- **第一个坑是，客户端和服务端的枚举定义不一致时，会出异常**
1. 可以开启 Jackson 的 read_unknown_enum_values_using_default_value 反序列化特性，也就是在枚
举值未知的时候使用默认值
2. 并为枚举添加一个默认值，使用 @JsonEnumDefaultValue 注解注释，需要注意的是，这个枚举值一定是
添加在客户端 StatusEnumClient 中的，因为反序列化使用的是客户端枚举。
3. 仅仅这样配置还不能让 RestTemplate 生效这个反序列化特性，还需要配置 RestTemplate，来使用 
Spring Boot 的 MappingJackson2HttpMessageConverter 才行
    
**enumusedinapi/getOrderStatusClient**
[21:49:03.887] [http-nio-45678-exec-1] [INFO ] [o.g.t.c.e.e.EnumUsedInAPIController:25  ] - result UNKNOWN

- 第二个坑，也是更大的坑，**枚举序列化反序列化实现自定义的字段非常麻烦，会涉及 Jackson 的 Bug**
【参考 序列化反序列化使用枚举自定义字段的问题.md】

### 序列化版本号问题：serialversionissue