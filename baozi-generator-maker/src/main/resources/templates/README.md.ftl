# ${name}

> ${description}
>
> 作者：${author}
>
> 基于 代码生成器项目 制作，感谢您的使用！

可以通过命令行交互式输入的方式动态生成想要的项目代码

## 使用说明

执行项目根目录下的脚本文件：
```xml
generator <命令> <选项参数>
 ```
示例命令：
```xml
generator generate <#list modelConfig.models as modelInfo><#if modelInfo.groupKey??><#list modelInfo.models as subModelInfo>--${subModelInfo.fieldName}<#if subModelInfo.abbr??>|-${subModelInfo.abbr}</#if></#list><#else>--${modelInfo.fieldName}<#if modelInfo.abbr??>|-${modelInfo.abbr}</#if> </#if></#list>
```

## 参数说明
<#list modelConfig.models as modelInfo>
${modelInfo?index + 1}）<#if modelInfo.groupKey??>${modelInfo.allArgsStr}<#else>${modelInfo.fieldName}</#if>

<#if modelInfo.groupKey??>
<#list modelInfo.models as subModelInfo>
类型：${subModelInfo.type}

描述：${subModelInfo.description}

默认值：${subModelInfo.defaultValue?c}

<#if subModelInfo.abbr??>缩写：-${subModelInfo.abbr}</#if>

</#list>
<#else>
类型：${modelInfo.type}

描述：${modelInfo.description}

默认值：${modelInfo.defaultValue?c}

<#if modelInfo.abbr??>缩写：-${modelInfo.abbr}</#if>
</#if>



</#list>


