package com.baozi.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baozi.maker.meta.enums.FileGenerateTypeEnum;
import com.baozi.maker.meta.enums.FileTypeEnum;
import com.baozi.maker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 元信息校验
 *
 * @author zwb
 * @date 2024/12/22 21:00
 * @since 2024.0.1
 **/
public class MetaValidator {

    public static void doValidAndFill(Meta meta) {
        validAndFillMetaRoot(meta);
        validAndFillFileConfig(meta);
        validAndFillModelConfig(meta);
    }

    public static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        // modelConfig 默认值
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(modelInfoList)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo modelInfo : modelInfoList) {
            // 为group不校验
            if (StrUtil.isNotBlank(modelInfo.getGroupKey())) {
                // 生成中间参数
                String args = modelInfo.getModels().stream().map(m -> String.format("\"--%s\"", m.getFieldName()))
                        .collect(Collectors.joining(", "));
                modelInfo.setAllArgsStr(args);
                continue;
            }

            // 输出路径默认值
            String fieldName = modelInfo.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }

            String modelInfoType = modelInfo.getType();
            if (StrUtil.isBlank(modelInfoType)) {
                modelInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    public static void validAndFillFileConfig(Meta meta) {
        // fileConfig 默认值
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        // sourceRootPath：必填
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }
        // inputRootPath：.source + sourceRootPath 的最后一个层级路径
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath = ".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        if (StrUtil.isBlank(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }
        // outputRootPath：默认为当前路径下的 generated
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath = "generated";
        if (StrUtil.isBlank(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }
        String fileConfigType = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getValue();
        if (StrUtil.isBlank(fileConfigType)) {
            fileConfig.setType(defaultType);
        }

        // fileInfo 默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (!CollectionUtil.isNotEmpty(fileInfoList)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
            String type = fileInfo.getType();
            // 文件组不校验
            if (Objects.equals(type, FileTypeEnum.GROUP.getValue())) {
                continue;
            }
            // inputPath: 必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }

            // outputPath: 默认等于 inputPath
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isBlank(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }
            // type：默认 inputPath 有文件后缀（如 .java）为 file，否则为 dir
            if (StrUtil.isBlank(type)) {
                // 无文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }
            // generateType：如果文件结尾不为 Ftl，generateType 默认为 static，否则为 dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                // 为动态模板
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    public static void validAndFillMetaRoot(Meta meta) {
        // 校验并填充默认值
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.emptyToDefault(meta.getDescription(), "我的模板代码生成器");
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "yupi");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.yupi");
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setName(name);
        meta.setDescription(description);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createTime);
    }
}
