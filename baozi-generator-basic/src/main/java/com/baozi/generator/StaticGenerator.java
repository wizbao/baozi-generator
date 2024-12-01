package com.baozi.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @author zwb
 * @date 2024/12/1 15:05
 * @since 2024.0.1
 **/
public class StaticGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        String srcPath = projectPath+ File.separator+"samples"+File.separator+"acm-template";
        String tarPath = projectPath;
        copyFilesByHutool(srcPath,tarPath);
    }

    public static void copyFilesByHutool(String sourcePath, String targetPath) {
        FileUtil.copy(sourcePath, targetPath, false);
    }
}
