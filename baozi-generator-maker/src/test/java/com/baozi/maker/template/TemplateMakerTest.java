package com.baozi.maker.template;

import com.baozi.maker.meta.Meta;
import com.baozi.maker.template.model.TemplateMakerFileConfig;
import com.baozi.maker.template.model.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TemplateMakerTest {

    @Test
    public void test1() {
        Meta meta = new Meta();
        String name = "acm-template-pro-generator2";
        String description = "ACM 示例模板生成器2";
        meta.setName(name);
        meta.setDescription(description);

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "samples/springboot-init-master";
        // String fileInputPath = "src/main/java/com/yupi/springbootinit/annotation/AuthCheck.java";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/esdao";
        String fileInputPath2 = "src/main/java/com/yupi/springbootinit/exception";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig file1 = new TemplateMakerFileConfig.FileInfoConfig();

        String inputFilePath2 = "src/main/resources/application.yml";

        file1.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(file1));


        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig3 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig3.setFieldName("password");
        modelInfoConfig3.setType("String");
        modelInfoConfig3.setDefaultValue("root");
        modelInfoConfig3.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2, modelInfoConfig3);
        templateMakerModelConfig.setModels(modelInfoConfigList);


        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 2L);
        System.out.println("id = " + id);

    }

    @Test
    public void test2() {
        Meta meta = new Meta();
        String name = "acm-template-pro-generator2";
        String description = "ACM 示例模板生成器2";
        meta.setName(name);
        meta.setDescription(description);

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "samples/springboot-init-master";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig file1 = new TemplateMakerFileConfig.FileInfoConfig();

        String inputFilePath2 = "./";

        file1.setPath(inputFilePath2);
        templateMakerFileConfig.setFiles(Arrays.asList(file1));


        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setReplaceText("BaseResponse");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);


        long id = TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 2L);

    }
}