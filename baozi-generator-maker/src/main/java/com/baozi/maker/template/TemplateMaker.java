package com.baozi.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baozi.maker.meta.Meta;
import com.baozi.maker.meta.enums.FileGenerateTypeEnum;
import com.baozi.maker.meta.enums.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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
     * @param newMeta
     * @param originProjectPath
     * @param inputFilePath
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originProjectPath, String inputFilePath, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id) {
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
        String sourceRootPath = tempLatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        String fileInputPath = inputFilePath;
        String fileOutputPath = fileInputPath + ".ftl";


        // 生成模版文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        String fileContent;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String newFileContent = StrUtil.replace(fileContent, searchStr, String.format("${%s}", modelInfo.getFieldName()));
        FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // Meta输出路径
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta,CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            newMeta.getFileConfig().getFiles().add(fileInfo);
            newMeta.getModelConfig().getModels().add(modelInfo);

            // 去重
            List<Meta.FileConfig.FileInfo> fileInfos = distinctFiles(newMeta.getFileConfig().getFiles());
            List<Meta.ModelConfig.ModelInfo> modelInfos = distinctModels(newMeta.getModelConfig().getModels());
            newMeta.getFileConfig().setFiles(fileInfos);
            newMeta.getModelConfig().setModels(modelInfos);
        } else {
            // 生成Meta文件
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.add(fileInfo);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);

        }
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    public static void main(String[] args) {
        Meta meta = new Meta();
        String name = "acm-template-pro-generator2";
        String description = "ACM 示例模板生成器2";
        meta.setName(name);
        meta.setDescription(description);

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "samples/acm-template-pro";
        String fileInputPath = "src/com/yupi/acm/MainTemplate.java";

        // Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("sum = ");
        //
        // String searchStr = "Sum: ";

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        String searchStr = "MainTemplate";

        long id = makeTemplate(meta, originProjectPath, fileInputPath, modelInfo, searchStr, 1874404666146377728L);
        System.out.println("id = " + id);
    }

    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(
                fileInfoList.stream()
                        .collect(
                                Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, Function.identity(), (e, r) -> r)
                        ).values()
        );
        return newFileInfoList;
    }

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(
                modelInfoList.stream()
                        .collect(
                                Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                        ).values()
        );
        return newModelInfoList;
    }


}
