package com.baozi.maker.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 *
 * @author zwb
 * @date 2024/12/22 21:09
 * @since 2024.0.1
 **/
@Getter
@AllArgsConstructor
public enum FileGenerateTypeEnum {

    DYNAMIC("动态", "dynamic"),
    STATIC("静态", "static");

    private final String text;

    private final String value;

}

