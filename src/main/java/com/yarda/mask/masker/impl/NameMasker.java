package com.yarda.mask.masker.impl;

import com.yarda.mask.masker.Masker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 名称脱敏节点
 *
 * @Author xuezheng
 * @Date 2021/9/29 15:01
 * @Version 1.0
 */
@Slf4j
@Component
public class NameMasker implements Masker<String> {

    @Override
    public String encrypt(String value) {
        String result = null;
        try {
            if (!StringUtils.isEmpty(value)) {
                int length = value.length();
                if (length == 1) {
                    result = value.substring(0, 1);
                } else if (length == 2) {
                    result = value.substring(0, 1).concat("*");
                } else if (length == 3) {
                    result = value.substring(0, 1).concat("*").concat(value.substring(2));
                } else {
                    result = value.substring(0, 2).concat("*").concat(value.substring(3));
                }
            }
        } catch (Exception e) {
            log.info("脱敏失败:value:{};msg:{}", value, e.getMessage());
        }
        return result;
    }
}
