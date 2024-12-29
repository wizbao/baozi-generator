package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

<#macro generateFile indent fileInfo>
${indent}inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
${indent}outputPath = new File(outputRootPath,"${fileInfo.outputPath}").getAbsolutePath();
<#if fileInfo.generateType == "dynamic">
${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
<#else>
${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
</#if>
</#macro>

/**
 * @author zwb
 * @date 2024/12/15 22:35
 * @since 2024.0.1
 **/
public class MainGenerator {

    public static void doGenerate(DataModel model) throws TemplateException, IOException {
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";
        String inputPath;
        String outputPath;

        <#list modelConfig.models as modelInfo>
            <#if modelInfo.groupKey??>
                <#list modelInfo.models as subModelInfo>
                ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
                </#list>
            <#else >
            ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
            </#if>
        </#list>

        <#list fileConfig.files as fileInfo>
            <#if fileInfo.groupKey??>
                <#if fileInfo.condition??>
                    if(${fileInfo.condition}) {
                    <#list fileInfo.files as fileInfo>
                        <@generateFile indent="     " fileInfo=fileInfo/>
                    </#list>
                    }
                <#else>
                    <#list fileInfo.files as fileInfo>
                        <@generateFile indent="     " fileInfo=fileInfo/>
                    </#list>
                </#if>
            <#else>
                <#if fileInfo.condition??>
                    if(${fileInfo.condition}) {
                    <@generateFile indent="     " fileInfo=fileInfo/>
                    }
                <#else>
                    <@generateFile indent="     " fileInfo=fileInfo/>
                </#if>
            </#if>
         </#list>
    }
}

