package com.yarda.mask.annotation;


import com.yarda.mask.Masker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密属性注解
 *
 * @Author xuezheng
 * @Date 2021/9/28 10:55
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskField {
    /**
     * 脱敏节点
     */
    Class<? extends Masker<?>> encryptHandler();
}
