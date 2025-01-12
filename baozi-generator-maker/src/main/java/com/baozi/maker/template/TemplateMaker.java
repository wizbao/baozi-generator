package com.baozi.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baozi.maker.meta.Meta;
import com.baozi.maker.meta.enums.FileGenerateTypeEnum;
import com.baozi.maker.meta.enums.FileTypeEnum;
import com.baozi.maker.template.model.TemplateMakerConfig;
import com.baozi.maker.template.model.TemplateMakerFileConfig;
import com.baozi.maker.template.model.TemplateMakerModelConfig;
import com.baozi.maker.template.model.TemplateMakerOutputConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zwb
 * @date 2025/1/1 15:02
 * @since 2024.0.1
 **/
public class TemplateMaker {

    /**
     * 制作模版
     *
     * @param templateMakerConfig
     * @return
     */
    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        Long id = templateMakerConfig.getId();
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig fileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig modelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutputConfig outputConfig = templateMakerConfig.getOutputConfig();
        return makeTemplate(meta, originProjectPath, fileConfig, modelConfig, outputConfig, id);
    }

    /**
     * 制作模版
     *
     * @param newMeta
     * @param originProjectPath
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originProjectPath,
                                    TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, TemplateMakerOutputConfig templateMakerOutputConfig,
                                    Long id) {
        if (Objects.isNull(id)) {
            id = IdUtil.getSnowflakeNextId();
        }
        // 为了不污染原模板文件，复制副本到该项目，做到工作空间隔离
        String projectPath = System.getProperty("user.dir");
        String tempLatePath = projectPath + File.separator + ".temp" + File.separator + id;
        if (!FileUtil.exist(tempLatePath)) {
            FileUtil.mkdir(tempLatePath);
            FileUtil.copy(originProjectPath, tempLatePath, true);
        }

        // 输入信息
        String sourceRootPath = FileUtil.loopFiles(new File(tempLatePath), 1, null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath();

        List<Meta.FileConfig.FileInfo> fileInfoList = makeFileTemplates(templateMakerFileConfig, templateMakerModelConfig, sourceRootPath);
        List<Meta.ModelConfig.ModelInfo> modelInfoList = getModelInfoList(templateMakerModelConfig);

        // 生成配置文件
        // Meta输出路径
        String metaOutputPath = tempLatePath + File.separator + "meta.json";
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            // 追加配置
            newMeta.getFileConfig().getFiles().addAll(fileInfoList);
            newMeta.getModelConfig().getModels().addAll(modelInfoList);

            // 去重
            List<Meta.FileConfig.FileInfo> fileInfos = distinctFiles(newMeta.getFileConfig().getFiles());
            List<Meta.ModelConfig.ModelInfo> modelInfos = distinctModels(newMeta.getModelConfig().getModels());
            newMeta.getFileConfig().setFiles(fileInfos);
            newMeta.getModelConfig().setModels(modelInfos);
        } else {
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            fileConfig.setFiles(fileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            modelConfig.setModels(modelInfoList);
        }

        // 输出配置
        if (Objects.nonNull(templateMakerOutputConfig)) {
            // 文件外层和分组去重
            if (templateMakerOutputConfig.isRemoveGroupFilesFromRoot()) {
                List<Meta.FileConfig.FileInfo> files = newMeta.getFileConfig().getFiles();
                newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(files));
            }
        }

        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> getModelInfoList(TemplateMakerModelConfig templateMakerModelConfig) {
        List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        // 非空校验
        if (Objects.isNull(templateMakerModelConfig)) {
            return modelInfoList;
        }
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigs = templateMakerModelConfig.getModels();
        if (CollUtil.isEmpty(modelInfoConfigs)) {
            return modelInfoList;
        }

        // 模型组处理
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = modelInfoConfigs.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (Objects.nonNull(modelGroupConfig)) {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig, modelInfo);
            modelInfo.setModels(inputModelInfoList);
            modelInfoList.add(modelInfo);
        } else {
            modelInfoList.addAll(inputModelInfoList);
        }
        return modelInfoList;
    }

    /**
     * 生成模版文件
     *
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath) {

        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();

        // 非空校验
        if (Objects.isNull(templateMakerFileConfig)) {
            return fileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigs = templateMakerFileConfig.getFiles();
        if (CollUtil.isEmpty(fileInfoConfigs)) {
            return fileInfoList;
        }

        // 生成模版文件
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigs) {
            String inputFilePath = fileInfoConfig.getPath();
            // 如果是相对路径，要改为绝对路径
            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }
            // 过滤后的文件列表（不会存在目录）
            List<File> files = FileFilter.doFilter(fileInfoConfig.getFilterConfigList(), inputFilePath);
            // 文件后缀是 .ftl 不处理
            files = files.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file : files) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, file, sourceRootPath, fileInfoConfig);
                fileInfoList.add(fileInfo);
            }
        }

        // 文件组处理
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (Objects.nonNull(fileGroupConfig)) {
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            String condition = fileGroupConfig.getCondition();

            // 新增分组配置
            Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
            fileInfo.setGroupKey(groupKey);
            fileInfo.setGroupName(groupName);
            fileInfo.setType(FileTypeEnum.GROUP.getValue());
            fileInfo.setCondition(condition);
            fileInfo.setFiles(fileInfoList);

            // 文件全部放到一个分组内
            fileInfoList = new ArrayList<>();
            fileInfoList.add(fileInfo);
        }
        return fileInfoList;
    }

    /**
     * 生成模版文件
     *
     * @param templateMakerModelConfig
     * @param file
     * @param sourceRootPath
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, File file,
                                                             String sourceRootPath,TemplateMakerFileConfig.FileInfoConfig fileInfoConfig) {
        String fileContent;
        // 相对路径
        String fileInputPath = file.getAbsolutePath().replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";

        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        boolean haveTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (haveTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String newFileContent = fileContent;
        String replacement;
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            // 不是分组
            if (Objects.isNull(modelGroupConfig)) {
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                // 是分组
                replacement = String.format("${%s.%s}", modelGroupConfig.getGroupKey(), modelInfoConfig.getFieldName());
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        boolean contentEquals = StrUtil.equals(newFileContent, fileContent);
        if (!haveTemplateFile) {
            if (contentEquals) {
                // 和原文件内容一致，说明没有挖坑，无需生成模版文件
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                // 文件配置信息
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            // 有模版文件并且新增了新坑，更新模版文件
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }


    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 策略：同分组内文件 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey)
                );


        // 2. 同组内的文件配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .map(Meta.FileConfig.FileInfo::getFiles).flatMap(Collection::stream)
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }

        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4. 将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream().filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey())).collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );

        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }


}
