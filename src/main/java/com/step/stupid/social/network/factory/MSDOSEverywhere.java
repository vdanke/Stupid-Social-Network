package com.step.stupid.social.network.factory;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MSDOSEverywhere {

    String operationSystem() default "MS-DOS";
}
