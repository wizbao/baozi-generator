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

    /**
     * 文件列表
     */
    private List<FileInfoConfig> files;

    /**
     * 文件分组配置信息
     */
    private FileGroupConfig fileGroupConfig;

    @Data
    public static class FileInfoConfig {
        /**
         * 文件的绝对路径或相对路径
         */
        private String path;

        /**
         * 文件过滤器配置
         */
        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    public static class FileGroupConfig{

        /**
         * 控制条件
         */
        private String condition;

        /**
         * 分组key
         */
        private String groupKey;

        /**
         * 分组名称
         */
        private String groupName;
    }
}
