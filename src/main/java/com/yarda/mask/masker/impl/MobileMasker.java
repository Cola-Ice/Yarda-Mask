package com.yarda.mask.masker.impl;

import com.yarda.mask.masker.Masker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 手机号脱敏节点
 *
 * @Author xuezheng
 * @Date 2021/9/29 14:49
 * @Version 1.0
 */
@Slf4j
@Component
public class MobileMasker implements Masker<String> {
    /**
     * 手机号长度
     */
    private static final int MOBILE_NUMBER_LENGTH = 11;

    @Override
    public String mask(String value) {
        String result = null;
        try {
            if (value != null && value.length() == MOBILE_NUMBER_LENGTH) {
                result = value.substring(0, 3).concat("****").concat((value).substring(7));
            }
        } catch (Exception e) {
            log.error("脱敏失败:value:{};msg:{}", value, e.getMessage());
        }
        return result;
    }
}
