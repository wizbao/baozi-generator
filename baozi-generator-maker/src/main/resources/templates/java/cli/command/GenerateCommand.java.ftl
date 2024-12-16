package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.file.FileGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author zwb
 * @date 2024/12/11 23:42
 * @since 2024.0.1
 **/
@Data
@CommandLine.Command(name = "generate", version = "GenerateCommand 1.0", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {
    <#list modelConfig.models as modelInfo>
    <#if modelInfo.description??>
    /**
     * description = "${modelInfo.description}"
     */
    </#if>
    @CommandLine.Option(names = {"--${modelInfo.fieldName}"<#if modelInfo.abbr??>,"-${modelInfo.abbr}"</#if>}, <#if modelInfo.description??>description = "${modelInfo.description}", </#if>interactive = true, arity = "0..1", echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName}<#if modelInfo.defaultValue??> = ${modelInfo.defaultValue?c}</#if>;
    </#list>


    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        FileGenerator.doGenerate(dataModel);
        return 0;
    }
}
