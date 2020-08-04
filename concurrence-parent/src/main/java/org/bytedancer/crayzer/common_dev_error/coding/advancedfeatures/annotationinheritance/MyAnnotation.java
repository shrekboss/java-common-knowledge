package org.bytedancer.crayzer.common_dev_error.coding.advancedfeatures.annotationinheritance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// 不使用 @Inherited 子类以及子类的方法，无法自动继承父类注解
// @Inherited
public @interface MyAnnotation {
    String value();
}
