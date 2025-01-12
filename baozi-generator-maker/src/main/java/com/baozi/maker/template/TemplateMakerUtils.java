package com.baozi.maker.template;

import cn.hutool.core.util.StrUtil;
import com.baozi.maker.meta.Meta;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模版制作工具类
 *
 * @author zwb
 * @date 2025/1/11 21:15
 * @since 2024.0.1
 **/
public class TemplateMakerUtils {

    /**
     * 从未分组文件中移除组内的同名文件
     *
     * @param fileInfos
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfos) {
        // 获取到所有分组
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfos.stream().filter(file -> StrUtil.isNotBlank(file.getGroupKey())).collect(Collectors.toList());

        // 获取到所有分组内的文件列表
        // 获取所有分组内的文件输入路径集合
        Set<String> fileInputPathSet = groupFileInfoList.stream().map(Meta.FileConfig.FileInfo::getFiles).flatMap(Collection::stream).map(Meta.FileConfig.FileInfo::getInputPath).collect(Collectors.toSet());

        // 移除所有输入路径在集合中的外层文件
        return fileInfos.stream().filter(file -> !fileInputPathSet.contains(file.getInputPath())).collect(Collectors.toList());
    }

}
