package com.baozi.maker.template.model;

import lombok.Data;

import java.util.List;

/**
 * @author zwb
 * @date 2025/1/5 19:38
 * @since 2024.0.1
 **/
@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> models;

    private ModelGroupConfig modelGroupConfig;

    @Data
    public static class ModelInfoConfig {

        private String fieldName;

        private String type;

        private String description;

        private Object defaultValue;

        private String abbr;

        // 用于替换哪些文本
        private String replaceText;
    }

    @Data
    public static class ModelGroupConfig {

        private String condition;

        private String groupKey;

        private String groupName;

        private String type;

        private String description;
    }
}
