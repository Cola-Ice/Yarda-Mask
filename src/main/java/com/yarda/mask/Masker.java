package com.yarda.mask;

/**
 * 脱敏节点
 *
 * @Author xuezheng
 * @Date 2021/9/29 11:48
 * @Version 1.0
 */
public interface Masker<T> {
    /**
     * 脱敏方法
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    public T encrypt(T value);
}
