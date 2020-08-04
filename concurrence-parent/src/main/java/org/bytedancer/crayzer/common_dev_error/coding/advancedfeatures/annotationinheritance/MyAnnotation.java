package org.bytedancer.crayzer.common_dev_error.coding.advancedfeatures.annotationinheritance;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MyAnnotation {
    String value();
}
