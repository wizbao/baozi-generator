package com.baozi.maker.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 文件过滤规则枚举
 *
 * @author zwb
 * @date 2025/1/4 21:35
 * @since 2024.0.1
 **/
@Getter
@AllArgsConstructor
public enum FileFilterRuleEnum {
    CONTAINS("包含", "contains"),
    STARTS_WITH("前缀匹配", "startsWith"),
    ENDS_WITH("后缀匹配", "endsWith"),
    REGEX("正则", "regex"),
    EQUALS("相等", "equals");

    private final String text;

    private final String value;

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static FileFilterRuleEnum getEnumByValue(String value) {
        return Arrays.stream(FileFilterRuleEnum.values()).filter(item -> item.getValue().equals(value)).findFirst().orElse(null);

    }
}


