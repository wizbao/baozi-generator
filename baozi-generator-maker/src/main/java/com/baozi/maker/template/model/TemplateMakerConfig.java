package com.baozi.maker.template.model;

import com.baozi.maker.meta.Meta;
import lombok.Data;

/**
 * 模板制作配置
 *
 * @author zwb
 * @date 2025/1/11 16:18
 * @since 2024.0.1
 **/
@Data
public class TemplateMakerConfig {

    private Long id;

    private Meta meta = new Meta();

    private String originProjectPath;

    TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();

    TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}



