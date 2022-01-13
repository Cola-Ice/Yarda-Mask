package com.yarda.mask.utils;

import org.springframework.util.ClassUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 类型工具类
 *
 * @Author xuezheng
 * @Date 2021/12/2 10:33
 * @Version 1.0
 */
public class TypeUtils {
    /**
     * 判断类型是否是一个简单的属性
     *
     * @param cls 类型
     * @return true/false
     */
    public static boolean isSimpleProperty(Class<?> cls) {
        if (ClassUtils.isPrimitiveOrWrapper(cls)) {
            return true;
        } else if (cls == String.class) {
            return true;
        } else if (cls == BigDecimal.class) {
            return true;
        } else if (cls == BigInteger.class) {
            return true;
        } else if (cls == Date.class) {
            return true;
        } else if (cls == java.sql.Date.class) {
            return true;
        } else if (cls == Time.class) {
            return true;
        } else if (cls == Timestamp.class) {
            return true;
        }
//        else if (cls.isEnum()) {  // 枚举不是一个简单类型
//            return true;
//        }
        else if (cls == Calendar.class) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断类型是否不是一个简单的属性
     *
     * @param cls 类型
     * @return true/false
     */
    public static boolean isNotSimpleProperty(Class<?> cls) {
        return !isSimpleProperty(cls);
    }
}
