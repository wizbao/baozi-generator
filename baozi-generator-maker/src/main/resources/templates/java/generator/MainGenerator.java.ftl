package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author zwb
 * @date 2024/12/15 22:35
 * @since 2024.0.1
 **/
public class MainGenerator {

    public static void doGenerate(Object model) throws TemplateException, IOException {
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";
        String inputPath;
        String outputPath;
        <#list fileConfig.files as fileInfo>
        <#if fileInfo.generateType == "dynamic">
         // 生成动态文件
         inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
         outputPath = new File(outputRootPath,"${fileInfo.outputPath}").getAbsolutePath();
         DynamicGenerator.doGenerate(inputPath, outputPath, model);
         <#else>
         // 生成静态文件
         StaticGenerator.copyFilesByHutool(inputPath, outputPath);
         </#if>
         </#list>
    }
}

