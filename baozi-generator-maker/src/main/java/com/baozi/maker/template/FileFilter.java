package com.baozi.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baozi.maker.template.enums.FileFilterRangeEnum;
import com.baozi.maker.template.enums.FileFilterRuleEnum;
import com.baozi.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件过滤器
 *
 * @author zwb
 * @date 2025/1/4 21:45
 * @since 2024.0.1
 **/
public class FileFilter {

    /**
     * 单个文件过滤
     *
     * @param fileFilterConfigs 过滤规则
     * @param file              文件
     * @return 是否保留
     */
    private static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigs, File file) {
        if (CollUtil.isEmpty(fileFilterConfigs) || Objects.isNull(file)) {
            return true;
        }
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);
        // 过滤器校验的最终结果
        boolean result = false;
        for (FileFilterConfig fileFilterConfig : fileFilterConfigs) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            FileFilterRangeEnum rangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (Objects.isNull(rangeEnum)) {
                continue;
            }
            // 要过滤的内容
            String content = "";
            switch (rangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }
            FileFilterRuleEnum ruleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (Objects.isNull(ruleEnum)) {
                continue;
            }
            switch (ruleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
            // 有一个不满足直接返回
            if (BooleanUtil.isFalse(result)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对某个文件或目录过滤，返回过滤后的文件列表
     *
     * @param fileFilterConfigs 过滤规则
     * @param filePath 某个文件或目录的绝对路径
     * @return 返回过滤后的文件列表
     */
    public static List<File> doFilter(List<FileFilterConfig> fileFilterConfigs, String filePath) {
        return FileUtil.loopFiles(filePath).stream().filter(file -> doSingleFileFilter(fileFilterConfigs, file)).collect(Collectors.toList());
    }

}
