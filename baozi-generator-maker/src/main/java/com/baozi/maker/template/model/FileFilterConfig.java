package com.baozi.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author zwb
 * @date 2025/1/4 21:22
 * @since 2024.0.1
 **/
@Data
@Builder
public class FileFilterConfig {

    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
