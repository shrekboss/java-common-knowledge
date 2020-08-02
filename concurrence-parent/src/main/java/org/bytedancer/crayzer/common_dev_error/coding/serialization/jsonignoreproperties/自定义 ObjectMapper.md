## 自定义 ObjectMapper
>希望修改一下 ObjectMapper 的行为，让枚举序列化为索引值而不是字符串值，比如默认情况下序列化一个
> Color 枚举中的 Color.BLUE 会得到字符串 BLUE

`
@Bean
public ObjectMapper objectMapper(){
    ObjectMapper objectMapper=new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX,true);
    return objectMapper;
}`

### 开启这个特性后，Color.BLUE 枚举序列化成索引值 1:
[16:11:37.382] [http-nio-45678-exec-1] [INFO ] [c.s.d.JsonIgnorePropertiesController:19 ] - color:1

### 修改后处理枚举序列化的逻辑是满足了要求，但线上爆出了大量 400 错误，日志中也出现了很多 
UnrecognizedPropertyException：
`
JSON parse error: Unrecognized field \"ver\" (class ...UserWrong), not marked as ignorable; nested 
exception is com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field 
\"version\" (class ...UserWrong), not marked as ignorable (one known property: \"name\"])\n at 
[Source: (PushbackInputStream); line: 1, column: 22] (through reference chain: UserWrong[\"ver\"])`
从异常信息中可以看到，这是因为反序列化的时候，**原始数据多了一个 version 属性**。进一步分析发现，使用
了 UserWrong 类型作为 Web 控制器 wrong 方法的入参，其中只有一个 name 属性

### 客户端实际传过来的数据多了一个 version 属性。那，为什么之前没这个问题呢？
问题就出在:
1. 自定义 ObjectMapper 启用 WRITE_ENUMS_USING_INDEX 序列化功能特性时，覆盖了 Spring Boot 自
动创建的 ObjectMapper；
2. 而这个自动创建的 ObjectMapper 设置过 FAIL_ON_UNKNOWN_PROPERTIES 反序列化特性为 false，
以确保出现未知字段时不要抛出异常。源码如下：

```java
public MappingJackson2HttpMessageConverter() {
  this(Jackson2ObjectMapperBuilder.json().build());
}

public class Jackson2ObjectMapperBuilder {
    ...
  private void customizeDefaultFeatures(ObjectMapper objectMapper) {
    if (!this.features.containsKey(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
      configureFeature(objectMapper, MapperFeature.DEFAULT_VIEW_INCLUSION, false);
    }
    if (!this.features.containsKey(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
      configureFeature(objectMapper, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
  }
}
```

### 修复这个问题，有三种方式：
- 第一种，同样禁用自定义的 ObjectMapper 的 FAIL_ON_UNKNOWN_PROPERTIES：
`objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
- 第二种，设置自定义类型，加上 @JsonIgnoreProperties 注解，开启 ignoreUnknown 属性，以实现反序列
化时忽略额外的数据：
```java
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRight {
    private String name;
}
```
- 第三种，不要自定义 ObjectMapper，而是直接在配置文件设置相关参数，来修改 Spring 默认的 
ObjectMapper 的功能。比如，直接在配置文件启用把枚举序列化为索引号：
`spring.jackson.serialization.write_enums_using_index=true`
或者可以直接定义 Jackson2ObjectMapperBuilderCustomizer Bean 来启用新特性：
`
@Bean
public Jackson2ObjectMapperBuilderCustomizer customizer(){
    return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
}`

### 总结
- Jackson 针对序列化和反序列化有大量的细节功能特性，可以参考 Jackson 官方文档来了解这些特性，
详见
[SerializationFeature](https://fasterxml.github.io/jackson-databind/javadoc/2.10/com/fasterxml/jackson/databind/SerializationFeature.html)
[DeserializationFeature](https://fasterxml.github.io/jackson-databind/javadoc/2.10/com/fasterxml/jackson/databind/DeserializationFeature.html)
[MapperFeature](https://fasterxml.github.io/jackson-databind/javadoc/2.10/com/fasterxml/jackson/databind/MapperFeature.html)
- 忽略多余字段，是写业务代码时最容易遇到的一个配置项。Spring Boot 在自动配置时贴心地做了全局设置。
如果需要设置更多的特性，可以直接修改配置文件 spring.jackson.** 或设置 Jackson2ObjectMapperBuilderCustomizer 
回调接口，来启用更多设置，无需重新定义 ObjectMapper Bean。














