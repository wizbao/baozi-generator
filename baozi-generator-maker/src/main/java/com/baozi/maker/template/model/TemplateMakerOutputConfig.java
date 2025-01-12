package com.baozi.maker.template.model;

import lombok.Data;

/**
 * @author zwb
 * @date 2025/1/11 21:12
 * @since 2024.0.1
 **/
@Data
public class TemplateMakerOutputConfig {
    // 从未分组文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}
