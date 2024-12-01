package com.baozi.generator;

import com.baozi.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author zwb
 * @date 2024/12/1 21:12
 * @since 2024.0.1
 **/
public class DynamicGenerator {

    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "baozi-generator-basic" + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "baozi-generator-basic" + File.separator + "MainTemplate.java";
        MainTemplateConfig model = new MainTemplateConfig();
        model.setLoop(false);
        model.setAuthor("zwb");
        model.setOutputText("sum");
        doGenerate(inputPath, outputPath, model);
    }

    /**
     * 生成文件
     *
     * @param inputPath  模版文件输入路径
     * @param outputPath 文件输出路径
     * @param model      数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir") + File.separator + "baozi-generator-basic";
        System.out.println(projectPath);
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

        // 指定模板文件所在的路径
        File tempFile = new File(inputPath);
        configuration.setDirectoryForTemplateLoading(tempFile.getParentFile());

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        configuration.setNumberFormat("0.######");

        // 创建模板对象，加载指定模板
        Template template = configuration.getTemplate(tempFile.getName());

        // 输出
        Writer out = new FileWriter(outputPath);

        template.process(model, out);

        // 生成文件后别忘了关闭哦
        out.close();

    }
}
