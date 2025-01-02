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
import java.util.Arrays;
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
     * @param inputFilePathList
     * @param modelInfo
     * @param searchStr
     * @param id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originProjectPath, List<String> inputFilePathList, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id) {
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

        // 生成模版文件
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        for (String inputFilePath : inputFilePathList) {
            String fileInputAbsolutePath = sourceRootPath + File.separator + inputFilePath;
            if (FileUtil.isDirectory(fileInputAbsolutePath)) {
                List<File> files = FileUtil.loopFiles(fileInputAbsolutePath);
                for (File file : files) {
                    Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, file, sourceRootPath);
                    fileInfoList.add(fileInfo);
                }
            } else {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, new File(fileInputAbsolutePath), sourceRootPath);
                fileInfoList.add(fileInfo);
            }
        }

        // Meta输出路径
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            // 追加配置
            newMeta.getFileConfig().getFiles().addAll(fileInfoList);
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
            fileConfig.setFiles(fileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);
        }
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    /**
     * 生成模版文件
     *
     * @param modelInfo
     * @param searchStr
     * @param file
     * @param sourceRootPath
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelInfo modelInfo, String searchStr, File file, String sourceRootPath) {
        String fileContent;
        // 相对路径
        String fileInputPath = file.getAbsolutePath().replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";

        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String newFileContent = StrUtil.replace(fileContent, searchStr, String.format("${%s}", modelInfo.getFieldName()));

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setInputPath(fileInputPath);
        if (Objects.equals(fileContent, newFileContent)) {
            // 和原文件内容一致，说明没有挖坑，无需生成模版文件
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        } else {
            // 文件配置信息
            fileInfo.setOutputPath(fileOutputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    public static void main(String[] args) {
        Meta meta = new Meta();
        String name = "acm-template-pro-generator2";
        String description = "ACM 示例模板生成器2";
        meta.setName(name);
        meta.setDescription(description);

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "samples/springboot-init-master";
        // String fileInputPath = "src/main/java/com/yupi/springbootinit/controller";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/esdao";
        String fileInputPath2 = "src/main/java/com/yupi/springbootinit/exception";
        // Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        // modelInfo.setFieldName("outputText");
        // modelInfo.setType("String");
        // modelInfo.setDefaultValue("sum = ");
        //
        // String searchStr = "Sum: ";

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        String searchStr = "class";

        long id = makeTemplate(meta, originProjectPath, Arrays.asList(fileInputPath1,fileInputPath2), modelInfo, searchStr, 2L);
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
