package org.bytedancer.crayzer.coding.advancedfeatures.annotationinheritance;

import java.lang.annotation.*;

/**
 * @author yizhe.chen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/*
 *不使用 @Inherited，子类无法自动继承父类注解，使用也无法继承父类方法注解
 */
@Inherited
public @interface MyAnnotation {
    String value();
}
