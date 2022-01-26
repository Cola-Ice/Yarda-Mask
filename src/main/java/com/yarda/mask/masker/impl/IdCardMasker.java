package com.yarda.mask.masker.impl;

import com.yarda.mask.masker.Masker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 身份证号脱敏节点
 *
 * @Author xuezheng
 * @Date 2021/9/29 11:54
 * @Version 1.0
 */
@Slf4j
@Component
public class IdCardMasker implements Masker<String> {
    /**
     * 身份证长度
     */
    private static final int ID_CARD_LENGTH = 18;

    @Override
    public String mask(String value) {
        String result = null;
        try {
            if (value != null && value.length() == ID_CARD_LENGTH) {
                result = value.substring(0, 6).concat("********").concat((value).substring(14));
            }
        } catch (Exception e) {
            log.error("脱敏失败:value:{};msg:{}", value, e.getMessage());
        }
        return result;
    }
}
