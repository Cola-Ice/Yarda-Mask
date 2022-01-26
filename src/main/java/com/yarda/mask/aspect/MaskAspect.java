package com.yarda.mask.aspect;

import com.yarda.mask.masker.Masker;
import com.yarda.mask.annotation.MaskField;
import com.yarda.mask.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 脱敏处理切面
 *
 * @Author xuezheng
 * @Date 2021/9/28 10:55
 * @Version 1.0
 */
@Order(-1)
@Slf4j
@Aspect
@Component
public class MaskAspect {

    /**
     * 请求参数：是否需要请求内容，0：不脱敏；1脱敏;默认脱敏
     */
    public static final String REQUEST_PARAM_MASK = "mask";

    @AfterReturning(
            pointcut = "@annotation(com.yarda.mask.annotation.MaskMethod)",
            returning = "jsonResult")
    public void afterReturn(JoinPoint joinPoint, Object jsonResult) {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return;
        }
        if (requireNotMask(request)) {
            this.recordMaskLog(request, joinPoint, jsonResult);
            return;
        }
        long start = System.currentTimeMillis();
        this.handle(jsonResult);
        log.debug("脱敏耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 判断请求方是否请求不脱敏内容
     *
     * @param request 请求对象
     */
    private boolean requireNotMask(HttpServletRequest request) {
        return "0".equals(request.getParameter(REQUEST_PARAM_MASK));
    }

    /**
     * 记录敏感操作日志
     *
     * @param request    请求对象
     * @param joinPoint  切入点
     * @param jsonResult 响应结果
     */
    private void recordMaskLog(HttpServletRequest request, JoinPoint joinPoint, Object jsonResult) {
        // 记录敏感操作日志（异步）
        String ip = IpUtils.getIpAddr(request);
        String uri = request.getRequestURI();
        String methodName = joinPoint.getTarget().getClass().getName() + "#" + joinPoint.getSignature().getName();
        log.info("记录敏感数据查询操作：uri:{},methodName:{},ip:{},result:{}", uri, methodName, ip, jsonResult);
    }

    /**
     * 脱敏处理方法
     *
     * @param result 切入方法返回值
     */
    private void handle(Object result) {
        try {
            if (result == null) {
                return;
            }
            Class<?> cls = result.getClass();
            if (Collection.class.isAssignableFrom(cls)) {
                Collection<?> collection = (Collection<?>) result;
                collection.forEach(this::handle);
            } else if (Map.class.isAssignableFrom(cls)) {
                Map<?, ?> map = (Map<?, ?>) result;
                map.entrySet().forEach(this::handle);
            } else if (cls.isArray()) {
                Object[] array = (Object[]) result;
                for (Object item : array) {
                    this.handle(item);
                }
            } else if (TypeUtils.isNotSimpleProperty(cls)) {
                Field[] fields = cls.getDeclaredFields();
                for (Field field : fields) {
                    if (TypeUtils.isSimpleProperty(field.getType())) {
                        MaskField encryptAnnotation = field.getAnnotation(MaskField.class);
                        if (encryptAnnotation != null) {
                            String fieldName = field.getName();
                            Masker<?> masker = SpringUtils.getBean(encryptAnnotation.encryptHandler());
                            if (masker == null) {
                                masker = encryptAnnotation.encryptHandler().newInstance();
                            }
                            Object encryptValue = masker.mask(ReflectUtils.invokeGetter(result, field.getName()));
                            ReflectUtils.invokeSetter(result, fieldName, encryptValue);
                        }
                    } else {
                        this.handle(ReflectUtils.invokeGetter(result, field.getName()));
                    }
                }
            }
        } catch (Throwable e) {
            log.error("脱敏异常:msg:{},obj:{}", e.getMessage(), result);
        }
    }

}
