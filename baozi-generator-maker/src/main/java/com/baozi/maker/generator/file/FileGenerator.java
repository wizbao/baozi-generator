package com.baozi.maker.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 生成动态和静态文件
 *
 * @author zwb
 * @date 2024/12/1 22:04
 * @since 2024.0.1
 **/
public class FileGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {
        // 生成静态文件
        String projectPath = System.getProperty("user.dir");
        String parentPath = new File(projectPath).getParent();
        String srcPath = parentPath + File.separator + "samples" + File.separator + "acm-template";
        StaticFileGenerator.copyFilesByHutool(srcPath, parentPath);
        // 生成动态文件
        String inputPath = parentPath + File.separator + "baozi-generator-maker" + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = parentPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputPath, outputPath, model);
    }
}
