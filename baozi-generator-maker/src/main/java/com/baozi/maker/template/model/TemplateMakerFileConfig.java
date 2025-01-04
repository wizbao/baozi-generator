package com.baozi.maker.template.model;

import lombok.Data;

import java.util.List;

/**
 * @author zwb
 * @date 2025/1/4 21:24
 * @since 2024.0.1
 **/
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    @Data
    public static class FileInfoConfig {
        /**
         * 文件的绝对路径或相对路径
         */
        private String path;

        private List<FileFilterConfig> filterConfigList;
    }
}
