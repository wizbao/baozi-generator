package com.baozi.maker.template.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 过滤范围枚举
 *
 * @author zwb
 * @date 2025/1/4 21:28
 * @since 2024.0.1
 **/
@Getter
@AllArgsConstructor
public enum FileFilterRangeEnum {

    FILE_NAME("文件名称","fileName"),

    FILE_CONTENT("文件内容","fileContent");

    private final String text;

    private final String value;


    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static FileFilterRangeEnum getEnumByValue(String value) {
        return Arrays.stream(FileFilterRangeEnum.values()).filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
    }

}
