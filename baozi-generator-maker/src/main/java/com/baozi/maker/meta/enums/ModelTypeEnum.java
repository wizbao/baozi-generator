package com.baozi.maker.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模型类型枚举
 *
 * @author zwb
 * @date 2024/12/22 21:10
 * @since 2024.0.1
 **/
@Getter
@AllArgsConstructor
public enum ModelTypeEnum {

    STRING("字符串", "String"),
    BOOLEAN("布尔", "boolean");

    private final String text;

    private final String value;

}


