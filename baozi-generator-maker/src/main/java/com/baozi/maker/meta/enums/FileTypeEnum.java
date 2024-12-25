package com.baozi.maker.meta.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件生成类型枚举
 *
 * @author zwb
 * @date 2024/12/22 21:08
 * @since 2024.0.1
 **/
@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    DIR("目录", "dir"),
    FILE("文件", "file");

    private final String text;

    private final String value;

}
