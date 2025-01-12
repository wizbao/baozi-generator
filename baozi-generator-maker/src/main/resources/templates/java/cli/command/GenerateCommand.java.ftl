package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

<#macro generateOption indent modelInfo>
<#if modelInfo.description??>
${indent}/**
${indent}* description = "${modelInfo.description}"
${indent}*/
</#if>
${indent}@CommandLine.Option(names = {"--${modelInfo.fieldName}"<#if modelInfo.abbr??>,"-${modelInfo.abbr}"</#if>}, <#if modelInfo.description??>description = "${modelInfo.description}", </#if>interactive = true, arity = "0..1", echo = true)
${indent}private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
</#macro>

<#macro generateCommand indent modelInfo>
${indent}System.out.println("输入${modelInfo.groupName}配置：");
${indent}CommandLine ${modelInfo.groupKey}commandLine = new CommandLine(${modelInfo.type}Command.class);
${indent}${modelInfo.groupKey}commandLine.execute(${modelInfo.allArgsStr});
</#macro>

/**
 * @author zwb
 * @date 2024/12/11 23:42
 * @since 2024.0.1
 **/
@Data
@CommandLine.Command(name = "generate", description = "生成模版文件", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {
    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
            /**
            * ${modelInfo.groupName}
            */
            static DataModel.${modelInfo.type} ${modelInfo.groupKey} = new DataModel.${modelInfo.type}();

            @Data
            @CommandLine.Command(name = "${modelInfo.groupKey}")
            public static class ${modelInfo.type}Command implements Runnable{
            <#list modelInfo.models as subModelInfo>
            <@generateOption indent="   " modelInfo=subModelInfo/>
            </#list>

                @Override
                public void run() {
                    <#list modelInfo.models as subModelInfo>
                    ${modelInfo.groupKey}.${subModelInfo.fieldName} = ${subModelInfo.fieldName};
                    </#list>
                }
            }
        <#else>
            <@generateOption indent="   " modelInfo=modelInfo/>
        </#if>
    </#list>

    @Override
    public Integer call() throws Exception {
        <#list modelConfig.models as modelInfo>
            <#if modelInfo.groupKey??>
                <#if modelInfo.condition??>
                    if(${modelInfo.condition}){
                        <@generateCommand indent="      " modelInfo=modelInfo/>
                    }
                <#else>
                    <@generateCommand indent="      " modelInfo=modelInfo/>
                </#if>
            </#if>
        </#list>
    <#-- 填充DataModel -->
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
    <#list modelConfig.models as modelInfo>
        <#if modelInfo.groupKey??>
            dataModel.${modelInfo.groupKey} = ${modelInfo.groupKey};
        </#if>
    </#list>
    MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
