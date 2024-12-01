package com.baozi.generator;

import com.baozi.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

import static com.baozi.generator.DynamicGenerator.doGenerate;
import static com.baozi.generator.StaticGenerator.copyFilesByHutool;

/**
 * 生成动态和静态文件
 *
 * @author zwb
 * @date 2024/12/1 22:04
 * @since 2024.0.1
 **/
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException {
        // 生成静态文件
        String projectPath = System.getProperty("user.dir");
        String srcPath = projectPath + File.separator + "samples" + File.separator + "acm-template";
        String tarPath = projectPath;
        copyFilesByHutool(srcPath, tarPath);

        // 生成动态文件
        String inputPath = projectPath + File.separator + "baozi-generator-basic" + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        MainTemplateConfig model = new MainTemplateConfig();
        model.setLoop(true);
        model.setAuthor("zwb");
        model.setOutputText("output result");
        doGenerate(inputPath, outputPath, model);

    }
}
